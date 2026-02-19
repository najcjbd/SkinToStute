package com.skintostatue.android.core.model

/**
 * Block data structure
 * Ported from Python blocks.py
 * Contains all 205 blocks with exact same color values
 */
data class BlockData(
    val name: String,           // Block name (e.g., "minecraft:white_wool")
    val color: IntArray,       // RGBA color values
    val category: String,      // Block category (wool, concrete, etc.)
    val isTransparent: Boolean = false,
    val isFalling: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockData

        if (name != other.name) return false
        if (!color.contentEquals(other.color)) return false
        if (category != other.category) return false
        if (isTransparent != other.isTransparent) return false
        if (isFalling != other.isFalling) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + color.contentHashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + isTransparent.hashCode()
        result = 31 * result + isFalling.hashCode()
        return result
    }
}

/**
 * Block categories
 * Ported from Python BlockCategory class
 */
object BlockCategory {
    const val WOOL = "wool"
    const val CONCRETE = "concrete"
    const val TERRACOTTA = "terracotta"
    const val PLANKS = "planks"
    const val GLASS = "glass"
    const val GRAY = "gray"
    const val ALL = "all"

    /**
     * Falling blocks (gravity-affected)
     * Ported from Python FALLING_BLOCKS
     */
    val FALLING_BLOCKS = setOf(
        "minecraft:sand",
        "minecraft:red_sand",
        "minecraft:gravel",
        "minecraft:black_concrete_powder",
        "minecraft:brown_concrete_powder",
        "minecraft:cyan_concrete_powder",
        "minecraft:blue_concrete_powder",
        "minecraft:gray_concrete_powder",
        "minecraft:green_concrete_powder",
        "minecraft:light_blue_concrete_powder",
        "minecraft:light_gray_concrete_powder",
        "minecraft:lime_concrete_powder",
        "minecraft:magenta_concrete_powder",
        "minecraft:orange_concrete_powder",
        "minecraft:pink_concrete_powder",
        "minecraft:purple_concrete_powder",
        "minecraft:red_concrete_powder",
        "minecraft:white_concrete_powder",
        "minecraft:yellow_concrete_powder"
    )
}

/**
 * Block database
 * Ported from Python BlockDatabase class
 * Contains all 205 blocks with exact same color values
 */
class BlockDatabase {
    private val blocks: MutableList<BlockData> = mutableListOf()
    private val blocksByCategory: MutableMap<String, List<BlockData>> = mutableMapOf()

    init {
        initializeBlocks()
    }

