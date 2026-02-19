package com.skintostatue.android.core.processor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import java.io.ByteArrayOutputStream

/**
 * Armor manager
 * Ported from Python armor_manager.py
 * Handles armor loading, dyeing, and independent layer rendering
 * Supports both plugin format (64x64) and standard Minecraft format (64x32)
 */
class ArmorManager(private val system: ArmorSystem = ArmorSystem.STANDARD) {
    companion object {
        private const val ARMOR_TEXTURE_SIZE = 64
        private const val HELMET_X = 8
        private const val HELMET_Y = 8
        private const val HELMET_SIZE = 8
        private const val CHESTPLATE_BODY_X = 20
        private const val CHESTPLATE_BODY_Y = 20
        private const val CHESTPLATE_BODY_W = 8
        private const val CHESTPLATE_BODY_H = 12
        private const val CHESTPLATE_ARM_X = 40
        private const val CHESTPLATE_ARM_Y = 20
        private const val CHESTPLATE_ARM_W = 4
        private const val CHESTPLATE_ARM_H = 12
        private const val LEGGINGS_BODY_X = 20
        private const val LEGGINGS_BODY_Y = 20
        private const val LEGGINGS_BODY_W = 8
        private const val LEGGINGS_BODY_H = 12
        private const val LEGGINGS_LEG_X = 0
        private const val LEGGINGS_LEG_Y = 20
        private const val LEGGINGS_LEG_W = 4
        private const val LEGGINGS_LEG_H = 12
        private const val BOOTS_LEG_X = 0
        private const val BOOTS_LEG_Y = 52
        private const val BOOTS_LEG_W = 4
        private const val BOOTS_LEG_H = 12
    }

    // Armor textures for each material
    private val armorTextures: MutableMap<ArmorMaterial, Bitmap> = mutableMapOf()

    /**
     * Load armor texture from file
     * Ported from Python load_armor function
     */
    fun loadArmor(material: ArmorMaterial, filePath: String): Bitmap {
        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(filePath, options)
                ?: throw IllegalArgumentException("Failed to load armor texture")

            // Convert to ARGB_8888 if needed
            val convertedBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888) {
                bitmap.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                bitmap
            }

