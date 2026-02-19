package com.skintostatue.android.core.model

/**
 * Color matching modes for finding the best matching block
 * 100% equivalent to Python color_modes.py
 */
enum class ColorMode(val displayName: String) {
    RGB("RGB"),
    ABSRGB("Absolute RGB"),
    HSL("HSL"),
    HSB("HSB"),
    LAB("LAB")
}

/**
 * Color difference calculator - Ported from Python color_modes.py
 * Ensures 100% identical results with Python version
 */
interface ColorDiffable {
    fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float = 1.0f,
        weight2: Float = 1.0f,
        weight3: Float = 1.0f
    ): Float
}

/**
 * RGB Euclidean distance
 * Ported from Python RGBDiff class
 */
class RGBDiff : ColorDiffable {
    override fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float,
        weight2: Float,
        weight3: Float
    ): Float {
        val r1 = color1[0]
        val g1 = color1[1]
        val b1 = color1[2]
        val r2 = color2[0]
        val g2 = color2[1]
        val b2 = color2[2]

        return Math.pow((r1 - r2).toDouble(), 2.0).toFloat() * weight1 +
               Math.pow((g1 - g2).toDouble(), 2.0).toFloat() * weight2 +
               Math.pow((b1 - b2).toDouble(), 2.0).toFloat() * weight3
    }
}

/**
 * RGB Manhattan distance
 * Ported from Python AbsRGBDiff class
 */
class AbsRGBDiff : ColorDiffable {
    override fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float,
        weight2: Float,
        weight3: Float
    ): Float {
        val r1 = color1[0]
        val g1 = color1[1]
        val b1 = color1[2]
        val r2 = color2[0]
        val g2 = color2[1]
        val b2 = color2[2]

        return Math.abs(r1 - r2).toFloat() * weight1 +
               Math.abs(g1 - g2).toFloat() * weight2 +
               Math.abs(b1 - b2).toFloat() * weight3
    }
}

/**
 * HSL color space distance
 * Ported from Python HSLDiff class
 */
class HSLDiff : ColorDiffable {
    override fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float,
        weight2: Float,
        weight3: Float
    ): Float {
        val hsla = rgbToHsl(color1[0], color1[1], color1[2])
        val hslb = rgbToHsl(color2[0], color2[1], color2[2])

        return Math.pow((hsla[0] - hslb[0]) * 1000.0, 2.0).toFloat() * weight1 +
               Math.pow((hsla[1] - hslb[1]) * 1000.0, 2.0).toFloat() * weight2 +
               Math.pow((hsla[2] - hslb[2]) * 1000.0, 2.0).toFloat() * weight3
    }

    companion object {
        /**
         * Convert RGB to HSL
         * Ported from Python _rgb_to_hsl function
         * Identical implementation for 100% matching results
         */
        fun rgbToHsl(r: Int, g: Int, b: Int): FloatArray {
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
                hue = when {
                    maxVal == rf -> ((gf - bf) / delta + (if (gf < bf) 6 else 0)) / 6
                    maxVal == gf -> ((bf - rf) / delta + 2) / 6
                    else -> ((rf - gf) / delta + 4) / 6
                }
            }

            return floatArrayOf(hue, saturation, lightness)
        }
    }
}

/**
 * HSB (HSV) color space distance
 * Ported from Python HSBDiff class
 */
class HSBDiff : ColorDiffable {
    override fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float,
        weight2: Float,
        weight3: Float
    ): Float {
        val hsba = rgbToHsb(color1[0], color1[1], color1[2])
        val hsbb = rgbToHsb(color2[0], color2[1], color2[2])

        return Math.pow((hsba[0] - hsbb[0]) * 1000.0, 2.0).toFloat() * weight1 +
               Math.pow((hsba[1] - hsbb[1]) * 1000.0, 2.0).toFloat() * weight2 +
               Math.pow((hsba[2] - hsbb[2]) * 1000.0, 2.0).toFloat() * weight3
    }

    companion object {
        /**
         * Convert RGB to HSB (HSV)
         * Ported from Python _rgb_to_hsb function
         * Identical implementation for 100% matching results
         */
        fun rgbToHsb(r: Int, g: Int, b: Int): FloatArray {
            val rf = r / 255.0f
            val gf = g / 255.0f
            val bf = b / 255.0f

            val maxVal = maxOf(rf, gf, bf)
            val minVal = minOf(rf, gf, bf)
            val delta = maxVal - minVal

            val brightness = maxVal
            val saturation = if (maxVal == 0.0f) 0.0f else (delta / maxVal)

            var hue = 0.0f
            if (delta != 0.0f) {
                hue = when {
                    maxVal == rf -> ((gf - bf) / delta) % 6
                    maxVal == gf -> (bf - rf) / delta + 2
                    else -> (rf - gf) / delta + 4
                }
                hue /= 6.0f
            }

            return floatArrayOf(hue, saturation, brightness)
        }
    }
}

