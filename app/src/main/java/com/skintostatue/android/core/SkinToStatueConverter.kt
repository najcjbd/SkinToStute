package com.skintostatue.android.core

import android.graphics.Bitmap
import android.graphics.Color
import com.skintostatue.android.core.generator.LitematicaGenerator
import com.skintostatue.android.core.generator.McStructureGenerator
import com.skintostatue.android.core.generator.SchematicGenerator
import com.skintostatue.android.core.model.*
import com.skintostatue.android.core.processor.ArmorManager
import com.skintostatue.android.core.processor.ArmorMaterial
import com.skintostatue.android.core.processor.ArmorPiece
import com.skintostatue.android.core.processor.ImageFilters
import com.skintostatue.android.core.processor.SkinProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Main converter class
 * Ported from Python main.py
 * Orchestrates all modules to convert Minecraft skins to schematic files
 * Includes ALL algorithms, logic, and operations from Python version
 */
class SkinToStatueConverter(
    private val config: Config = Config()
) {
    private val blockDatabase = BlockDatabase()
    private val skinProcessor = SkinProcessor()
    private val armorManager = ArmorManager(system = ArmorSystem.STANDARD)

    // Create optimized block index for fast color matching
    // Filter falling blocks if configured (like Python version)
    private val availableBlocks = if (config.excludeFallingBlocks) {
        blockDatabase.getBlocksWithoutFalling()
    } else {
        blockDatabase.getAllBlocks()
    }
    private val blockIndex = BlockIndex(availableBlocks, config.exactMode)

    /**
     * Convert skin to schematic file
     * Ported from Python convert function
     * Returns the generated schematic data
     */
    suspend fun convert(
        skinImage: Bitmap,
        armorConfig: Map<ArmorPiece, ArmorMaterial>? = null,
        leatherColorConfig: Map<ArmorPiece, IntArray>? = null
    ): ConversionResult = withContext(Dispatchers.Default) {
        // Validate settings
        validateConfig()

        // Process skin
        val processedSkin = processSkin(skinImage)

        // Process armor if enabled
        val skinWithArmor = if (config.armorEnabled && armorConfig != null) {
            processArmor(processedSkin, armorConfig, leatherColorConfig)
        } else {
            processedSkin
        }

        // Generate blocks
        val blocks = generateBlocks(skinWithArmor)

        // Generate schematic based on output format
        val schematicData = when (config.outputFormat) {
            OutputFormat.SPONGE_SCHEMATIC -> generateSchematic(blocks)
            OutputFormat.LITEMATICA -> generateLitematic(blocks)
            OutputFormat.MCSTRUCTURE -> generateMcStructure(blocks)
        }

        ConversionResult(
            data = schematicData,
            format = config.outputFormat,
            blockCount = blocks.size,
            uniqueBlockCount = blocks.map { it.third }.distinct().size,
            dimensions = calculateDimensions(blocks)
        )
    }

    /**
     * Process skin image
     * Apply filters and extract regions
     * Ported from Python image processing logic
     */
    private fun processSkin(skinImage: Bitmap): Bitmap {
        var processed = skinImage

        // Apply image filters in the same order as Python
        if (config.hue != 0.0f) {
            processed = ImageFilters.applyHue(processed, config.hue)
        }
        if (config.saturation != 1.0f) {
            processed = ImageFilters.applySaturation(processed, config.saturation)
        }
        if (config.brightness != 1.0f) {
            processed = ImageFilters.applyBrightness(processed, config.brightness)
        }
        if (config.contrast != 1.0f) {
            processed = ImageFilters.applyContrast(processed, config.contrast)
        }
        if (config.posterize > 0) {
            processed = ImageFilters.applyPosterize(processed, config.posterize)
        }

        return processed
    }

    /**
     * Process armor
     * Apply armor overlay to skin
     */
    private fun processArmor(
        skinImage: Bitmap,
        armorConfig: Map<ArmorPiece, ArmorMaterial>,
        leatherColorConfig: Map<ArmorPiece, IntArray>?
    ): Bitmap {
        val skinScale = skinImage.width.toFloat() / 64f
        return armorManager.applyArmorToSkin(skinImage, armorConfig, leatherColorConfig, skinScale)
    }

    /**
     * Generate blocks from skin image
     * Ported from Python _add_region_to_schematic function
     * Includes ALL algorithms: transparency handling, scaling, orientation, priority system
     */
    private fun generateBlocks(skinImage: Bitmap): List<Triple<Int, Int, Int, String>> {
        val blocks = mutableListOf<Triple<Int, Int, Int, String>>()

        // Apply scale
        val scale = config.scale
        val skinScale = skinImage.width.toFloat() / 64f

        // Create color difference calculator based on color mode
        val diffFunc = when (config.colorMode) {
            ColorMode.RGB -> ColorMatcher.RGBDiff()
            ColorMode.ABSRGB -> ColorMatcher.AbsRGBDiff()
            ColorMode.HSL -> ColorMatcher.HSLDiff()
            ColorMode.HSB -> ColorMatcher.HSBDiff()
            ColorMode.LAB -> ColorMatcher.LABDiff()
        }

        // Get all regions with their coordinates
        val skinProcessor = SkinProcessor()
        skinProcessor.skinImage = skinImage

        val regions = mapOf(
            "head" to Pair(8, 8),
            "body" to Pair(20, 20),
            "right_arm" to Pair(40, 20),
            "left_arm" to Pair(32, 52),
            "right_leg" to Pair(0, 20),
            "left_leg" to Pair(16, 52)
        )

        // Process each region
        for ((regionName, (baseX, baseY)) in regions) {
            if (!shouldIncludeRegion(regionName)) continue

            val regionWidth = when (regionName) {
                "head" -> 8
                "body" -> 8
                "right_arm" -> if (skinProcessor.skinFormat == com.skintostatue.android.core.processor.SkinFormat.SLIM) 3 else 4
                "left_arm" -> if (skinProcessor.skinFormat == com.skintostatue.android.core.processor.SkinFormat.SLIM) 3 else 4
                "right_leg" -> 4
                "left_leg" -> 4
                else -> 8
            }

            val regionHeight = 12

            // Extract region pixels
            val regionPixels = extractRegionPixels(skinImage, baseX, baseY, regionWidth, regionHeight)

            // Process each pixel with all algorithms
            for (y in 0 until regionHeight) {
                for (x in 0 until regionWidth) {
                    val pixel = regionPixels[y * regionWidth + x]
                    val alpha = (pixel shr 24) and 0xFF

                    // Handle transparency based on alpha value (reference PlayerStatueBuilder ColorMaps.java)
                    if (alpha < 32) {
                        // Almost transparent, skip (return air)
                        continue
                    }

                    val wantTransparent = alpha < 200

                    // Extract RGB values
                    val r = (pixel shr 16) and 0xFF
                    val g = (pixel shr 8) and 0xFF
                    val b = pixel and 0xFF
                    val pixelColor = intArrayOf(r, g, b, alpha)

                    // Use optimized block index for fast color matching with priority system
                    val bestBlock = blockIndex.findBestMatch(
                        pixelColor, diffFunc, config.colorWeights, wantTransparent
                    )

                    if (bestBlock != null) {
                        // Add block to schematic with scaling (exactly as Python version)
                        if (scale >= 1.0f) {
                            // For scale > 1, replicate blocks
                            val scaleInt = scale.toInt()
                            for (sy in 0 until scaleInt) {
                                for (sx in 0 until scaleInt) {
                                    for (sz in 0 until scaleInt) {
                                        // Calculate position with Y-axis flip (height - 1 - y)
                                        val posX = baseX + (x * scale).toInt() + sx
                                        val posY = baseY + ((regionHeight - 1 - y) * scale).toInt() + sy
                                        val posZ = sz

                                        // Apply orientation and offset
                                        val (finalX, finalY, finalZ) = applyOrientationAndOffset(
                                            posX, posY, posZ,
                                            config
                                        )

                                        blocks.add(Triple(finalX, finalY, finalZ, bestBlock.name))
                                    }
                                }
                            }
                        } else {
                            // For scale < 1, sample blocks
                            val posX = (baseX + x * scale).toInt()
                            val posY = (baseY + (regionHeight - 1 - y) * scale).toInt()
                            val posZ = 0

                            // Apply orientation and offset
                            val (finalX, finalY, finalZ) = applyOrientationAndOffset(
                                posX, posY, posZ,
                                config
                            )

                            blocks.add(Triple(finalX, finalY, finalZ, bestBlock.name))
                        }
                    }
                }
            }
        }

        return blocks
    }

    /**
     * Extract pixels from a region
     * Ported from Python pixel extraction logic
     */
    private fun extractRegionPixels(
        image: Bitmap,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): IntArray {
        val pixels = IntArray(width * height)
        image.getPixels(pixels, 0, width, x, y, width, height)
        return pixels
    }

    /**
     * Apply orientation and offset to coordinates
     * Ported from Python orientation logic
     */
    private fun applyOrientationAndOffset(
        x: Int,
        y: Int,
        z: Int,
        config: Config
    ): Triple<Int, Int, Int> {
        var finalX = x
        var finalY = y
        var finalZ = z

        // Apply direction rotation
        when (config.direction) {
            Direction.NORTH -> {
                // No rotation
            }
            Direction.SOUTH -> {
                finalX = -x
                finalZ = -z
            }
            Direction.EAST -> {
                finalX = z
                finalZ = -x
            }
            Direction.WEST -> {
                finalX = -z
                finalZ = x
            }
        }

        // Apply offset
        finalX += config.offsetX
        finalY += config.offsetY
        finalZ += config.offsetZ

        // Apply flips
        if (config.flipHorizontal) {
            finalX = -finalX
        }
        if (config.flipVertical) {
            finalY = -finalY
        }

        return Triple(finalX, finalY, finalZ)
    }

    /**
     * Check if a region should be included
     * Ported from Python region inclusion logic
     */
    private fun shouldIncludeRegion(regionName: String): Boolean {
        return when (regionName) {
            "head" -> config.includeHead
            "body" -> config.includeBody
            "right_arm", "left_arm" -> config.includeArms
            "right_leg", "left_leg" -> config.includeLegs
            else -> true
        }
    }

    /**
     * Generate Sponge Schematic
     * Ported from Python schematic generation logic
     */
    private fun generateSchematic(blocks: List<Triple<Int, Int, Int, String>>): ByteArray {
        val generator = SchematicGenerator()
        generator.addBlocks(blocks.map { Triple(it.first, it.second, it.third, it.third) })
        return generator.generateSpongeSchematic()
    }

    /**
     * Generate Litematic
     * Ported from Python litematic generation logic
     */
    private fun generateLitematic(blocks: List<Triple<Int, Int, Int, String>>): ByteArray {
        val generator = LitematicaGenerator()
        blocks.forEach { generator.addBlock(it.first, it.second, it.third, it.third) }
        return generator.generateLitematic()
    }

    /**
     * Generate McStructure
     * Ported from Python mcstructure generation logic
     */
    private fun generateMcStructure(blocks: List<Triple<Int, Int, Int, String>>): ByteArray {
        val generator = McStructureGenerator()
        blocks.forEach { generator.addBlock(it.first, it.second, it.third, it.third) }
        return generator.generateMcStructure()
    }

    /**
     * Calculate dimensions from blocks
     * Ported from Python dimension calculation logic
     */
    private fun calculateDimensions(blocks: List<Triple<Int, Int, Int, String>>): Triple<Int, Int, Int> {
        if (blocks.isEmpty()) {
            return Triple(0, 0, 0)
        }

        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var minZ = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        var maxZ = Int.MIN_VALUE

        for ((x, y, z, _) in blocks) {
            minX = minOf(minX, x)
            minY = minOf(minY, y)
            minZ = minOf(minZ, z)
            maxX = maxOf(maxX, x)
            maxY = maxOf(maxY, y)
            maxZ = maxOf(maxZ, z)
        }

        return Triple(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1)
    }

    /**
     * Validate configuration
     * Ported from Python configuration validation logic
     * Exact same validation ranges as Python version
     */
    private fun validateConfig() {
        if (config.scale <= 0) {
            throw IllegalArgumentException("Scale must be greater than 0")
        }

        if (config.hue < 0.0f || config.hue > 1.0f) {
            throw IllegalArgumentException("Hue value must be between 0.0 and 1.0")
        }

        if (config.saturation < 0.0f || config.saturation > 1.0f) {
            throw IllegalArgumentException("Saturation value must be between 0.0 and 1.0")
        }

        if (config.brightness < 0.0f || config.brightness > 1.0f) {
            throw IllegalArgumentException("Brightness value must be between 0.0 and 1.0")
        }

        if (config.contrast < 0.0f || config.contrast > 1.0f) {
            throw IllegalArgumentException("Contrast value must be between 0.0 and 1.0")
        }

        if (config.posterize < 0 || config.posterize > 128) {
            throw IllegalArgumentException("Posterize value must be between 0 and 128")
        }
    }

    /**
     * Clear resources
     */
    fun clear() {
        armorManager.clear()
        blockIndex.clearCache()
    }
}

/**
 * Conversion result data class
 */
data class ConversionResult(
    val data: ByteArray,
    val format: OutputFormat,
    val blockCount: Int,
    val uniqueBlockCount: Int,
    val dimensions: Triple<Int, Int, Int>
)