package com.skintostatue.android.core.processor

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Image processing filters
 * Ported from Python filters.py
 * Supports hue, saturation, brightness, contrast, and posterize filters
 * Includes ALL methods and algorithms from Python version
 */
class ImageFilters {
    companion object {
        /**
         * Apply hue rotation
         * Ported from Python apply_hue function
         * Uses exact same HSL conversion algorithm as Python version
         */
        fun applyHue(image: Bitmap, hueShift: Float): Bitmap {
            if (hueShift == 0.0f) {
                return image
            }

            val result = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val pixels = IntArray(image.width * image.height)
            image.getPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

            for (i in pixels.indices) {
                val pixel = pixels[i]
                val a = Color.alpha(pixel)

                if (a > 0) {
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)

                    val (h, s, l) = rgbToHsl(r, g, b)
                    val newH = (h + hueShift) % 1.0f
                    val (newR, newG, newB) = hslToRgb(newH, s, l)

                    pixels[i] = Color.argb(a, newR.toInt(), newG.toInt(), newB.toInt())
                }
            }

            result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
            return result
        }

        /**
         * Apply saturation
         * Ported from Python apply_saturation function
         * Uses ImageEnhance.Color algorithm (linear interpolation with grayscale)
         */
        fun applySaturation(image: Bitmap, saturation: Float): Bitmap {
            if (saturation == 1.0f) {
                return image
            }

            // Create grayscale version (degenerate image)
            val grayscale = imageToGrayscale(image)

            // Blend original with grayscale
            // Formula: result = grayscale * (1 - saturation) + original * saturation
            return blendImages(grayscale, image, saturation)
        }

        /**
         * Apply brightness
         * Ported from Python apply_brightness function
         * Uses ImageEnhance.Brightness algorithm (linear interpolation with black)
         */
        fun applyBrightness(image: Bitmap, brightness: Float): Bitmap {
            if (brightness == 1.0f) {
                return image
            }

            // Create black image (degenerate image)
            val black = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            black.eraseColor(Color.BLACK)

            // Blend black with original
            // Formula: result = black * (1 - brightness) + original * brightness
            return blendImages(black, image, brightness)
        }

        /**
         * Apply contrast
         * Ported from Python apply_contrast function
         * Uses ImageEnhance.Contrast algorithm (linear interpolation with gray)
         */
        fun applyContrast(image: Bitmap, contrast: Float): Bitmap {
            if (contrast == 1.0f) {
                return image
            }

            // Calculate mean gray
            val pixels = IntArray(image.width * image.height)
            image.getPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

            var total = 0.0
            for (pixel in pixels) {
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
                total += gray
            }

            val meanGray = (total / pixels.size).toInt()

            // Create gray image (degenerate image)
            val grayImage = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            grayImage.eraseColor(Color.rgb(meanGray, meanGray, meanGray))

            // Blend gray with original
            // Formula: result = gray * (1 - contrast) + original * contrast
            return blendImages(grayImage, image, contrast)
        }

        /**
         * Apply posterize (color quantization)
         * Ported from Python apply_posterize function
         */
        fun applyPosterize(image: Bitmap, levels: Int): Bitmap {
            if (levels <= 0 || levels >= 256) {
                return image
            }

            val result = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val pixels = IntArray(image.width * image.height)
            image.getPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

            for (i in pixels.indices) {
                val pixel = pixels[i]
                val a = Color.alpha(pixel)

                if (a > 0) {
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)

                    val newR = posterizeChannel(r, levels)
                    val newG = posterizeChannel(g, levels)
                    val newB = posterizeChannel(b, levels)

                    pixels[i] = Color.argb(a, newR, newG, newB)
                }
            }

