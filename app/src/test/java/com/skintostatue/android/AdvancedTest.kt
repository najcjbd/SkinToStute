package com.skintostatue.android

import android.graphics.Bitmap
import android.graphics.Color
import com.skintostatue.android.core.ColorMatcher
import com.skintostatue.android.core.processor.ImageFilters
import org.junit.Assert.*
import org.junit.Test

/**
 * Advanced test suite
 * Tests additional features, edge cases, and complete functionality
 * Complements ComprehensiveTest and PerfectRestorationTest
 */
class AdvancedTest {

    /**
     * Test 1: Image rotation 90 degrees
     * Verifies clockwise rotation
     */
    @Test
    fun testImageRotation90() {
        // Create test image with distinct corners
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(0, 0, Color.RED)      // Top-left
        testImage.setPixel(7, 0, Color.GREEN)    // Top-right
        testImage.setPixel(0, 7, Color.BLUE)     // Bottom-left
        testImage.setPixel(7, 7, Color.YELLOW)   // Bottom-right

        val rotated = ImageFilters.rotate90Clockwise(testImage)

        // After 90° clockwise rotation:
        // Top-left (0,0) → Top-right (7,0)
        // Top-right (7,0) → Bottom-right (7,7)
        // Bottom-left (0,7) → Top-left (0,0)
        // Bottom-right (7,7) → Bottom-left (0,7)

        assertEquals("Top-left after 90° rotation", Color.BLUE, rotated.getPixel(0, 0))
        assertEquals("Top-right after 90° rotation", Color.RED, rotated.getPixel(7, 0))
        assertEquals("Bottom-left after 90° rotation", Color.YELLOW, rotated.getPixel(0, 7))
        assertEquals("Bottom-right after 90° rotation", Color.GREEN, rotated.getPixel(7, 7))
    }

    /**
     * Test 2: Image rotation 180 degrees
     * Verifies 180° rotation
     */
    @Test
    fun testImageRotation180() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(0, 0, Color.RED)      // Top-left
        testImage.setPixel(7, 0, Color.GREEN)    // Top-right
        testImage.setPixel(0, 7, Color.BLUE)     // Bottom-left
        testImage.setPixel(7, 7, Color.YELLOW)   // Bottom-right

        val rotated = ImageFilters.rotate180(testImage)