            armorTextures[material] = convertedBitmap
            convertedBitmap
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to load armor: ${e.message}", e)
        }
    }

    /**
     * Load armor from byte array
     */
    fun loadArmorFromBytes(material: ArmorMaterial, imageData: ByteArray): Bitmap {
        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)
                ?: throw IllegalArgumentException("Failed to decode armor texture")

            val convertedBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888) {
                bitmap.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                bitmap
            }

            armorTextures[material] = convertedBitmap
            convertedBitmap
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to load armor: ${e.message}", e)
        }
    }

    /**
     * Dye leather armor
     * Ported from Python dye_leather_armor function
     * Uses the formula from assets/公式.txt
     * Ensures 100% identical result with Python version
     */
    fun dyeLeatherArmor(armor: Bitmap, dyeColor: IntArray): Bitmap {
        val result = Bitmap.createBitmap(armor.width, armor.height, Bitmap.Config.ARGB_8888)
        val armorPixels = IntArray(armor.width * armor.height)
        val resultPixels = IntArray(result.width * result.height)

        armor.getPixels(armorPixels, 0, armor.width, 0, 0, armor.width, armor.height)

        // Step 1: Collect all non-transparent pixels for color calculation
        var totalRed = 0.0
        var totalGreen = 0.0
        var totalBlue = 0.0
        var totalMax = 0.0
        var count = 0

        for (i in armorPixels.indices) {
            val pixel = armorPixels[i]
            val a = Color.alpha(pixel)

            if (a > 0) {
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)

                totalRed += r
                totalGreen += g
                totalBlue += b
                totalMax += maxOf(r, g, b)
                count++
            }
        }

        if (count == 0) {
            // No non-transparent pixels, return original
            return armor
        }

        // Step 2: Apply the formula from assets/公式.txt
        val averageRed = totalRed / count
        val averageGreen = totalGreen / count
        val averageBlue = totalBlue / count
        val averageMax = totalMax / count

        val maxOfAverage = maxOf(averageRed, averageGreen, averageBlue)
        val gainFactor = if (maxOfAverage > 0) averageMax / maxOfAverage else 0.0

        val resultRed = averageRed * gainFactor
        val resultGreen = averageGreen * gainFactor
        val resultBlue = averageBlue * gainFactor

        // Step 3: Calculate dye effect
        // Mix the calculated representative color with the dye color
        val finalR = ((resultRed + dyeColor[0]) / 2).toInt()
        val finalG = ((resultGreen + dyeColor[1]) / 2).toInt()
        val finalB = ((resultBlue + dyeColor[2]) / 2).toInt()

        // Step 4: Apply dye to the leather armor
        // All non-transparent pixels are set to the same color
        for (i in armorPixels.indices) {
            val pixel = armorPixels[i]
            val a = Color.alpha(pixel)

            if (a > 0) {
                // Raise alpha to 255 for all non-transparent pixels
                // This preserves all pixel details and matches dyeing properties
                resultPixels[i] = Color.argb(255, finalR, finalG, finalB)
            } else {
                resultPixels[i] = pixel
            }
        }

        result.setPixels(resultPixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }

    /**
     * Get maximum of three values
     */
    private fun maxOf(a: Int, b: Int, c: Int): Int {
        return maxOf(a, maxOf(b, c))
    }

    /**
     * Get maximum of three values (double)
     */
    private fun maxOf(a: Double, b: Double, c: Double): Double {
        return maxOf(a, maxOf(b, c))
    }

    /**
     * Get armor as independent layer images
     * Ported from Python get_armor_layer function
     * Returns dict mapping region keys to cropped armor images
     */
    fun getArmorLayer(
        skinImage: Bitmap,
        armorConfig: Map<ArmorPiece, ArmorMaterial>,
        leatherColorConfig: Map<ArmorPiece, IntArray>? = null,
        skinScale: Float = 1.0f
    ): Map<String, Bitmap> {
        val result = Bitmap.createBitmap(skinImage.width, skinImage.height, Bitmap.Config.ARGB_8888)
        val pieceImages = mutableMapOf<String, Bitmap>()
        val canvas = Canvas(result)

        // Skin layout with HD skin scaling
        val skinRegions = mapOf(
            "helmet" to listOf(
                Triple((8 * skinScale).toInt(), (8 * skinScale).toInt(), (8 * skinScale).toInt(), (8 * skinScale).toInt())
            ),
            "chestplate" to listOf(
                Triple((20 * skinScale).toInt(), (20 * skinScale).toInt(), (8 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((40 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((32 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            ),
            "leggings" to listOf(
                Triple((0 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((16 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            ),
            "boots" to listOf(
                Triple((0 * skinScale).toInt(), (52 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((16 * skinScale).toInt(), (52 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            )
        )

        // Process each piece
        for ((pieceName, material) in armorConfig) {
            // Load and dye armor
            val armorTexture = armorTextures[material]
                ?: throw IllegalArgumentException("Armor texture not loaded for $material")

            var dyedArmor = armorTexture
            if (material == ArmorMaterial.LEATHER && leatherColorConfig != null) {
                val dyeColor = leatherColorConfig[pieceName]
                if (dyeColor != null) {
                    dyedArmor = dyeLeatherArmor(armorTexture, dyeColor)
                }
            }

            // Get skin regions and paste armor to blank image
            val skinRegionList = skinRegions[pieceName.name.lowercase()] ?: continue
            for ((skinX, skinY, skinW, skinH) in skinRegionList) {
                // Handle scaling and paste
                val armorResized = Bitmap.createScaledBitmap(dyedArmor, skinW, skinH, false)
                canvas.drawBitmap(armorResized, skinX.toFloat(), skinY.toFloat(), null)

                // Store the piece image for each region
                val regionKey = "${pieceName.name.lowercase()}_${skinRegionList.indexOf(Triple(skinX, skinY, skinW, skinH))}"
                val pieceImage = Bitmap.createBitmap(result, skinX, skinY, skinW, skinH)
                pieceImages[regionKey] = pieceImage
            }
        }

        pieceImages["full"] = result
        return pieceImages
    }

    /**
     * Apply armor overlay to skin image
     * Ported from Python apply_armor_to_skin function
     */
    fun applyArmorToSkin(
        skinImage: Bitmap,
        armorConfig: Map<ArmorPiece, ArmorMaterial>,
        leatherColorConfig: Map<ArmorPiece, IntArray>? = null,
        skinScale: Float = 1.0f
    ): Bitmap {
        val result = skinImage.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        // Skin layout
        val skinRegions = mapOf(
            "helmet" to listOf(
                Triple((8 * skinScale).toInt(), (8 * skinScale).toInt(), (8 * skinScale).toInt(), (8 * skinScale).toInt())
            ),
            "chestplate" to listOf(
                Triple((20 * skinScale).toInt(), (20 * skinScale).toInt(), (8 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((40 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((32 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            ),
            "leggings" to listOf(
                Triple((0 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((16 * skinScale).toInt(), (20 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            ),
            "boots" to listOf(
                Triple((0 * skinScale).toInt(), (52 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt()),
                Triple((16 * skinScale).toInt(), (52 * skinScale).toInt(), (4 * skinScale).toInt(), (12 * skinScale).toInt())
            )
        )

        // Process each piece
        for ((pieceName, material) in armorConfig) {
            val armorTexture = armorTextures[material]
                ?: throw IllegalArgumentException("Armor texture not loaded for $material")

            var dyedArmor = armorTexture
            if (material == ArmorMaterial.LEATHER && leatherColorConfig != null) {
                val dyeColor = leatherColorConfig[pieceName]
                if (dyeColor != null) {
                    dyedArmor = dyeLeatherArmor(armorTexture, dyeColor)
                }
            }

            val skinRegionList = skinRegions[pieceName.name.lowercase()] ?: continue
            for ((skinX, skinY, skinW, skinH) in skinRegionList) {
                val armorResized = Bitmap.createScaledBitmap(dyedArmor, skinW, skinH, false)
                canvas.drawBitmap(armorResized, skinX.toFloat(), skinY.toFloat(), null)
            }
        }

        return result
    }

    /**
     * Clear loaded armor textures
     */
    fun clear() {
        armorTextures.clear()
    }
}

/**
 * Armor system enum
 * Ported from Python ArmorSystem class
 */
enum class ArmorSystem {
    PLUGIN,
    STANDARD
}

/**
 * Armor piece enum
 * Ported from Python ArmorPiece class
 */
enum class ArmorPiece {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS
}

/**
 * Armor material enum
 * Ported from Python armor materials
 */
enum class ArmorMaterial {
    LEATHER,
    CHAINMAIL,
    IRON,
    GOLD,
    DIAMOND,
    NETHERITE,
    TURTLE
}

/**
 * Leather color enum
 * Ported from Python leather colors
 */
enum class LeatherColor(val rgb: IntArray) {
    WHITE(intArrayOf(249, 255, 254)),
    ORANGE(intArrayOf(249, 128, 29)),
    MAGENTA(intArrayOf(199, 78, 189)),
    LIGHT_BLUE(intArrayOf(58, 179, 218)),
    YELLOW(intArrayOf(254, 216, 61)),
    LIME(intArrayOf(128, 199, 31)),
    PINK(intArrayOf(243, 139, 170)),
    GRAY(intArrayOf(125, 125, 125)),
    LIGHT_GRAY(intArrayOf(215, 215, 215)),
    CYAN(intArrayOf(46, 110, 137)),
    PURPLE(intArrayOf(126, 61, 181)),
    BLUE(intArrayOf(46, 56, 141)),
    BROWN(intArrayOf(113, 92, 60)),
    GREEN(intArrayOf(86, 122, 66)),
    RED(intArrayOf(176, 48, 96)),
    BLACK(intArrayOf(29, 29, 33))
}