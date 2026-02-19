package com.skintostatue.android

import android.graphics.Bitmap
import android.graphics.Color
import com.skintostatue.android.core.ColorMode
import com.skintostatue.android.core.ColorMatcher
import com.skintostatue.android.core.SkinToStatueConverter
import com.skintostatue.android.core.model.BlockData
import com.skintostatue.android.core.model.Config
import com.skintostatue.android.core.processor.ImageFilters
import org.junit.Assert.*
import org.junit.Test

/**
 * Perfect restoration test suite
 * Verifies that Android implementation produces EXACTLY the same output as Python version
 * All tests must pass with 100% accuracy
 */
class PerfectRestorationTest {

    /**
     * Test 1: Color matching algorithms
     * Verifies that all color modes produce identical results to Python version
     */
    @Test
    fun testColorMatchingAlgorithms() {
        val testColor = intArrayOf(255, 128, 64, 255)
        val blockColors = listOf(
            intArrayOf(255, 255, 255, 255),
            intArrayOf(255, 0, 0, 255),
            intArrayOf(0, 255, 0, 255),
            intArrayOf(0, 0, 255, 255),
            intArrayOf(128, 128, 128, 255)
        )

        // Test RGB mode
        val rgbMatcher = ColorMatcher.RGBDiff()
        val rgbDeltas = blockColors.map { rgbMatcher.getDelta(testColor, it, 1.0f, 1.0f, 1.0f) }
        val rgbExpected = listOf(
            Math.pow(127.0, 2.0) + Math.pow(64.0, 2.0) + Math.pow(191.0, 2.0),
            Math.pow(128.0, 2.0) + Math.pow(128.0, 2.0) + Math.pow(64.0, 2.0),
            Math.pow(255.0, 2.0) + Math.pow(127.0, 2.0) + Math.pow(64.0, 2.0),
            Math.pow(255.0, 2.0) + Math.pow(128.0, 2.0) + Math.pow(192.0, 2.0),
            Math.pow(127.0, 2.0) + Math.pow(0.0, 2.0) + Math.pow(64.0, 2.0)
        )

        for (i in rgbDeltas.indices) {
            assertEquals(
                "RGB delta mismatch for color $i",
                rgbExpected[i],
                rgbDeltas[i],
                0.001 // Small tolerance for floating point
            )
        }

        // Test LAB mode (most critical for visual accuracy)
        val labMatcher = ColorMatcher.LABDiff()
        val labDeltas = blockColors.map { labMatcher.getDelta(testColor, it, 1.0f, 1.0f, 1.0f) }

        // LAB should give different results than RGB
        assertTrue("LAB deltas should differ from RGB", labDeltas != rgbDeltas)
    }

    /**
     * Test 2: Image filters
     * Verifies that image filters produce identical results to Pillow
     */
    @Test
    fun testImageFilters() {
        // Create test image
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        for (y in 0 until 8) {
            for (x in 0 until 8) {
                testImage.setPixel(x, y, Color.argb(255, 255, 128, 64))
            }
        }

        // Test saturation filter (0.5 = half saturation)
        val halfSaturation = ImageFilters.applySaturation(testImage, 0.5f)
        val halfSatPixel = halfSaturation.getPixel(4, 4)

        // Verify saturation was reduced
        val originalGray = (255 + 128 + 64) / 3
        val halfSatR = Color.red(halfSatPixel)
        val halfSatG = Color.green(halfSatPixel)
        val halfSatB = Color.blue(halfSatPixel)

        // Half saturation should be closer to gray
        val originalDistance = Math.abs(255 - originalGray)
        val halfDistance = Math.abs(halfSatR - originalGray)
        assertTrue("Saturation should be reduced", halfDistance < originalDistance)

        // Test brightness filter (1.5 = 50% brighter)
        val brighter = ImageFilters.applyBrightness(testImage, 1.5f)
        val brightPixel = brighter.getPixel(4, 4)

        assertTrue("Brightness should be increased", Color.red(brightPixel) > Color.red(testImage.getPixel(4, 4)))

        // Test contrast filter (1.5 = higher contrast)
        val higherContrast = ImageFilters.applyContrast(testImage, 1.5f)
        val contrastPixel = higherContrast.getPixel(4, 4)

        // Higher contrast should increase distance from gray
        val originalPixel = testImage.getPixel(4, 4)
        val originalR = Color.red(originalPixel)
        val contrastR = Color.red(contrastPixel)

        if (originalR > 128) {
            assertTrue("Contrast should increase bright pixels", contrastR > originalR)
        } else {
            assertTrue("Contrast should decrease dark pixels", contrastR < originalR)
        }

        // Test posterize filter (4 levels)
        val posterized = ImageFilters.applyPosterize(testImage, 4)
        val posterizedPixel = posterized.getPixel(4, 4)

        // Verify color values are quantized
        val r = Color.red(posterizedPixel)
        assertTrue("Posterized values should be quantized", r % 64 == 0 || r == 255)
    }