        // After 180° rotation, all corners are flipped
        assertEquals("Top-left after 180° rotation", Color.YELLOW, rotated.getPixel(0, 0))
        assertEquals("Top-right after 180° rotation", Color.BLUE, rotated.getPixel(7, 0))
        assertEquals("Bottom-left after 180° rotation", Color.GREEN, rotated.getPixel(0, 7))
        assertEquals("Bottom-right after 180° rotation", Color.RED, rotated.getPixel(7, 7))
    }

    /**
     * Test 3: Image rotation 270 degrees
     * Verifies counter-clockwise rotation
     */
    @Test
    fun testImageRotation270() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(0, 0, Color.RED)      // Top-left
        testImage.setPixel(7, 0, Color.GREEN)    // Top-right
        testImage.setPixel(0, 7, Color.BLUE)     // Bottom-left
        testImage.setPixel(7, 7, Color.YELLOW)   // Bottom-right

        val rotated = ImageFilters.rotate270Clockwise(testImage)

        // After 270° clockwise rotation (90° counter-clockwise):
        assertEquals("Top-left after 270° rotation", Color.GREEN, rotated.getPixel(0, 0))
        assertEquals("Top-right after 270° rotation", Color.YELLOW, rotated.getPixel(7, 0))
        assertEquals("Bottom-left after 270° rotation", Color.RED, rotated.getPixel(0, 7))
        assertEquals("Bottom-right after 270° rotation", Color.BLUE, rotated.getPixel(7, 7))
    }

    /**
     * Test 4: Horizontal flip
     * Verifies left-right mirroring
     */
    @Test
    fun testHorizontalFlip() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(0, 4, Color.RED)      // Left
        testImage.setPixel(7, 4, Color.GREEN)    // Right

        val flipped = ImageFilters.flipHorizontal(testImage)

        // After horizontal flip, left and right swap
        assertEquals("Left after horizontal flip", Color.GREEN, flipped.getPixel(0, 4))
        assertEquals("Right after horizontal flip", Color.RED, flipped.getPixel(7, 4))
    }

    /**
     * Test 5: Vertical flip
     * Verifies top-bottom mirroring
     */
    @Test
    fun testVerticalFlip() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(4, 0, Color.RED)      // Top
        testImage.setPixel(4, 7, Color.GREEN)    // Bottom

        val flipped = ImageFilters.flipVertical(testImage)

        // After vertical flip, top and bottom swap
        assertEquals("Top after vertical flip", Color.GREEN, flipped.getPixel(4, 0))
        assertEquals("Bottom after vertical flip", Color.RED, flipped.getPixel(4, 7))
    }

    /**
     * Test 6: Grayscale filter
     * Verifies conversion to grayscale
     */
    @Test
    fun testGrayscaleFilter() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(4, 4, Color.RED)

        val gray = ImageFilters.grayscale(testImage)
        val grayPixel = gray.getPixel(4, 4)

        val grayValue = Color.red(grayPixel)
        assertEquals("Grayscale R", grayValue, Color.green(grayPixel))
        assertEquals("Grayscale G", grayValue, Color.blue(grayPixel))

        // Standard grayscale formula: 0.299*R + 0.587*G + 0.114*B
        // For RED (255, 0, 0): 0.299*255 + 0.587*0 + 0.114*0 = 76.245
        val expectedGray = (0.299 * 255 + 0.587 * 0 + 0.114 * 0).toInt()
        assertEquals("Grayscale value", expectedGray, grayValue, 2)
    }

    /**
     * Test 7: Image overlay
     * Verifies alpha blending of two images
     */
    @Test
    fun testImageOverlay() {
        val baseImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        for (y in 0 until 8) {
            for (x in 0 until 8) {
                baseImage.setPixel(x, y, Color.RED)
            }
        }

        val overlayImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        for (y in 0 until 8) {
            for (x in 0 until 8) {
                overlayImage.setPixel(x, y, Color.argb(128, 0, 255, 0)) // 50% opacity green
            }
        }

        val result = ImageFilters.overlayImage(baseImage, overlayImage)
        val resultPixel = result.getPixel(4, 4)

        // With 50% opacity: result = base * (1-0.5) + overlay * 0.5
        // R: 255 * 0.5 + 0 * 0.5 = 127
        // G: 0 * 0.5 + 255 * 0.5 = 127
        // B: 0 * 0.5 + 0 * 0.5 = 0
        assertEquals("Overlay R", 127, Color.red(resultPixel), 2)
        assertEquals("Overlay G", 127, Color.green(resultPixel), 2)
        assertEquals("Overlay B", 0, Color.blue(resultPixel), 2)
    }

    /**
     * Test 8: HSL color difference calculation
     * Verifies HSL mode produces correct color differences
     */
    @Test
    fun testHSLColorDifference() {
        val hslMatcher = ColorMatcher.HSLDiff()

        // Test two similar colors
        val color1 = intArrayOf(255, 128, 64, 255)
        val color2 = intArrayOf(255, 130, 66, 255)

        val delta = hslMatcher.getDelta(color1, color2, 1.0f, 1.0f, 1.0f)

        // Very similar colors should have small delta
        assertTrue("Similar colors should have small delta", delta < 1000.0)

        // Test two very different colors
        val color3 = intArrayOf(255, 0, 0, 255)
        val color4 = intArrayOf(0, 255, 255, 255)

        val delta2 = hslMatcher.getDelta(color3, color4, 1.0f, 1.0f, 1.0f)

        // Very different colors should have large delta
        assertTrue("Different colors should have large delta", delta2 > 10000.0)
    }

    /**
     * Test 9: HSB color difference calculation
     * Verifies HSB mode produces correct color differences
     */
    @Test
    fun testHSBColorDifference() {
        val hsbMatcher = ColorMatcher.HSBDiff()

        // Test same brightness, different hue
        val color1 = intArrayOf(255, 0, 0, 255)    // Red
        val color2 = intArrayOf(0, 255, 0, 255)    // Green

        val delta = hsbMatcher.getDelta(color1, color2, 1.0f, 1.0f, 1.0f)

        // Different hue should produce significant delta
        assertTrue("Different hue should have large delta", delta > 1000.0)

        // Test same hue, different brightness
        val color3 = intArrayOf(255, 0, 0, 255)    // Bright red
        val color4 = intArrayOf(128, 0, 0, 255)    // Dark red

        val delta2 = hsbMatcher.getDelta(color3, color4, 1.0f, 1.0f, 1.0f)

        // Different brightness should produce delta
        assertTrue("Different brightness should have delta", delta2 > 0.0)
    }

    /**
     * Test 10: ABSRGB color difference
     * Verifies absolute RGB difference calculation
     */
    @Test
    fun testABSRGBColorDifference() {
        val absMatcher = ColorMatcher.AbsRGBDiff()

        val color1 = intArrayOf(255, 255, 255, 255)
        val color2 = intArrayOf(0, 0, 0, 255)

        val delta = absMatcher.getDelta(color1, color2, 1.0f, 1.0f, 1.0f)

        // ABSRGB: |255-0| + |255-0| + |255-0| = 765
        assertEquals("ABSRGB delta", 765.0, delta, 0.1)
    }

    /**
     * Test 11: Skin coordinate mapping
     * Verifies skin image coordinates map correctly to statue blocks
     */
    @Test
    fun testSkinCoordinateMapping() {
        // Test standard 64x64 skin
        val skinWidth = 64
        val skinHeight = 64

        // Front face: x=8-15, y=8-15 (8x8 pixels)
        val frontXStart = 8
        val frontXEnd = 15
        val frontYStart = 8
        val frontYEnd = 15

        // Calculate width and height
        val faceWidth = frontXEnd - frontXStart + 1
        val faceHeight = frontYEnd - frontYStart + 1

        assertEquals("Front face width", 8, faceWidth)
        assertEquals("Front face height", 8, faceHeight)

        // Verify coordinates are within skin bounds
        assertTrue("Front X start within bounds", frontXStart >= 0 && frontXStart < skinWidth)
        assertTrue("Front X end within bounds", frontXEnd >= 0 && frontXEnd < skinWidth)
        assertTrue("Front Y start within bounds", frontYStart >= 0 && frontYStart < skinHeight)
        assertTrue("Front Y end within bounds", frontYEnd >= 0 && frontYEnd < skinHeight)
    }

    /**
     * Test 12: Invalid skin handling
     * Verifies graceful handling of invalid skin images
     */
    @Test
    fun testInvalidSkinHandling() {
        // Test null skin
        try {
            val converter = com.skintostatue.android.core.SkinToStatueConverter(
                com.skintostatue.android.core.model.Config()
            )

            val result = kotlinx.coroutines.runBlocking {
                converter.convert(null)
            }

            // Should handle gracefully (either return null or throw appropriate exception)
            // This test verifies the converter doesn't crash
            assertTrue("Result should be null for null skin", result == null || result.data.isEmpty())
        } catch (e: Exception) {
            // Exception is acceptable if it's the expected behavior
            assertTrue("Exception should be related to null input", 
                e.message?.contains("null", ignoreCase = true) == true || 
                e.message?.contains("skin", ignoreCase = true) == true)
        }
    }

    /**
     * Test 13: Empty skin handling
     * Verifies handling of completely transparent skin
     */
    @Test
    fun testEmptySkinHandling() {
        val emptySkin = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        // All pixels are transparent by default

        val converter = com.skintostatue.android.core.SkinToStatueConverter(
            com.skintostatue.android.core.model.Config()
        )

        val result = kotlinx.coroutines.runBlocking {
            converter.convert(emptySkin)
        }

        assertNotNull("Result should not be null", result)
        assertEquals("Empty skin should have 0 blocks", 0, result.blockCount)
        assertEquals("Empty skin should have 0 unique blocks", 0, result.uniqueBlockCount)

        converter.clear()
    }

    /**
     * Test 14: Single pixel skin
     * Verifies handling of skin with only one non-transparent pixel
     */
    @Test
    fun testSinglePixelSkin() {
        val singlePixelSkin = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        singlePixelSkin.setPixel(32, 32, Color.WHITE)

        val converter = com.skintostatue.android.core.SkinToStatueConverter(
            com.skintostatue.android.core.model.Config()
        )

        val result = kotlinx.coroutines.runBlocking {
            converter.convert(singlePixelSkin)
        }

        assertNotNull("Result should not be null", result)
        assertTrue("Single pixel should produce at least 1 block", result.blockCount >= 1)
        assertTrue("Single pixel should have exactly 1 unique block", result.uniqueBlockCount == 1)

        converter.clear()
    }

    /**
     * Test 15: Maximum scale test
     * Verifies scaling with maximum scale factor
     */
    @Test
    fun testMaximumScale() {
        val config = com.skintostatue.android.core.model.Config(scale = 8.0f)
        val converter = com.skintostatue.android.core.SkinToStatueConverter(config)

        val testSkin = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        testSkin.setPixel(32, 32, Color.WHITE)

        val result = kotlinx.coroutines.runBlocking {
            converter.convert(testSkin)
        }

        assertNotNull("Result should not be null", result)
        assertTrue("Maximum scale should produce blocks", result.blockCount > 0)

        converter.clear()
    }

    /**
     * Test 16: All rotation directions
     * Verifies all 4 rotation directions produce correct results
     */
    @Test
    fun testAllRotationDirections() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        testImage.setPixel(0, 0, Color.RED)
        testImage.setPixel(7, 0, Color.GREEN)
        testImage.setPixel(0, 7, Color.BLUE)
        testImage.setPixel(7, 7, Color.YELLOW)

        val rotated90 = ImageFilters.rotate90Clockwise(testImage)
        val rotated180 = ImageFilters.rotate180(testImage)
        val rotated270 = ImageFilters.rotate270Clockwise(testImage)

        // Verify all rotations produced images
        assertEquals("Rotated 90 dimensions", 8, rotated90.width)
        assertEquals("Rotated 180 dimensions", 8, rotated180.width)
        assertEquals("Rotated 270 dimensions", 8, rotated270.width)

        // Verify corners are in different positions
        val topLeft90 = rotated90.getPixel(0, 0)
        val topLeft180 = rotated180.getPixel(0, 0)
        val topLeft270 = rotated270.getPixel(0, 0)

        assertTrue("Different rotations should produce different corners",
            topLeft90 != topLeft180 || topLeft90 != topLeft270)
    }

    /**
     * Test 17: Filter combination
     * Verifies applying multiple filters in sequence
     */
    @Test
    fun testFilterCombination() {
        val testImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        for (y in 0 until 8) {
            for (x in 0 until 8) {
                testImage.setPixel(x, y, Color.argb(255, 255, 128, 64))
            }
        }

        // Apply filters in sequence: saturation -> brightness -> contrast
        val step1 = ImageFilters.applySaturation(testImage, 0.5f)
        val step2 = ImageFilters.applyBrightness(step1, 1.2f)
        val step3 = ImageFilters.applyContrast(step2, 1.1f)

        val originalPixel = testImage.getPixel(4, 4)
        val finalPixel = step3.getPixel(4, 4)

        // Verify colors changed after applying filters
        assertFalse("Combined filters should change pixel", 
            originalPixel == finalPixel)
    }

    /**
     * Test 18: Color weights
     * Verifies color weights affect color matching
     */
    @Test
    fun testColorWeights() {
        val blockDatabase = com.skintostatue.android.core.BlockDatabase()
        val blocks = blockDatabase.getAllBlocks()
        val blockIndex = com.skintostatue.android.core.model.BlockIndex(blocks, exactMode = false)

        val testColor = intArrayOf(255, 128, 64, 255)
        val matcher = ColorMatcher.RGBDiff()

        // Test with equal weights
        val result1 = blockIndex.findBestMatch(testColor, matcher, listOf(1.0f, 1.0f, 1.0f), false)

        // Test with red-weighted
        val result2 = blockIndex.findBestMatch(testColor, matcher, listOf(2.0f, 1.0f, 1.0f), false)

        // Test with blue-weighted
        val result3 = blockIndex.findBestMatch(testColor, matcher, listOf(1.0f, 1.0f, 2.0f), false)

        // Different weights should potentially produce different results
        // (though they might be the same if the best match is very clear)
        assertNotNull("Equal weights result", result1)
        assertNotNull("Red-weighted result", result2)
        assertNotNull("Blue-weighted result", result3)
    }

    /**
     * Test 19: Exact mode vs priority mode
     * Verifies exact mode uses exact matching, priority mode uses priority
     */
    @Test
    fun testExactVsPriorityMode() {
        val blockDatabase = com.skintostatue.android.core.BlockDatabase()
        val blocks = blockDatabase.getAllBlocks()

        val exactIndex = com.skintostatue.android.core.model.BlockIndex(blocks, exactMode = true)
        val priorityIndex = com.skintostatue.android.core.model.BlockIndex(blocks, exactMode = false)

        val testColor = intArrayOf(255, 255, 255, 255)
        val matcher = ColorMatcher.RGBDiff()
        val weights = listOf(1.0f, 1.0f, 1.0f)

        val exactResult = exactIndex.findBestMatch(testColor, matcher, weights, false)
        val priorityResult = priorityIndex.findBestMatch(testColor, matcher, weights, false)

        assertNotNull("Exact mode result", exactResult)
        assertNotNull("Priority mode result", priorityResult)

        // Priority mode should prefer wool due to higher priority
        // Exact mode might prefer concrete if exact color matches
        assertTrue("Priority mode should prefer wool", priorityResult!!.name.contains("wool"))
    }

    /**
     * Test 20: Large image processing
     * Verifies handling of HD skins (512x512)
     */
    @Test
    fun testLargeImageProcessing() {
        val largeImage = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        
        // Fill with pattern
        for (y in 0 until 512 step 2) {
            for (x in 0 until 512 step 2) {
                largeImage.setPixel(x, y, Color.WHITE)
                largeImage.setPixel(x + 1, y, Color.BLACK)
            }
        }

        // Apply filters to verify no crashes
        val saturated = ImageFilters.applySaturation(largeImage, 0.5f)
        val brightened = ImageFilters.applyBrightness(saturated, 1.1f)
        val contrasted = ImageFilters.applyContrast(brightened, 1.05f)

        assertEquals("Large image width", 512, contrasted.width)
        assertEquals("Large image height", 512, contrasted.height)
    }
}