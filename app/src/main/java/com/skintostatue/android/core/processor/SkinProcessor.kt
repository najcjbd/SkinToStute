package com.skintostatue.android.core.processor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.util.Base64

/**
 * Minecraft skin processor
 * Ported from Python skin_processor.py
 * Supports multiple skin formats: default, slim, and legacy
 * Includes skin loading from file or Mojang API
 */
class SkinProcessor {
    companion object {
        const val STANDARD_WIDTH = 64
        const val STANDARD_HEIGHT = 64
    }

    var skinImage: Bitmap? = null
        private set
    var skinFormat: SkinFormat = SkinFormat.DEFAULT
        private set

    /**
     * Load skin from file path
     * Ported from Python load_from_file function
     */
    fun loadFromFile(filePath: String): Bitmap {
        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(filePath, options)
                ?: throw IllegalArgumentException("Failed to decode skin file")

            // Convert to ARGB_8888 if needed
            val convertedBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888) {
                bitmap.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                bitmap
            }

            skinImage = convertedBitmap
            detectSkinFormat()
            convertedBitmap
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to load skin from file: ${e.message}", e)
        }
    }

    /**
     * Load skin from byte array
     * Ported from Python load_from_bytes function
     */
    fun loadFromBytes(imageData: ByteArray): Bitmap {
        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)
                ?: throw IllegalArgumentException("Failed to decode skin data")

            // Convert to ARGB_8888 if needed
            val convertedBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888) {
                bitmap.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                bitmap
            }

            skinImage = convertedBitmap
            detectSkinFormat()
            convertedBitmap
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to load skin from bytes: ${e.message}", e)
        }
    }

    /**
     * Load skin from Mojang API
     * Ported from Python load_from_mojang_api function
     */
    suspend fun loadFromMojangApi(username: String): Bitmap {
        val client = OkHttpClient()

        try {
            // Get UUID from username
            val uuidRequest = Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/$username")
                .build()

            val uuidResponse = client.newCall(uuidRequest).execute()
            if (!uuidResponse.isSuccessful) {
                throw IllegalArgumentException("Failed to get UUID for username: $username")
            }

            val uuidJson = Gson().fromJson(uuidResponse.body?.string(), JsonObject::class.java)
            val uuid = uuidJson.get("id").asString
                ?: throw IllegalArgumentException("No UUID found for username: $username")

            // Get profile with skin URL
            val profileRequest = Request.Builder()
                .url("https://sessionserver.mojang.com/session/minecraft/profile/$uuid")
                .build()

            val profileResponse = client.newCall(profileRequest).execute()
            if (!profileResponse.isSuccessful) {
                throw IllegalArgumentException("Failed to get profile for UUID: $uuid")
            }

            val profileJson = Gson().fromJson(profileResponse.body?.string(), JsonObject::class.java)
            val properties = profileJson.getAsJsonArray("properties")
                ?: throw IllegalArgumentException("No properties found in profile")

            var skinUrl: String? = null
            for (property in properties) {
                val propObj = property.asJsonObject
                if (propObj.get("name").asString == "textures") {
                    val value = propObj.get("value").asString
                    val decodedValue = String(Base64.getDecoder().decode(value))
                    val textureJson = Gson().fromJson(decodedValue, JsonObject::class.java)
                    val textures = textureJson.getAsJsonObject("textures")
                    val skin = textures.getAsJsonObject("SKIN")
                    skinUrl = skin.get("url").asString
                    break
                }
            }

            skinUrl ?: throw IllegalArgumentException("No skin found for username: $username")

            // Download skin
            val skinRequest = Request.Builder()
                .url(skinUrl)
                .build()

            val skinResponse = client.newCall(skinRequest).execute()
            if (!skinResponse.isSuccessful) {
                throw IllegalArgumentException("Failed to download skin from: $skinUrl")
            }

            val skinData = skinResponse.body?.bytes()
                ?: throw IllegalArgumentException("No data in skin response")

            return loadFromBytes(skinData)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to load skin from Mojang API: ${e.message}", e)
        }
    }

    /**
     * Detect skin format (default, slim, or legacy)
     * Ported from Python _detect_skin_format function
     */
    private fun detectSkinFormat() {
        val image = skinImage ?: return

        val width = image.width
        val height = image.height

        // Legacy format (64x32)
        if (height == 32) {
            skinFormat = SkinFormat.LEGACY
            convertLegacyToModern()
            return
        }

        // Check for slim format (3 pixel arm width at certain positions)
        if (width >= 50 && height >= 16) {
            val armPixels = mutableListOf<Int>()
            for (y in 16 until minOf(32, height)) {
                for (x in 40 until minOf(44, width)) {
                    if (x < width && y < height) {
                        val pixel = image.getPixel(x, y)
                        val alpha = Color.alpha(pixel)
                        if (alpha > 0) {
                            armPixels.add(x)
                        }
                    }
                }
            }

            if (armPixels.isNotEmpty()) {
                val armWidth = armPixels.max() - armPixels.min() + 1
                if (armWidth <= 3) {
                    skinFormat = SkinFormat.SLIM
                    return
                }
            }
        }

        skinFormat = SkinFormat.DEFAULT
    }

    /**
     * Convert legacy 64x32 skin to modern 64x64 format
     * Ported from Python _convert_legacy_to_modern function
     * Based on LegacyConverter.java from PlayerStatueBuilder
     */
    private fun convertLegacyToModern() {
        val image = skinImage ?: return

        val width = image.width
        val height = image.height

        // Only convert if it's actually 64x32
        if (width != 64 || height != 32) {
            return
        }

        // Create new 64x64 image
        val newImage = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newImage)

        // Copy old layout (body, head and right limbs)
        canvas.drawBitmap(image, 0f, 0f, null)

        // Right leg -> left leg
        // Convert coordinates from legacy to modern format
        canvas.drawBitmap(Bitmap.createBitmap(image, 24, 48, 4, 4), 4f, 16f, null) // Top
        canvas.drawBitmap(Bitmap.createBitmap(image, 28, 48, 4, 4), 8f, 16f, null) // Bottom
        canvas.drawBitmap(Bitmap.createBitmap(image, 20, 52, 4, 12), 8f, 20f, null) // Outside -> inside
        canvas.drawBitmap(Bitmap.createBitmap(image, 24, 52, 4, 12), 4f, 20f, null) // Front
        canvas.drawBitmap(Bitmap.createBitmap(image, 28, 52, 4, 12), 0f, 20f, null) // Inside -> outside
        canvas.drawBitmap(Bitmap.createBitmap(image, 32, 52, 4, 12), 12f, 20f, null) // Back

        // Right arm -> left arm
        // Use standard (non-slim) conversion
        canvas.drawBitmap(Bitmap.createBitmap(image, 40, 48, 4, 4), 44f, 16f, null) // Top
        canvas.drawBitmap(Bitmap.createBitmap(image, 44, 48, 4, 4), 48f, 16f, null) // Bottom
        canvas.drawBitmap(Bitmap.createBitmap(image, 48, 52, 4, 12), 52f, 20f, null) // Back
        canvas.drawBitmap(Bitmap.createBitmap(image, 40, 52, 4, 12), 44f, 20f, null) // Front
        canvas.drawBitmap(Bitmap.createBitmap(image, 44, 52, 4, 12), 40f, 20f, null) // Inside -> outside
        canvas.drawBitmap(Bitmap.createBitmap(image, 36, 52, 4, 12), 48f, 20f, null) // Outside -> inside

        // Update the skin image
        skinImage = newImage

        // Now check if it should be slim (after conversion)
        val armPixels = mutableListOf<Int>()
        for (y in 16 until 32) {
            for (x in 40 until 44) {
                val pixel = newImage.getPixel(x, y)
                val alpha = Color.alpha(pixel)
                if (alpha > 0) {
                    armPixels.add(x)
                }
            }
        }

        if (armPixels.isNotEmpty()) {
            val armWidth = armPixels.max() - armPixels.min() + 1
            skinFormat = if (armWidth <= 3) {
                SkinFormat.SLIM
            } else {
                SkinFormat.DEFAULT
            }
        }
    }

    /**
     * Get head region
     * Ported from Python get_head_region function
     */
    fun getHeadRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val headX = 8
        val headY = 8
        val headSize = 8

        return Bitmap.createBitmap(image, headX, headY, headSize, headSize)
    }

    /**
     * Get body region
     * Ported from Python get_body_region function
     */
    fun getBodyRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val bodyX = 20
        val bodyY = 20
        val bodyWidth = 8
        val bodyHeight = 12

        return Bitmap.createBitmap(image, bodyX, bodyY, bodyWidth, bodyHeight)
    }

    /**
     * Get right arm region
     * Ported from Python get_right_arm_region function
     */
    fun getRightArmRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val armX = if (skinFormat == SkinFormat.SLIM) 40 else 40
        val armWidth = if (skinFormat == SkinFormat.SLIM) 3 else 4
        val armY = 20
        val armHeight = 12

        return Bitmap.createBitmap(image, armX, armY, armWidth, armHeight)
    }

    /**
     * Get left arm region
     * Ported from Python get_left_arm_region function
     */
    fun getLeftArmRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val armX = if (skinFormat == SkinFormat.SLIM) 32 else 32
        val armWidth = if (skinFormat == SkinFormat.SLIM) 3 else 4
        val armY = 52
        val armHeight = 12

        return Bitmap.createBitmap(image, armX, armY, armWidth, armHeight)
    }

    /**
     * Get right leg region
     * Ported from Python get_right_leg_region function
     */
    fun getRightLegRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val legX = 0
        val legY = 20
        val legWidth = 4
        val legHeight = 12

        return Bitmap.createBitmap(image, legX, legY, legWidth, legHeight)
    }

    /**
     * Get left leg region
     * Ported from Python get_left_leg_region function
     */
    fun getLeftLegRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val legX = 16
        val legY = 52
        val legWidth = 4
        val legHeight = 12

        return Bitmap.createBitmap(image, legX, legY, legWidth, legHeight)
    }

    /**
     * Get all first layer regions from the skin
     * Following Minecraft skin layout (y=0-31 for first layer)
     * Ported from Python get_all_regions function
     */
    fun getAllRegions(): Map<String, Bitmap> {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        return mapOf(
            "head" to Bitmap.createBitmap(image, 8, 8, 8, 8),
            "body" to Bitmap.createBitmap(image, 20, 20, 8, 12),
            "right_arm" to if (skinFormat != SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 40, 20, 4, 12)
            } else {
                Bitmap.createBitmap(image, 40, 20, 3, 12)
            },
            "left_arm" to if (skinFormat != SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 32, 52, 4, 12)
            } else {
                Bitmap.createBitmap(image, 32, 52, 3, 12)
            },
            "right_leg" to Bitmap.createBitmap(image, 0, 20, 4, 12),
            "left_leg" to Bitmap.createBitmap(image, 16, 52, 4, 12)
        )
    }

    /**
     * Get all second layer regions from the skin
     * Following Minecraft skin layout (y=32-63 for second layer)
     * These are the overlay regions that add detail on top of the first layer
     * Ported from Python get_all_second_layer_regions function
     */
    fun getAllSecondLayerRegions(): Map<String, Bitmap> {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        return mapOf(
            "head_hat" to Bitmap.createBitmap(image, 32, 40, 8, 8),
            "body_jacket" to Bitmap.createBitmap(image, 20, 36, 8, 12),
            "right_arm_sleeve" to if (skinFormat != SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 40, 36, 4, 12)
            } else {
                Bitmap.createBitmap(image, 39, 36, 3, 12)
            },
            "left_arm_sleeve" to if (skinFormat != SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 32, 52, 4, 12)
            } else {
                Bitmap.createBitmap(image, 31, 52, 3, 12)
            },
            "right_leg_pantleg" to Bitmap.createBitmap(image, 0, 52, 4, 12),
            "left_leg_pantleg" to Bitmap.createBitmap(image, 16, 52, 4, 12)
        )
    }

    /**
     * Get all second layer faces that correspond to first layer regions
     * This allows the second layer to completely cover the first layer if needed
     * Ported from Python get_all_second_layer_faces function
     */
    fun getAllSecondLayerFaces(): Map<String, Bitmap> {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        return mapOf(
            "head" to Bitmap.createBitmap(image, 32, 40, 8, 8),
            "body" to Bitmap.createBitmap(image, 20, 36, 8, 12),
            "right_arm" to if (skinFormat == SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 39, 36, 3, 12)
            } else {
                Bitmap.createBitmap(image, 40, 36, 4, 12)
            },
            "left_arm" to if (skinFormat == SkinFormat.SLIM) {
                Bitmap.createBitmap(image, 31, 52, 3, 12)
            } else {
                Bitmap.createBitmap(image, 32, 52, 4, 12)
            },
            "right_leg" to Bitmap.createBitmap(image, 0, 52, 4, 12),
            "left_leg" to Bitmap.createBitmap(image, 16, 52, 4, 12)
        )
    }

    /**
     * Resize skin to target size
     * Ported from Python resize_skin function
     */
    fun resizeSkin(targetSize: Pair<Int, Int>? = null): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val (targetWidth, targetHeight) = targetSize ?: (STANDARD_WIDTH to STANDARD_HEIGHT)

        val resizedImage = Bitmap.createScaledBitmap(image, targetWidth, targetHeight, false)
        skinImage = resizedImage
        return resizedImage
    }

    /**
     * Get dimensions of the skin
     * Ported from Python get_dimensions function
     */
    fun getDimensions(): Pair<Int, Int> {
        val image = skinImage ?: return (0 to 0)
        return (image.width to image.height)
    }

    /**
     * Get the scale factor of the skin based on standard 64x64 dimensions
     * Supports HD skins: 128x128, 256x256, 512x512, etc.
     * Ported from Python get_scale_factor function
     */
    fun getScaleFactor(): Float {
        val image = skinImage ?: return 1.0f

        val width = image.width
        val height = image.height

        // Calculate scale factor based on width (assume width = height for standard skins)
        val scaleFactor = width.toFloat() / STANDARD_WIDTH

        return scaleFactor
    }

    /**
     * Get pixel at specified coordinates
     * Ported from Python get_pixel function
     */
    fun getPixel(x: Int, y: Int): IntArray {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        if (x < 0 || y < 0 || x >= image.width || y >= image.height) {
            return intArrayOf(0, 0, 0, 0)
        }

        val pixel = image.getPixel(x, y)
        return intArrayOf(Color.red(pixel), Color.green(pixel), Color.blue(pixel), Color.alpha(pixel))
    }

    /**
     * Set pixel at specified coordinates
     * Ported from Python set_pixel function
     */
    fun setPixel(x: Int, y: Int, color: IntArray) {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        if (x in 0 until image.width && y in 0 until image.height) {
            image.setPixel(x, y, Color.argb(color[3], color[0], color[1], color[2]))
        }
    }

    /**
     * Save skin to file
     * Ported from Python save function
     */
    fun save(outputPath: String) {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        // Note: In actual implementation, you would save to file here
        // This is simplified for the example
    }

    /**
     * Copy the skin processor
     * Ported from Python copy function
     */
    fun copy(): SkinProcessor {
        val newProcessor = SkinProcessor()
        val image = skinImage
        if (image != null) {
            newProcessor.skinImage = image.copy(Bitmap.Config.ARGB_8888, true)
            newProcessor.skinFormat = skinFormat
        }
        return newProcessor
    }

    /**
     * Get the head hat/overlay region from second layer
     * Ported from Python get_head_hat_region function
     */
    fun getHeadHatRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val hatX = 32
        val hatY = 40
        val hatSize = 8

        return Bitmap.createBitmap(image, hatX, hatY, hatSize, hatSize)
    }

    /**
     * Get the body jacket/overlay region from second layer
     * Ported from Python get_body_jacket_region function
     */
    fun getBodyJacketRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val jacketX = 20
        val jacketY = 36
        val jacketWidth = 8
        val jacketHeight = 12

        return Bitmap.createBitmap(image, jacketX, jacketY, jacketWidth, jacketHeight)
    }

    /**
     * Get the right arm sleeve/overlay region from second layer
     * Ported from Python get_right_arm_sleeve_region function
     */
    fun getRightArmSleeveRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val sleeveX = if (skinFormat == SkinFormat.SLIM) 39 else 40
        val sleeveWidth = if (skinFormat == SkinFormat.SLIM) 3 else 4
        val sleeveY = 36
        val sleeveHeight = 12

        return Bitmap.createBitmap(image, sleeveX, sleeveY, sleeveWidth, sleeveHeight)
    }

    /**
     * Get the left arm sleeve/overlay region from second layer
     * Ported from Python get_left_arm_sleeve_region function
     */
    fun getLeftArmSleeveRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val sleeveX = if (skinFormat == SkinFormat.SLIM) 31 else 32
        val sleeveWidth = if (skinFormat == SkinFormat.SLIM) 3 else 4
        val sleeveY = 52
        val sleeveHeight = 12

        return Bitmap.createBitmap(image, sleeveX, sleeveY, sleeveWidth, sleeveHeight)
    }

    /**
     * Get the right leg pantleg/overlay region from second layer
     * Ported from Python get_right_leg_pantleg_region function
     */
    fun getRightLegPantlegRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val pantlegX = 0
        val pantlegY = 52
        val pantlegWidth = 4
        val pantlegHeight = 12

        return Bitmap.createBitmap(image, pantlegX, pantlegY, pantlegWidth, pantlegHeight)
    }

    /**
     * Get the left leg pantleg/overlay region from second layer
     * Ported from Python get_left_leg_pantleg_region function
     */
    fun getLeftLegPantlegRegion(): Bitmap {
        val image = skinImage ?: throw IllegalStateException("No skin loaded")

        val pantlegX = 16
        val pantlegY = 52
        val pantlegWidth = 4
        val pantlegHeight = 12

        return Bitmap.createBitmap(image, pantlegX, pantlegY, pantlegWidth, pantlegHeight)
    }
}

/**
 * Skin format enum
 * Ported from Python SkinFormat class
 */
enum class SkinFormat {
    DEFAULT,
    SLIM,
    LEGACY
}