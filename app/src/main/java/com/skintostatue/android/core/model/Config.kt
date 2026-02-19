package com.skintostatue.android.core.model

import kotlinx.coroutines.flow.Flow

/**
 * Application configuration
 * Ported from Python settings.py with DataStore for reactive configuration
 * Replaces hot reload with reactive Flow-based updates
 */
data class Config(
    // Output settings
    val outputFormat: OutputFormat = OutputFormat.SPONGE_SCHEMATIC,
    val schematicFormat: SchematicFormat = SchematicFormat.SPONGE,
    val outputDirectory: String = "/storage/emulated/0/Download",
    val outputFilename: String = "statue",

    // Color settings
    val colorMode: ColorMode = ColorMode.LAB,
    val blockCategories: List<String> = listOf(
        BlockCategory.WOOL,
        BlockCategory.CONCRETE,
        BlockCategory.TERRACOTTA,
        BlockCategory.PLANKS,
        BlockCategory.GLASS
    ),
    val excludeFallingBlocks: Boolean = true,
    val colorWeights: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f),

    // Filter settings
    val hue: Float = 0.0f,
    val saturation: Float = 1.0f,
    val brightness: Float = 1.0f,
    val contrast: Float = 1.0f,
    val posterize: Int = 0,

    // Orientation settings
    val direction: Direction = Direction.NORTH,
    val rotate: Int = 0,
    val plane: Plane = Plane.XZ,
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val offsetX: Int = 0,
    val offsetY: Int = 0,
    val offsetZ: Int = 0,

    // Statue settings
    val scale: Float = 1.0f,
    val includeHead: Boolean = true,
    val includeBody: Boolean = true,
    val includeArms: Boolean = true,
    val includeLegs: Boolean = true,
    val pose: String = "standing",

    // Armor settings
    val armorEnabled: Boolean = false,
    val armorMaterial: String = "",
    val armorPieces: List<ArmorPiece> = listOf(
        ArmorPiece.HELMET,
        ArmorPiece.CHESTPLATE,
        ArmorPiece.BOOTS
    ),
    val armorSystem: ArmorSystem = ArmorSystem.STANDARD,
    val autoSaveColor: Boolean = true,
    val leatherColorName: String = "",
    val leatherColorRgb: IntArray? = null,

    // Advanced settings
    val dithering: Boolean = false,
    val antialiasing: Boolean = false,
    val optimizePalette: Boolean = true,
    val exactMode: Boolean = false,
    val multithreading: Boolean = false,
    val maxWorkers: Int = 4,
    val saveMaterialsList: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Config

        if (outputFormat != other.outputFormat) return false
        if (schematicFormat != other.schematicFormat) return false
        if (outputDirectory != other.outputDirectory) return false
        if (outputFilename != other.outputFilename) return false
        if (colorMode != other.colorMode) return false
        if (blockCategories != other.blockCategories) return false
        if (excludeFallingBlocks != other.excludeFallingBlocks) return false
        if (!colorWeights.contentEquals(other.colorWeights)) return false
        if (hue != other.hue) return false
        if (saturation != other.saturation) return false
        if (brightness != other.brightness) return false
        if (contrast != other.contrast) return false
        if (posterize != other.posterize) return false
        if (direction != other.direction) return false
        if (rotate != other.rotate) return false
        if (plane != other.plane) return false
        if (flipHorizontal != other.flipHorizontal) return false
        if (flipVertical != other.flipVertical) return false
        if (offsetX != other.offsetX) return false
        if (offsetY != other.offsetY) return false
        if (offsetZ != other.offsetZ) return false
        if (scale != other.scale) return false
        if (includeHead != other.includeHead) return false
        if (includeBody != other.includeBody) return false
        if (includeArms != other.includeArms) return false
        if (includeLegs != other.includeLegs) return false
        if (pose != other.pose) return false
        if (armorEnabled != other.armorEnabled) return false
        if (armorMaterial != other.armorMaterial) return false
        if (armorPieces != other.armorPieces) return false
        if (armorSystem != other.armorSystem) return false
        if (autoSaveColor != other.autoSaveColor) return false
        if (leatherColorName != other.leatherColorName) return false
        if (leatherColorRgb != null) {
            if (other.leatherColorRgb == null) return false
            if (!leatherColorRgb.contentEquals(other.leatherColorRgb)) return false
        } else if (other.leatherColorRgb != null) return false
        if (dithering != other.dithering) return false
        if (antialiasing != other.antialiasing) return false
        if (optimizePalette != other.optimizePalette) return false
        if (exactMode != other.exactMode) return false
        if (multithreading != other.multithreading) return false
        if (maxWorkers != other.maxWorkers) return false
        if (saveMaterialsList != other.saveMaterialsList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = outputFormat.hashCode()
        result = 31 * result + schematicFormat.hashCode()
        result = 31 * result + outputDirectory.hashCode()
        result = 31 * result + outputFilename.hashCode()
        result = 31 * result + colorMode.hashCode()
        result = 31 * result + blockCategories.hashCode()
        result = 31 * result + excludeFallingBlocks.hashCode()
        result = 31 * result + colorWeights.contentHashCode()
        result = 31 * result + hue.hashCode()
        result = 31 * result + saturation.hashCode()
        result = 31 * result + brightness.hashCode()
        result = 31 * result + contrast.hashCode()
        result = 31 * result + posterize.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + rotate.hashCode()
        result = 31 * result + plane.hashCode()
        result = 31 * result + flipHorizontal.hashCode()
        result = 31 * result + flipVertical.hashCode()
        result = 31 * result + offsetX.hashCode()
        result = 31 * result + offsetY.hashCode()
        result = 31 * result + offsetZ.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + includeHead.hashCode()
        result = 31 * result + includeBody.hashCode()
        result = 31 * result + includeArms.hashCode()
        result = 31 * result + includeLegs.hashCode()
        result = 31 * result + pose.hashCode()
        result = 31 * result + armorEnabled.hashCode()
        result = 31 * result + armorMaterial.hashCode()
        result = 31 * result + armorPieces.hashCode()
        result = 31 * result + armorSystem.hashCode()
        result = 31 * result + autoSaveColor.hashCode()
        result = 31 * result + leatherColorName.hashCode()
        result = 31 * result + (leatherColorRgb?.contentHashCode() ?: 0)
        result = 31 * result + dithering.hashCode()
        result = 31 * result + antialiasing.hashCode()
        result = 31 * result + optimizePalette.hashCode()
        result = 31 * result + exactMode.hashCode()
        result = 31 * result + multithreading.hashCode()
        result = 31 * result + maxWorkers.hashCode()
        result = 31 * result + saveMaterialsList.hashCode()
        return result
    }
}

