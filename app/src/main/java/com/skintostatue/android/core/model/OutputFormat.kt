package com.skintostatue.android.core.model

/**
 * Output format types
 * Ported from Python settings.py
 */
enum class OutputFormat(val extension: String, val displayName: String) {
    SPONGE_SCHEMATIC("schem", "Sponge Schematic"),
    MCSTRUCTURE("mcstructure", "Bedrock Structure"),
    LITEMATICA("litematic", "Litematica")
}

/**
 * Minecraft versions
 * Ported from Python MinecraftVersion enum
 */
enum class MinecraftVersion(val displayName: String) {
    JE_1_12_2("Java Edition 1.12.2"),
    JE_1_13_2("Java Edition 1.13.2"),
    JE_1_14_4("Java Edition 1.14.4"),
    JE_1_15_2("Java Edition 1.15.2"),
    JE_1_16_1("Java Edition 1.16.1"),
    JE_1_16_5("Java Edition 1.16.5"),
    JE_1_17_1("Java Edition 1.17.1"),
    JE_1_18_2("Java Edition 1.18.2"),
    JE_1_19_4("Java Edition 1.19.4"),
    JE_1_20_4("Java Edition 1.20.4"),
    JE_1_21("Java Edition 1.21")
}

/**
 * Schematic formats
 * Ported from Python SchematicFormat enum
 */
enum class SchematicFormat {
    SPONGE,
    LEGACY
}

/**
 * Direction types for statue orientation
 */
enum class Direction(val displayName: String) {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West")
}

/**
 * Plane types for statue orientation
 */
enum class Plane {
    XY,
    YZ,
    XZ
}

/**
 * Orientation configuration for statue
 * Ported from Python orientation settings
 */
data class Orientation(
    val direction: Direction = Direction.NORTH,
    val rotate: Int = 0,
    val plane: Plane = Plane.XZ,
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val offset: Offset = Offset()
)

/**
 * Offset configuration for statue position
 */
data class Offset(
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
)

/**
 * Skin format types
 * Ported from Python SkinFormat class
 */
enum class SkinFormat {
    DEFAULT,
    SLIM,
    LEGACY
}

/**
 * Armor system types
 * Ported from Python ArmorSystem enum
 */
enum class ArmorSystem {
    STANDARD,
    PLUGIN
}

/**
 * Armor piece types
 * Ported from Python ArmorPiece enum
 */
enum class ArmorPiece(val displayName: String) {
    HELMET("Helmet"),
    CHESTPLATE("Chestplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots")
}

/**
 * Armor materials
 * Ported from Python armor_manager.py
 */
enum class ArmorMaterial(val displayName: String, val color: IntArray) {
    LEATHER("Leather", intArrayOf(160, 101, 64, 255)),
    CHAINMAIL("Chainmail", intArrayOf(128, 128, 128, 255)),
    IRON("Iron", intArrayOf(220, 220, 220, 255)),
    GOLD("Golden", intArrayOf(255, 215, 0, 255)),
    DIAMOND("Diamond", intArrayOf(85, 255, 255, 255)),
    NETHERITE("Netherite", intArrayOf(78, 78, 78, 255))
}

/**
 * Leather dye colors
 * Ported from Python armor_manager.py
 */
data class LeatherDyeColor(
    val name: String,
    val rgb: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LeatherDyeColor

        if (name != other.name) return false
        if (!rgb.contentEquals(other.rgb)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + rgb.contentHashCode()
        return result
    }
}

/**
 * All 16 leather dye colors
 * Ported from Python armor_manager.py
 */
object LeatherColors {
    val WHITE = LeatherDyeColor("white", intArrayOf(249, 255, 254, 255))
    val ORANGE = LeatherDyeColor("orange", intArrayOf(249, 128, 29, 255))
    val MAGENTA = LeatherDyeColor("magenta", intArrayOf(199, 78, 189, 255))
    val LIGHT_BLUE = LeatherDyeColor("light_blue", intArrayOf(58, 179, 218, 255))
    val YELLOW = LeatherDyeColor("yellow", intArrayOf(254, 216, 61, 255))
    val LIME = LeatherDyeColor("lime", intArrayOf(128, 199, 31, 255))
    val PINK = LeatherDyeColor("pink", intArrayOf(243, 139, 170, 255))
    val GRAY = LeatherDyeColor("gray", intArrayOf(125, 125, 125, 255))
    val LIGHT_GRAY = LeatherDyeColor("light_gray", intArrayOf(180, 180, 180, 255))
    val CYAN = LeatherDyeColor("cyan", intArrayOf(42, 179, 209, 255))
    val PURPLE = LeatherDyeColor("purple", intArrayOf(126, 61, 181, 255))
    val BLUE = LeatherDyeColor("blue", intArrayOf(46, 56, 141, 255))
    val BROWN = LeatherDyeColor("brown", intArrayOf(86, 51, 26, 255))
    val GREEN = LeatherDyeColor("green", intArrayOf(56, 83, 26, 255))
    val RED = LeatherDyeColor("red", intArrayOf(168, 36, 36, 255))
    val BLACK = LeatherDyeColor("black", intArrayOf(25, 20, 20, 255))

    fun all(): List<LeatherDyeColor> {
        return listOf(
            WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY,
            LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK
        )
    }

    fun fromName(name: String): LeatherDyeColor? {
        return all().find { it.name == name }
    }
}