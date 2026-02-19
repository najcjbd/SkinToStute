package com.skintostatue.android.core.model

import com.skintostatue.android.core.ColorMode
import com.skintostatue.android.core.ColorMatcher

/**
 * Optimized index for fast color matching
 * Ported from Python BlockIndex class
 * Includes all algorithms: caching, priority system, fallback logic
 */
class BlockIndex(
    private val blocks: List<BlockData>,
    private val exactMode: Boolean = false
) {
    private val solidBlocks: List<BlockData>
    private val transparentBlocks: List<BlockData>
    private val colorCache: MutableMap<Pair<Triple<Int, Int, Int>, Boolean>, BlockData> = mutableMapOf()

    // Color priority system - prioritize certain block types
    // Ported from Python BlockIndex class with exact same priority values
    private val colorPriority: Map<String, Int> = mapOf(
        "wool" to 10,
        "concrete" to 8,
        "terracotta" to 7,
        "concrete_powder" to 6,
        "glazed_terracotta" to 5,
        "stained_glass" to 4,
        "shulker_box" to 3,
        "planks" to 2,
        "other" to 1
    )

    init {
        // Separate blocks into solid and transparent
        solidBlocks = blocks.filter { it.color[3] >= 200 }
        transparentBlocks = blocks.filter { it.color[3] < 200 && it.color[3] >= 32 }
    }

    /**
     * Check if a block is solid (opaque)
     * Ported from Python is_solid method
     */
    fun isSolid(blockName: String): Boolean {
        val block = blocks.find { it.name == blockName }
        return block?.color?.get(3) ?: 0 >= 200
    }

    /**
     * Get block priority
     * Ported from Python get_block_priority method
     */
    fun getBlockPriority(blockName: String): Int {
        val blockLower = blockName.lowercase()
        
        return when {
            blockLower.contains("wool") -> colorPriority["wool"] ?: 1
            blockLower.contains("concrete_powder") -> colorPriority["concrete_powder"] ?: 1
            blockLower.contains("concrete") -> colorPriority["concrete"] ?: 1
            blockLower.contains("terracotta") -> colorPriority["terracotta"] ?: 1
            blockLower.contains("glazed_terracotta") -> colorPriority["glazed_terracotta"] ?: 1
            blockLower.contains("stained_glass") -> colorPriority["stained_glass"] ?: 1
            blockLower.contains("shulker_box") -> colorPriority["shulker_box"] ?: 1
            blockLower.contains("planks") -> colorPriority["planks"] ?: 1
            else -> colorPriority["other"] ?: 1
        }
    }

    /**
     * Find best matching block with caching
     * Ported from Python find_best_match method
     * Includes all algorithms: caching, priority system, fallback logic
     */
    fun findBestMatch(
        pixel: IntArray,
        diffFunc: ColorMatcher.ColorDiffable,
        colorWeights: List<Float>,
        wantTransparent: Boolean
    ): BlockData? {
        // Check cache (include wantTransparent in cache key like Python version)
        val cacheKey = Pair(Triple(pixel[0], pixel[1], pixel[2]), wantTransparent)
        val cachedResult = colorCache[cacheKey]
        if (cachedResult != null) {
            return cachedResult
        }

        // Select appropriate block list
        val blockList = if (wantTransparent) transparentBlocks else solidBlocks
        val fallbackList = if (wantTransparent) solidBlocks else transparentBlocks

        // Find best match with priority system
        var bestBlock: BlockData? = null
        var smallestDelta = Double.MAX_VALUE
        var highestPriority = 0

        for (block in blockList) {
            val blockColor = block.color
            val delta = diffFunc.getDelta(
                pixel, blockColor,
                colorWeights[0], colorWeights[1], colorWeights[2]
            )

            // Get block priority
            val priority = getBlockPriority(block.name)

            // Priority-based matching: prefer higher priority blocks for similar delta
            // Allow a small delta threshold for priority override
            val deltaThreshold = smallestDelta * 1.05  // Allow 5% delta difference for priority

            if (delta < smallestDelta || (priority > highestPriority && delta <= deltaThreshold)) {
                smallestDelta = delta
                bestBlock = block
                highestPriority = priority
            }
        }

        // Fallback if no match found (only if not in exact mode)
        if (bestBlock == null && fallbackList.isNotEmpty() && !exactMode) {
            for (block in fallbackList) {
                val blockColor = block.color
                val delta = diffFunc.getDelta(
                    pixel, blockColor,
                    colorWeights[0], colorWeights[1], colorWeights[2]
                )

                if (delta < smallestDelta) {
                    smallestDelta = delta
                    bestBlock = block
                }
            }
        }

        // Cache result
        if (bestBlock != null) {
            colorCache[cacheKey] = bestBlock
        }

        return bestBlock
    }

    /**
     * Get all blocks
     */
    fun getAllBlocks(): List<BlockData> {
        return blocks
    }

    /**
     * Get solid blocks
     */
    fun getSolidBlocks(): List<BlockData> {
        return solidBlocks
    }

    /**
     * Get transparent blocks
     */
    fun getTransparentBlocks(): List<BlockData> {
        return transparentBlocks
    }

    /**
     * Clear cache
     */
    fun clearCache() {
        colorCache.clear()
    }
}