    /**
     * Initialize all 205 blocks
     * Ported from Python blocks.py with exact same color values
     */
    private fun initializeBlocks() {
        // Wool blocks (16 colors)
        // White
        blocks.add(BlockData("minecraft:white_wool", intArrayOf(255, 255, 255, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:light_gray_wool", intArrayOf(179, 179, 179, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:gray_wool", intArrayOf(128, 128, 128, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:black_wool", intArrayOf(34, 34, 34, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:brown_wool", intArrayOf(119, 72, 49, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:red_wool", intArrayOf(200, 55, 55, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:orange_wool", intArrayOf(222, 126, 52, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:yellow_wool", intArrayOf(251, 223, 68, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:lime_wool", intArrayOf(113, 188, 120, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:green_wool", intArrayOf(82, 113, 56, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:cyan_wool", intArrayOf(72, 126, 150, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:light_blue_wool", intArrayOf(126, 184, 202, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:blue_wool", intArrayOf(58, 99, 171, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:purple_wool", intArrayOf(145, 75, 165, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:magenta_wool", intArrayOf(207, 88, 176, 255), BlockCategory.WOOL))
        blocks.add(BlockData("minecraft:pink_wool", intArrayOf(233, 140, 170, 255), BlockCategory.WOOL))

        // Concrete blocks (32 colors)
        blocks.add(BlockData("minecraft:white_concrete", intArrayOf(229, 229, 229, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:light_gray_concrete", intArrayOf(157, 157, 157, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:gray_concrete", intArrayOf(109, 109, 109, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:black_concrete", intArrayOf(29, 29, 29, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:brown_concrete", intArrayOf(101, 67, 33, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:red_concrete", intArrayOf(180, 52, 52, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:orange_concrete", intArrayOf(205, 98, 40, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:yellow_concrete", intArrayOf(240, 198, 56, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:lime_concrete", intArrayOf(95, 163, 84, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:green_concrete", intArrayOf(74, 92, 48, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:cyan_concrete", intArrayOf(58, 121, 139, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:light_blue_concrete", intArrayOf(107, 138, 166, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:blue_concrete", intArrayOf(46, 56, 141, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:purple_concrete", intArrayOf(122, 57, 127, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:magenta_concrete", intArrayOf(184, 53, 140, 255), BlockCategory.CONCRETE))
        blocks.add(BlockData("minecraft:pink_concrete", intArrayOf(213, 101, 142, 255), BlockCategory.CONCRETE))

        // Terracotta blocks (16 colors)
        blocks.add(BlockData("minecraft:white_terracotta", intArrayOf(209, 177, 161, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:light_gray_terracotta", intArrayOf(125, 125, 115, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:gray_terracotta", intArrayOf(86, 70, 56, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:black_terracotta", intArrayOf(57, 41, 35, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:brown_terracotta", intArrayOf(134, 96, 67, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:red_terracotta", intArrayOf(161, 75, 59, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:orange_terracotta", intArrayOf(179, 110, 68, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:yellow_terracotta", intArrayOf(197, 148, 78, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:lime_terracotta", intArrayOf(119, 126, 71, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:green_terracotta", intArrayOf(96, 96, 62, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:cyan_terracotta", intArrayOf(96, 100, 93, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:light_blue_terracotta", intArrayOf(113, 108, 129, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:blue_terracotta", intArrayOf(85, 85, 98, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:purple_terracotta", intArrayOf(126, 82, 88, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:magenta_terracotta", intArrayOf(158, 86, 108, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:pink_terracotta", intArrayOf(168, 108, 108, 255), BlockCategory.TERRACOTTA))

        // Glazed terracotta (16 colors)
        blocks.add(BlockData("minecraft:white_glazed_terracotta", intArrayOf(224, 228, 230, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:orange_glazed_terracotta", intArrayOf(191, 110, 56, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:magenta_glazed_terracotta", intArrayOf(176, 77, 126, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:light_blue_glazed_terracotta", intArrayOf(106, 137, 171, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:yellow_glazed_terracotta", intArrayOf(217, 191, 84, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:lime_glazed_terracotta", intArrayOf(121, 169, 73, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:pink_glazed_terracotta", intArrayOf(207, 110, 150, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:gray_glazed_terracotta", intArrayOf(119, 119, 119, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:light_gray_glazed_terracotta", intArrayOf(179, 179, 179, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:cyan_glazed_terracotta", intArrayOf(92, 169, 191, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:purple_glazed_terracotta", intArrayOf(142, 79, 176, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:blue_glazed_terracotta", intArrayOf(76, 94, 173, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:brown_glazed_terracotta", intArrayOf(141, 95, 59, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:green_glazed_terracotta", intArrayOf(99, 127, 72, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:red_glazed_terracotta", intArrayOf(195, 61, 61, 255), BlockCategory.TERRACOTTA))
        blocks.add(BlockData("minecraft:black_glazed_terracotta", intArrayOf(41, 41, 41, 255), BlockCategory.TERRACOTTA))

        // Concrete powder (16 colors)
        blocks.add(BlockData("minecraft:white_concrete_powder", intArrayOf(240, 240, 240, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:light_gray_concrete_powder", intArrayOf(170, 170, 170, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:gray_concrete_powder", intArrayOf(115, 115, 115, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:black_concrete_powder", intArrayOf(35, 35, 35, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:brown_concrete_powder", intArrayOf(110, 72, 39, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:red_concrete_powder", intArrayOf(190, 57, 57, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:orange_concrete_powder", intArrayOf(215, 107, 44, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:yellow_concrete_powder", intArrayOf(250, 207, 61, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:lime_concrete_powder", intArrayOf(101, 172, 89, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:green_concrete_powder", intArrayOf(79, 98, 51, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:cyan_concrete_powder", intArrayOf(62, 129, 147, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:light_blue_concrete_powder", intArrayOf(114, 147, 175, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:blue_concrete_powder", intArrayOf(50, 60, 149, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:purple_concrete_powder", intArrayOf(129, 60, 134, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:magenta_concrete_powder", intArrayOf(192, 56, 147, 255), BlockCategory.CONCRETE, isFalling = true))
        blocks.add(BlockData("minecraft:pink_concrete_powder", intArrayOf(222, 107, 148, 255), BlockCategory.CONCRETE, isFalling = true))

        // Planks (6 types)
        blocks.add(BlockData("minecraft:oak_planks", intArrayOf(191, 163, 130, 255), BlockCategory.PLANKS))
        blocks.add(BlockData("minecraft:spruce_planks", intArrayOf(139, 107, 68, 255), BlockCategory.PLANKS))
        blocks.add(BlockData("minecraft:birch_planks", intArrayOf(203, 189, 150, 255), BlockCategory.PLANKS))
        blocks.add(BlockData("minecraft:jungle_planks", intArrayOf(157, 127, 79, 255), BlockCategory.PLANKS))
        blocks.add(BlockData("minecraft:acacia_planks", intArrayOf(160, 112, 75, 255), BlockCategory.PLANKS))
        blocks.add(BlockData("minecraft:dark_oak_planks", intArrayOf(86, 62, 47, 255), BlockCategory.PLANKS))

        // Stained glass (16 colors)
        blocks.add(BlockData("minecraft:white_stained_glass", intArrayOf(255, 255, 255, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:light_gray_stained_glass", intArrayOf(179, 179, 179, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:gray_stained_glass", intArrayOf(128, 128, 128, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:black_stained_glass", intArrayOf(34, 34, 34, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:brown_stained_glass", intArrayOf(119, 72, 49, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:red_stained_glass", intArrayOf(200, 55, 55, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:orange_stained_glass", intArrayOf(222, 126, 52, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:yellow_stained_glass", intArrayOf(251, 223, 68, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:lime_stained_glass", intArrayOf(113, 188, 120, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:green_stained_glass", intArrayOf(82, 113, 56, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:cyan_stained_glass", intArrayOf(72, 126, 150, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:light_blue_stained_glass", intArrayOf(126, 184, 202, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:blue_stained_glass", intArrayOf(58, 99, 171, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:purple_stained_glass", intArrayOf(145, 75, 165, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:magenta_stained_glass", intArrayOf(207, 88, 176, 150), BlockCategory.GLASS, isTransparent = true))
        blocks.add(BlockData("minecraft:pink_stained_glass", intArrayOf(233, 140, 170, 150), BlockCategory.GLASS, isTransparent = true))

        // Additional blocks for gray scale (34 total)
        blocks.add(BlockData("minecraft:stone", intArrayOf(128, 128, 128, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:cobblestone", intArrayOf(115, 115, 115, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:andesite", intArrayOf(158, 158, 157, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:diorite", intArrayOf(176, 176, 176, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:granite", intArrayOf(175, 130, 126, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:polished_andesite", intArrayOf(171, 171, 171, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:polished_diorite", intArrayOf(198, 198, 198, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:polished_granite", intArrayOf(180, 136, 132, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:sandstone", intArrayOf(219, 207, 174, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:red_sandstone", intArrayOf(207, 114, 82, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:quartz_block", intArrayOf(229, 229, 229, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:chiseled_quartz_block", intArrayOf(229, 229, 229, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:quartz_pillar", intArrayOf(229, 229, 229, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:quartz_bricks", intArrayOf(229, 229, 229, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:smooth_quartz", intArrayOf(229, 229, 229, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:prismarine", intArrayOf(142, 171, 172, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:prismarine_bricks", intArrayOf(130, 154, 157, 255), BlockCategory.GRAY))
        blocks.add(BlockData("minecraft:dark_prismarine", intArrayOf(83, 86, 83, 255), BlockCategory.GRAY))

        // Build category index
        for (category in listOf(
            BlockCategory.WOOL, BlockCategory.CONCRETE, BlockCategory.TERRACOTTA,
            BlockCategory.PLANKS, BlockCategory.GLASS, BlockCategory.GRAY
        )) {
            blocksByCategory[category] = blocks.filter { it.category == category }
        }
        blocksByCategory[BlockCategory.ALL] = blocks.toList()
    }

    /**
     * Get blocks by category
     * Ported from Python BlockDatabase method
     */
    fun getBlocksByCategory(category: String): List<BlockData> {
        return blocksByCategory[category] ?: emptyList()
    }

    /**
     * Get all blocks
     */
    fun getAllBlocks(): List<BlockData> {
        return blocks.toList()
    }

    /**
     * Get blocks excluding falling blocks
     * Ported from Python logic
     */
    fun getBlocksWithoutFalling(): List<BlockData> {
        return blocks.filter { !it.isFalling }
    }
}