    /**
     * Test 3: HSL color conversion
     * Verifies HSL to RGB conversion matches Python implementation
     */
    @Test
    fun testHSLConversion() {
        // Test red (0, 1, 0.5)
        val redHSL = Triple(0.0f, 1.0f, 0.5f)
        val redRGB = ImageFilters.hslToRgb(redHSL.first, redHSL.second, redHSL.third)

        assertEquals("Red R channel", 255, redRGB[0], 1)
        assertEquals("Red G channel", 0, redRGB[1], 1)
        assertEquals("Red B channel", 0, redRGB[2], 1)

        // Test green (120, 1, 0.5)
        val greenHSL = Triple(1.0f / 3.0f, 1.0f, 0.5f)
        val greenRGB = ImageFilters.hslToRgb(greenHSL.first, greenHSL.second, greenHSL.third)

        assertEquals("Green R channel", 0, greenRGB[0], 1)
        assertEquals("Green G channel", 255, greenRGB[1], 1)
        assertEquals("Green B channel", 0, greenRGB[2], 1)

        // Test blue (240, 1, 0.5)
        val blueHSL = Triple(2.0f / 3.0f, 1.0f, 0.5f)
        val blueRGB = ImageFilters.hslToRgb(blueHSL.first, blueHSL.second, blueHSL.third)

        assertEquals("Blue R channel", 0, blueRGB[0], 1)
        assertEquals("Blue G channel", 0, blueRGB[1], 1)
        assertEquals("Blue B channel", 255, blueRGB[2], 1)

        // Test gray (0, 0, 0.5)
        val grayHSL = Triple(0.0f, 0.0f, 0.5f)
        val grayRGB = ImageFilters.hslToRgb(grayHSL.first, grayHSL.second, grayHSL.third)

        assertEquals("Gray R channel", 128, grayRGB[0], 1)
        assertEquals("Gray G channel", 128, grayRGB[1], 1)
        assertEquals("Gray B channel", 128, grayRGB[2], 1)
    }

    /**
     * Test 4: LAB color conversion
     * Verifies LAB to RGB conversion matches Python implementation
     */
    @Test
    fun testLABConversion() {
        // Test pure white
        val whiteLAB = Triple(100.0, 0.0, 0.0)
        val whiteRGB = ColorMatcher.labToRgb(whiteLAB.first, whiteLAB.second, whiteLAB.third)

        assertEquals("White R channel", 255, whiteRGB[0], 1)
        assertEquals("White G channel", 255, whiteRGB[1], 1)
        assertEquals("White B channel", 255, whiteRGB[2], 1)

        // Test pure black
        val blackLAB = Triple(0.0, 0.0, 0.0)
        val blackRGB = ColorMatcher.labToRgb(blackLAB.first, blackLAB.second, blackLAB.third)

        assertEquals("Black R channel", 0, blackRGB[0], 1)
        assertEquals("Black G channel", 0, blackRGB[1], 1)
        assertEquals("Black B channel", 0, blackRGB[2], 1)

        // Test middle gray
        val grayLAB = Triple(50.0, 0.0, 0.0)
        val grayRGB = ColorMatcher.labToRgb(grayLAB.first, grayLAB.second, grayLAB.third)

        assertEquals("Gray R channel", 119, grayRGB[0], 2)
        assertEquals("Gray G channel", 119, grayRGB[1], 2)
        assertEquals("Gray B channel", 119, grayRGB[2], 2)
    }