            result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
            return result
        }

        /**
         * Apply all filters
         * Ported from Python apply_filters function
         */
        fun applyFilters(
            image: Bitmap,
            hue: Float = 0.0f,
            saturation: Float = 1.0f,
            brightness: Float = 1.0f,
            contrast: Float = 1.0f,
            posterize: Int = 0
        ): Bitmap {
            var result = image

            if (hue != 0.0f) {
                result = applyHue(result, hue)
            }

            if (saturation != 1.0f) {
                result = applySaturation(result, saturation)
            }

            if (brightness != 1.0f) {
                result = applyBrightness(result, brightness)
            }

            if (contrast != 1.0f) {
                result = applyContrast(result, contrast)
            }

            if (posterize > 0) {
                result = applyPosterize(result, posterize)
            }

            return result
        }

        /**
         * Overlay image on base image
         * Ported from Python overlay_image function
         * Uses alpha compositing
         */
        fun overlayImage(baseImage: Bitmap, overlayImage: Bitmap?): Bitmap {
            if (overlayImage == null) {
                return baseImage
            }

            val base = baseImage.copy(Bitmap.Config.ARGB_8888)
            var overlay = overlayImage.copy(Bitmap.Config.ARGB_8888)

            if (base.width != overlay.width || base.height != overlay.height) {
                overlay = Bitmap.createScaledBitmap(overlay, base.width, base.height, true)
            }

            // Alpha composite
            val canvas = Canvas(base)
            val paint = Paint()
            canvas.drawBitmap(overlay, 0f, 0f, paint)

            return base
        }

        /**
         * Convert image to grayscale
         * Ported from Python grayscale function
         */
        fun grayscale(image: Bitmap): Bitmap {
            return imageToGrayscale(image)
        }

        /**
         * Flip image horizontally
         * Ported from Python flip_horizontal function
         */
        fun flipHorizontal(image: Bitmap): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.setScale(-1f, 1f, image.width / 2f, image.height / 2f)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        /**
         * Flip image vertically
         * Ported from Python flip_vertical function
         */
        fun flipVertical(image: Bitmap): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.setScale(1f, -1f, image.width / 2f, image.height / 2f)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        /**
         * Rotate image 90 degrees
         * Ported from Python rotate_90 function
         */
        fun rotate90(image: Bitmap): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.postRotate(90f, image.width / 2f, image.height / 2f)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        /**
         * Rotate image 180 degrees
         * Ported from Python rotate_180 function
         */
        fun rotate180(image: Bitmap): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.postRotate(180f, image.width / 2f, image.height / 2f)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        /**
         * Rotate image 270 degrees
         * Ported from Python rotate_270 function
         */
        fun rotate270(image: Bitmap): Bitmap {
            val matrix = android.graphics.Matrix()
            matrix.postRotate(270f, image.width / 2f, image.height / 2f)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        /**
         * RGB to HSL conversion
         * Ported from Python _rgb_to_hsl function
         * Uses exact same algorithm as Python version
         */
        private fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Float, Float, Float> {
            val rf = r / 255.0f
            val gf = g / 255.0f
            val bf = b / 255.0f

            val maxVal = maxOf(rf, gf, bf)
            val minVal = minOf(rf, gf, bf)
            val delta = maxVal - minVal

            val lightness = (maxVal + minVal) / 2.0f
            var saturation = 0.0f

            if (delta != 0.0f) {
                saturation = if (lightness > 0.5f) {
                    delta / (2.0f - maxVal - minVal)
                } else {
                    delta / (maxVal + minVal)
                }
            }

            var hue = 0.0f
            if (delta != 0.0f) {
                when (maxVal) {
                    rf -> hue = ((gf - bf) / delta + (if (gf < bf) 6f else 0f)) / 6f
                    gf -> hue = ((bf - rf) / delta + 2f) / 6f
                    bf -> hue = ((rf - gf) / delta + 4f) / 6f
                }
            }

            return Triple(hue, saturation, lightness)
        }

        /**
         * HSL to RGB conversion
         * Ported from Python _hsl_to_rgb function
         * Uses exact same algorithm as Python version
         */
        fun hslToRgb(h: Float, s: Float, l: Float): Triple<Float, Float, Float> {
            if (s == 0.0f) {
                return Triple(l * 255, l * 255, l * 255)
            }

            fun hueToRgb(p: Float, q: Float, t: Float): Float {
                var t = t
                if (t < 0) t += 1
                if (t > 1) t -= 1
                if (t < 1f / 6f) return p + (q - p) * 6 * t
                if (t < 1f / 2f) return q
                if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6
                return p
            }

            val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
            val p = 2 * l - q

            val r = hueToRgb(p, q, h + 1f / 3f)
            val g = hueToRgb(p, q, h)
            val b = hueToRgb(p, q, h - 1f / 3f)

            return Triple(r * 255, g * 255, b * 255)
        }

        /**
         * Posterize a single color channel
         * Ported from Python _posterize_channel function
         */
        private fun posterizeChannel(value: Int, levels: Int): Int {
            val step = 256.0f / levels
            return (value / step).toInt() * step
        }

        /**
         * Convert image to grayscale
         * Ported from Python grayscale function
         */
        private fun imageToGrayscale(image: Bitmap): Bitmap {
            val result = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val pixels = IntArray(image.width * image.height)
            image.getPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

            for (i in pixels.indices) {
                val pixel = pixels[i]
                val a = Color.alpha(pixel)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
                pixels[i] = Color.argb(a, gray, gray, gray)
            }

            result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
            return result
        }

        /**
         * Blend two images
         * Ported from Python Image.blend function
         * Formula: result = image1 * (1 - alpha) + image2 * alpha
         */
        private fun blendImages(image1: Bitmap, image2: Bitmap, alpha: Float): Bitmap {
            val result = Bitmap.createBitmap(image1.width, image1.height, Bitmap.Config.ARGB_8888)
            val pixels1 = IntArray(image1.width * image1.height)
            val pixels2 = IntArray(image2.width * image2.height)
            val resultPixels = IntArray(result.width * result.height)

            image1.getPixels(pixels1, 0, image1.width, 0, 0, image1.width, image1.height)
            image2.getPixels(pixels2, 0, image2.width, 0, 0, image2.width, image2.height)

            for (i in pixels1.indices) {
                val pixel1 = pixels1[i]
                val pixel2 = pixels2[i]

                val a1 = Color.alpha(pixel1)
                val r1 = Color.red(pixel1)
                val g1 = Color.green(pixel1)
                val b1 = Color.blue(pixel1)

                val a2 = Color.alpha(pixel2)
                val r2 = Color.red(pixel2)
                val g2 = Color.green(pixel2)
                val b2 = Color.blue(pixel2)

                // Blend formula: result = image1 * (1 - alpha) + image2 * alpha
                val newR = (r1 * (1 - alpha) + r2 * alpha).toInt()
                val newG = (g1 * (1 - alpha) + g2 * alpha).toInt()
                val newB = (b1 * (1 - alpha) + b2 * alpha).toInt()
                val newA = maxOf(a1, a2) // Use max alpha

                resultPixels[i] = Color.argb(newA, newR, newG, newB)
            }

            result.setPixels(resultPixels, 0, result.width, 0, 0, result.width, result.height)
            return result
        }
    }
}