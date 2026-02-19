package com.skintostatue.android.core

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Color matching algorithms
 * Ported from Python color_modes.py
 * Includes ALL color difference algorithms and color space conversions
 */
class ColorMatcher {
    
    /**
     * RGB color difference
     * Ported from Python RGBDiff class
     */
    class RGBDiff : ColorDiffable {
        override fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double {
            val r1 = color1[0]
            val g1 = color1[1]
            val b1 = color1[2]
            val a1 = color1[3]

            val r2 = color2[0]
            val g2 = color2[1]
            val b2 = color2[2]
            val a2 = color2[3]

            return (r1 - r2).toDouble().pow(2) * weight1 +
                   (g1 - g2).toDouble().pow(2) * weight2 +
                   (b1 - b2).toDouble().pow(2) * weight3
        }
    }

    /**
     * Absolute RGB color difference
     * Ported from Python AbsRGBDiff class
     */
    class AbsRGBDiff : ColorDiffable {
        override fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double {
            val r1 = color1[0]
            val g1 = color1[1]
            val b1 = color1[2]
            val a1 = color1[3]

            val r2 = color2[0]
            val g2 = color2[1]
            val b2 = color2[2]
            val a2 = color2[3]

            return abs(r1 - r2) * weight1 +
                   abs(g1 - g2) * weight2 +
                   abs(b1 - b2) * weight3
        }
    }

    /**
     * HSB color difference
     * Ported from Python HSBDiff class
     */
    class HSBDiff : ColorDiffable {
        override fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double {
            val r1 = color1[0]
            val g1 = color1[1]
            val b1 = color1[2]
            val a1 = color1[3]

            val r2 = color2[0]
            val g2 = color2[1]
            val b2 = color2[2]
            val a2 = color2[3]

            val (h1, s1, brightness1) = rgbToHsb(r1, g1, b1)
            val (h2, s2, brightness2) = rgbToHsb(r2, g2, b2)

            return ((h1 - h2) * 1000).pow(2) * weight1 +
                   ((s1 - s2) * 1000).pow(2) * weight2 +
                   ((brightness1 - brightness2) * 1000).pow(2) * weight3
        }
    }

    /**
     * HSL color difference
     * Ported from Python HSLDiff class
     */
    class HSLDiff : ColorDiffable {
        override fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double {
            val r1 = color1[0]
            val g1 = color1[1]
            val b1 = color1[2]
            val a1 = color1[3]

            val r2 = color2[0]
            val g2 = color2[1]
            val b2 = color2[2]
            val a2 = color2[3]

            val (h1, s1, l1) = rgbToHsl(r1, g1, b1)
            val (h2, s2, l2) = rgbToHsl(r2, g2, b2)

            return ((h1 - h2) * 1000).pow(2) * weight1 +
                   ((s1 - s2) * 1000).pow(2) * weight2 +
                   ((l1 - l2) * 1000).pow(2) * weight3
        }
    }

    /**
     * LAB color difference
     * Ported from Python LABDiff class
     * Uses exact same XYZ and LAB conversion coefficients as Python version
     */
    class LABDiff : ColorDiffable {
        companion object {
            // Reference white point (D65)
            private const val XN = 95.047
            private const val YN = 100.000
            private const val ZN = 108.883
        }

        override fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double {
            val r1 = color1[0]
            val g1 = color1[1]
            val b1 = color1[2]
            val a1 = color1[3]

            val r2 = color2[0]
            val g2 = color2[1]
            val b2 = color2[2]
            val a2 = color2[3]

            val (l1, aL1, bL1) = rgbToLab(r1, g1, b1)
            val (l2, aL2, bL2) = rgbToLab(r2, g2, b2)

            return ((l1 - l2) * 1000).pow(2) * weight1 +
                   ((aL1 - aL2) * 1000).pow(2) * weight2 +
                   ((bL1 - bL2) * 1000).pow(2) * weight3
        }

        private fun rgbToLab(r: Int, g: Int, b: Int): Triple<Double, Double, Double> {
            val xyz = rgbToXyz(r, g, b)
            return xyzToLab(xyz.first, xyz.second, xyz.third)
        }