/**
 * Configuration manager interface
 * Ported from Python Settings class with reactive Flow API
 * Replaces hot reload with reactive configuration updates
 */
interface ConfigManager {
    /**
     * Get current configuration as Flow
     * Automatically updates when configuration changes
     */
    fun getConfigFlow(): Flow<Config>

    /**
     * Get current configuration (synchronous)
     */
    suspend fun getConfig(): Config

    /**
     * Update configuration
     */
    suspend fun updateConfig(update: (Config) -> Config)

    /**
     * Reset to default configuration
     */
    suspend fun resetToDefaults()

    /**
     * Validate current configuration
     */
    fun validate(config: Config): List<String>
}

/**
 * Default configuration validation
 * Ported from Python Settings.validate() method
 */
fun validateConfig(config: Config): List<String> {
    val errors = mutableListOf<String>()

    // Validate scale
    if (config.scale <= 0f) {
        errors.add("Scale must be greater than 0")
    }

    // Validate color weights
    if (config.colorWeights.size != 3) {
        errors.add("Color weights must have exactly 3 values")
    }

    // Validate hue (0-1)
    if (config.hue < 0f || config.hue > 1f) {
        errors.add("Hue value must be between 0.0 and 1.0")
    }

    // Validate saturation (0-1)
    if (config.saturation < 0f || config.saturation > 1f) {
        errors.add("Saturation value must be between 0.0 and 1.0")
    }

    // Validate brightness (0-1)
    if (config.brightness < 0f || config.brightness > 1f) {
        errors.add("Brightness value must be between 0.0 and 1.0")
    }

    // Validate contrast (0-1)
    if (config.contrast < 0f || config.contrast > 1f) {
        errors.add("Contrast value must be between 0.0 and 1.0")
    }

    // Validate posterize (0-128)
    if (config.posterize < 0 || config.posterize > 128) {
        errors.add("Posterize value must be between 0 and 128")
    }

    return errors
}