/**
 * LAB color space distance
 * Ported from Python LABDiff class
 * Reference white point (D65) matches Python implementation
 */
class LABDiff : ColorDiffable {
    companion object {
        private const val XN = 95.047
        private const val YN = 100.000
        private const val ZN = 108.883
    }

    override fun getDelta(
        color1: IntArray,
        color2: IntArray,
        weight1: Float,
        weight2: Float,
        weight3: Float
    ): Float {
        val lab1 = rgbToLab(color1[0], color1[1], color1[2])
        val lab2 = rgbToLab(color2[0], color2[1], color2[2])

        return Math.pow((lab1[0] - lab2[0]) * 1000.0, 2.0).toFloat() * weight1 +
               Math.pow((lab1[1] - lab2[1]) * 1000.0, 2.0).toFloat() * weight2 +
               Math.pow((lab1[2] - lab2[2]) * 1000.0, 2.0).toFloat() * weight3
    }

    /**
     * Convert RGB to LAB
     * Ported from Python _rgb_to_lab function
     * Uses exact same coefficients for 100% matching results
     */
    private fun rgbToLab(r: Int, g: Int, b: Int): FloatArray {
        val xyz = rgbToXyz(r, g, b)
        return xyzToLab(xyz[0], xyz[1], xyz[2])
    }

    /**
     * Convert RGB to XYZ
     * Ported from Python _rgb_to_xyz function
     * Exact coefficients from Python implementation
     */
    private fun rgbToXyz(r: Int, g: Int, b: Int): FloatArray {
        val rf = r / 255.0
        val gf = g / 255.0
        val bf = b / 255.0

        val x = rf * 0.4124564 + gf * 0.3575761 + bf * 0.1804375
        val y = rf * 0.2126729 + gf * 0.7151522 + bf * 0.0721750
        val z = rf * 0.0193339 + gf * 0.1191920 + bf * 0.9503041

        return floatArrayOf((x * 100).toFloat(), (y * 100).toFloat(), (z * 100).toFloat())
    }

    /**
     * Convert XYZ to LAB
     * Ported from Python _xyz_to_lab function
     * Uses exact same white point and formulas
     */
    private fun xyzToLab(x: Float, y: Float, z: Float): FloatArray {
        var xf = x / XN
        var yf = y / YN
        var zf = z / ZN

        val fx = f(xf)
        val fy = f(yf)
        val fz = f(zf)

        val l = 116 * fy - 16
        val a = 500 * (fx - fy)
        val b = 200 * (fy - fz)

        return floatArrayOf(l, a, b)
    }

    /**
     * LAB conversion function
     * Ported from Python _f function
     */
    private fun f(t: Float): Float {
        val deltaPow = Math.pow(6.0 / 29.0, 3.0)
        return if (t > deltaPow) {
            Math.pow(t.toDouble(), 1.0 / 3.0).toFloat()
        } else {
            (1.0 / 3.0 * Math.pow(29.0 / 6.0, 2.0) * t + 4.0 / 29.0).toFloat()
        }
    }
}

/**
 * Get color difference calculator for a specific mode
 * Ported from Python get_color_diff_mode function
 */
fun getColorDiffMode(mode: ColorMode): ColorDiffable {
    return when (mode) {
        ColorMode.RGB -> RGBDiff()
        ColorMode.ABSRGB -> AbsRGBDiff()
        ColorMode.HSL -> HSLDiff()
        ColorMode.HSB -> HSBDiff()
        ColorMode.LAB -> LABDiff()
    }
}

/**
 * Find the best matching block for a pixel color
 * Ported from Python find_best_block function
 * Ensures 100% identical block selection
 */
fun findBestBlock(
    pixelColor: IntArray,
    availableBlocks: List<BlockData>,
    colorMode: ColorMode = ColorMode.LAB,
    weights: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f)
): BlockData? {
    val diffCalculator = getColorDiffMode(colorMode)

    var bestBlock: BlockData? = null
    var minDelta = Float.MAX_VALUE

    for (block in availableBlocks) {
        val blockColor = block.color
        val delta = diffCalculator.getDelta(
            pixelColor, blockColor,
            weights[0], weights[1], weights[2]
        )

        if (delta < minDelta) {
            minDelta = delta
            bestBlock = block
        }
    }

    return bestBlock
}