        private fun rgbToXyz(r: Int, g: Int, b: Int): Triple<Double, Double, Double> {
            val rf = r / 255.0
            val gf = g / 255.0
            val bf = b / 255.0

            // Exact same coefficients as Python version
            val x = rf * 0.4124564 + gf * 0.3575761 + bf * 0.1804375
            val y = rf * 0.2126729 + gf * 0.7151522 + bf * 0.0721750
            val z = rf * 0.0193339 + gf * 0.1191920 + bf * 0.9503041

            return Triple(x * 100, y * 100, z * 100)
        }

        private fun xyzToLab(x: Double, y: Double, z: Double): Triple<Double, Double, Double> {
            val xNorm = x / XN
            val yNorm = y / YN
            val zNorm = z / ZN

            val fx = f(xNorm)
            val fy = f(yNorm)
            val fz = f(zNorm)

            val l = 116 * fy - 16
            val a = 500 * (fx - fy)
            val b = 200 * (fy - fz)

            return Triple(l, a, b)
        }

        private fun f(t: Double): Double {
            val deltaPow = (6.0 / 29.0).pow(3)
            return if (t > deltaPow) {
                t.pow(1.0 / 3.0)
            } else {
                (1.0 / 3.0) * (29.0 / 6.0).pow(2) * t + 4.0 / 29.0
            }
        }
    }

    /**
     * Color difference interface
     */
    interface ColorDiffable {
        fun getDelta(
            color1: IntArray,
            color2: IntArray,
            weight1: Float = 1.0f,
            weight2: Float = 1.0f,
            weight3: Float = 1.0f
        ): Double
    }

    companion object {
        /**
         * RGB to HSL conversion
         * Ported from Python _rgb_to_hsl function
         */
        fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Float, Float, Float> {
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
         * RGB to HSB conversion
         * Ported from Python _rgb_to_hsb function
         */
        fun rgbToHsb(r: Int, g: Int, b: Int): Triple<Float, Float, Float> {
            val rf = r / 255.0f
            val gf = g / 255.0f
            val bf = b / 255.0f

            val maxVal = maxOf(rf, gf, bf)
            val minVal = minOf(rf, gf, bf)
            val delta = maxVal - minVal

            val brightness = maxVal
            val saturation = if (maxVal == 0f) 0f else delta / maxVal

            var hue = 0.0f
            if (delta != 0.0f) {
                when (maxVal) {
                    rf -> hue = ((gf - bf) / delta + (if (gf < bf) 6f else 0f)) / 6f
                    gf -> hue = ((bf - rf) / delta + 2f) / 6f
                    bf -> hue = ((rf - gf) / delta + 4f) / 6f
                }
            }

            return Triple(hue, saturation, brightness)
        }

        /**
         * LAB to RGB conversion
         * Ported from Python _lab_to_rgb function
         * Inverse of RGB to LAB conversion
         */
        fun labToRgb(l: Double, a: Double, b: Double): Triple<Float, Float, Float> {
            // LAB to XYZ
            val fy = (l + 16) / 116.0
            val fx = (a / 500.0) + fy
            val fz = fy - (b / 200.0)

            val x = xFromF(fx) * XN
            val y = yFromF(fy) * YN
            val z = zFromF(fz) * ZN

            // XYZ to RGB
            val rf = x * 3.2404542 - y * 1.5371385 - z * 0.4985314
            val gf = -x * 0.9692660 + y * 1.8760108 + z * 0.0415560
            val bf = x * 0.0556434 - y * 0.2040259 + z * 1.0572252

            return Triple(
                (rf * 255).toFloat(),
                (gf * 255).toFloat(),
                (bf * 255).toFloat()
            )
        }

        private fun xFromF(fx: Double): Double {
            val fx3 = fx.pow(3)
            return if (fx3 > 0.008856) fx3 else (fx - 16.0 / 116.0) / 7.787
        }

        private fun yFromF(fy: Double): Double {
            val fy3 = fy.pow(3)
            return if (fy3 > 0.008856) fy3 else (fy - 16.0 / 116.0) / 7.787
        }

        private fun zFromF(fz: Double): Double {
            val fz3 = fz.pow(3)
            return if (fz3 > 0.008856) fz3 else (fz - 16.0 / 116.0) / 7.787
        }
    }
}