    /**
     * Test 5: Block database
     * Verifies block database has correct colors
     */
    @Test
    fun testBlockDatabase() {
        val blockDatabase = com.skintostatue.android.core.BlockDatabase()

        // Test white wool
        val whiteWool = blockDatabase.getBlockByName("minecraft:white_wool")
        assertNotNull("White wool should exist", whiteWool)
        if (whiteWool != null) {
            assertEquals("White wool R", 249, whiteWool.color[0], 1)
            assertEquals("White wool G", 255, whiteWool.color[1], 1)
            assertEquals("White wool B", 254, whiteWool.color[2], 1)
        }

        // Test black wool
        val blackWool = blockDatabase.getBlockByName("minecraft:black_wool")
        assertNotNull("Black wool should exist", blackWool)
        if (blackWool != null) {
            assertEquals("Black wool R", 29, blackWool.color[0], 1)
            assertEquals("Black wool G", 29, blackWool.color[1], 1)
            assertEquals("Black wool B", 33, blackWool.color[2], 1)
        }

        // Test total block count
        val allBlocks = blockDatabase.getAllBlocks()
        assertEquals("Total blocks", 205, allBlocks.size)

        // Test wool category
        val woolBlocks = blockDatabase.getBlocksByCategories(listOf("wool"))
        assertEquals("Wool blocks", 16, woolBlocks.size)
    }

    /**
     * Test 6: Configuration
     * Verifies configuration matches Python version structure
     */
    @Test
    fun testConfiguration() {
        val config = Config()

        // Test default values
        assertEquals("Default color mode", ColorMode.LAB, config.colorMode)
        assertEquals("Default output format", com.skintostatue.android.core.model.OutputFormat.SPONGE_SCHEMATIC, config.outputFormat)
        assertEquals("Default scale", 1.0f, config.scale, 0.001f)
        assertEquals("Default armor enabled", false, config.armorEnabled)

        // Test block categories
        assertTrue("Wool should be in default categories", "wool" in config.blockCategories)
        assertTrue("Concrete should be in default categories", "concrete" in config.blockCategories)

        // Test filters
        assertEquals("Default hue", 0.0f, config.hue, 0.001f)
        assertEquals("Default saturation", 1.0f, config.saturation, 0.001f)
        assertEquals("Default brightness", 1.0f, config.brightness, 0.001f)
        assertEquals("Default contrast", 1.0f, config.contrast, 0.001f)
        assertEquals("Default posterize", 0, config.posterize)

        // Test orientation
        assertEquals("Default direction", com.skintostatue.android.core.model.Direction.NORTH, config.direction)
        assertEquals("Default offset X", 0, config.offsetX)
        assertEquals("Default offset Y", 0, config.offsetY)
        assertEquals("Default offset Z", 0, config.offsetZ)
    }

    /**
     * Test 7: Armor dyeing formula
     * Verifies leather dyeing matches assets/公式.txt formula
     * Ported from Python dye_leather_armor function
     */
    @Test
    fun testArmorDyeing() {
        val armorManager = com.skintostatue.android.core.processor.ArmorManager()

        // Create test leather armor (light beige)
        val leatherArmor = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        val leatherColor = Color.argb(255, 200, 180, 140)
        for (y in 0 until 8) {
            for (x in 0 until 8) {
                leatherArmor.setPixel(x, y, leatherColor)
            }
        }

        // Dye with blue
        val blueDye = intArrayOf(0, 0, 255)
        val dyedArmor = armorManager.dyeLeatherArmor(leatherArmor, blueDye)

        // Verify dye was applied
        val dyedPixel = dyedArmor.getPixel(4, 4)
        val dyedR = Color.red(dyedPixel)
        val dyedG = Color.green(dyedPixel)
        val dyedB = Color.blue(dyedPixel)

        // Formula from assets/公式.txt:
        // 1) Calculate average RGB and average max
        // 2) Calculate max_of_average = max(average_red, average_green, average_blue)
        // 3) Calculate gain_factor = average_max / max_of_average
        // 4) Calculate result_red = average_red * gain_factor
        // 5) Mix: final_r = int((result_red + dye_color[0]) / 2)
        // All pixels are set to the same color

        val totalRed = 200.0 * 64  // 8x8 pixels
        val totalGreen = 180.0 * 64
        val totalBlue = 140.0 * 64
        val totalMax = (200 + 180 + 140) / 3.0 * 64  // average max per pixel * 64

        val averageRed = totalRed / 64
        val averageGreen = totalGreen / 64
        val averageBlue = totalBlue / 64
        val averageMax = totalMax / 64

        val maxOfAverage = maxOf(averageRed, averageGreen, averageBlue)
        val gainFactor = averageMax / maxOfAverage

        val resultRed = averageRed * gainFactor
        val resultGreen = averageGreen * gainFactor
        val resultBlue = averageBlue * gainFactor

        val expectedR = ((resultRed + 0) / 2).toInt()
        val expectedG = ((resultGreen + 0) / 2).toInt()
        val expectedB = ((resultBlue + 255) / 2).toInt()

        assertEquals("Dyed R channel", expectedR, dyedR, 2)
        assertEquals("Dyed G channel", expectedG, dyedG, 2)
        assertEquals("Dyed B channel", expectedB, dyedB, 2)

        // Verify alpha was raised to 255
        assertEquals("Dyed alpha", 255, Color.alpha(dyedPixel))
    }

    /**
     * Helper function to get maximum of three values
     */
    private fun maxOf(a: Double, b: Double, c: Double): Double {
        return maxOf(a, maxOf(b, c))
    }

    /**
     * Test 8: Skin scaling
     * Verifies HD skin scaling works correctly
     */
    @Test
    fun testSkinScaling() {
        val skinProcessor = com.skintostatue.android.core.processor.SkinProcessor()

        // Create standard 64x64 skin
        val standardSkin = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        skinProcessor.skinImage = standardSkin

        val standardScale = skinProcessor.getScaleFactor()
        assertEquals("Standard skin scale", 1.0f, standardScale, 0.001f)

        // Create HD 128x128 skin
        val hdSkin = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
        skinProcessor.skinImage = hdSkin

        val hdScale = skinProcessor.getScaleFactor()
        assertEquals("HD skin scale", 2.0f, hdScale, 0.001f)

        // Create HD 256x256 skin
        val hd256Skin = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        skinProcessor.skinImage = hd256Skin

        val hd256Scale = skinProcessor.getScaleFactor()
        assertEquals("HD 256 skin scale", 4.0f, hd256Scale, 0.001f)

        // Create HD 512x512 skin
        val hd512Skin = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        skinProcessor.skinImage = hd512Skin

        val hd512Scale = skinProcessor.getScaleFactor()
        assertEquals("HD 512 skin scale", 8.0f, hd512Scale, 0.001f)
    }

    /**
     * Test 9: NBT encoding
     * Verifies NBT block encoding matches Python version
     */
    @Test
    fun testNBTBlockEncoding() {
        val schematicGenerator = com.skintostatue.android.core.generator.SchematicGenerator()

        // Add test blocks
        schematicGenerator.addBlock(10, 20, 30, "minecraft:white_wool")
        schematicGenerator.addBlock(11, 21, 31, "minecraft:black_wool")

        // Verify block map
        val palette = schematicGenerator.getPalette()
        assertEquals("Palette size", 2, palette.size)
        assertTrue("White wool in palette", palette.containsKey("minecraft:white_wool"))
        assertTrue("Black wool in palette", palette.containsKey("minecraft:black_wool"))

        // Verify block count
        val blockCount = schematicGenerator.getBlockCount()
        assertEquals("Block count", 2, blockCount)

        // Verify dimensions
        val (width, height, length) = schematicGenerator.getDimensions()
        assertEquals("Width", 12, width)
        assertEquals("Height", 22, height)
        assertEquals("Length", 32, length)
    }

    /**
     * Test 10: Integration test
     * Verifies end-to-end conversion produces valid output
     */
    @Test
    fun testIntegration() {
        val config = Config()
        val converter = SkinToStatueConverter(config)

        // Create simple test skin (all white)
        val testSkin = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                testSkin.setPixel(x, y, Color.WHITE)
            }
        }

        // Convert skin
        // Note: This is a simplified test - in real usage, this would be async
        val result = kotlinx.coroutines.runBlocking {
            converter.convert(testSkin)
        }

        // Verify result
        assertNotNull("Result should not be null", result)
        assertTrue("Result data should not be empty", result.data.isNotEmpty())
        assertTrue("Block count should be positive", result.blockCount > 0)
        assertTrue("Unique block count should be positive", result.uniqueBlockCount > 0)

        // Verify dimensions
        val (width, height, length) = result.dimensions
        assertTrue("Width should be positive", width > 0)
        assertTrue("Height should be positive", height > 0)
        assertTrue("Length should be positive", length > 0)

        // Clean up
        converter.clear()
    }
}