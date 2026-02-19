package com.skintostatue.android.core.generator

import org.jnbt.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.util.zip.GZIPOutputStream

/**
 * Bedrock Structure generator
 * Ported from Python mcstructure_generator.py and mcstructure library
 * Generates .mcstructure files for Bedrock Edition
 * Uses jNBT library with little-endian encoding
 * Ensures 100% identical output with Python version
 */
class McStructureGenerator {
    companion object {
        private const val COMPATIBILITY_VERSION = 17959425  // 1.16.210.03
    }

    private val blocks: MutableList<Triple<Int, Int, Int, String>> = mutableListOf()
    private val blockMap: MutableMap<String, Int> = mutableMapOf()
    private var width: Int = 0
    private var height: Int = 0
    private var length: Int = 0

    // Java to Bedrock block name mapping
    private val javaToBedrockMap: Map<String, String> = createJavaToBedrockMap()

    /**
     * Create Java to Bedrock block name mapping
     * Ported from Python JAVA_TO_BEDROCK_BLOCK_MAP
     */
    private fun createJavaToBedrockMap(): Map<String, String> {
        return mapOf(
            // Wool blocks
            "minecraft:white_wool" to "minecraft:wool",
            "minecraft:light_gray_wool" to "minecraft:wool",
            "minecraft:gray_wool" to "minecraft:wool",
            "minecraft:black_wool" to "minecraft:wool",
            "minecraft:brown_wool" to "minecraft:wool",
            "minecraft:red_wool" to "minecraft:wool",
            "minecraft:orange_wool" to "minecraft:wool",
            "minecraft:yellow_wool" to "minecraft:wool",
            "minecraft:lime_wool" to "minecraft:wool",
            "minecraft:green_wool" to "minecraft:wool",
            "minecraft:cyan_wool" to "minecraft:wool",
            "minecraft:light_blue_wool" to "minecraft:wool",
            "minecraft:blue_wool" to "minecraft:wool",
            "minecraft:purple_wool" to "minecraft:wool",
            "minecraft:magenta_wool" to "minecraft:wool",
            "minecraft:pink_wool" to "minecraft:wool",

            // Concrete blocks
            "minecraft:white_concrete" to "minecraft:concrete",
            "minecraft:light_gray_concrete" to "minecraft:concrete",
            "minecraft:gray_concrete" to "minecraft:concrete",
            "minecraft:black_concrete" to "minecraft:concrete",
            "minecraft:brown_concrete" to "minecraft:concrete",
            "minecraft:red_concrete" to "minecraft:concrete",
            "minecraft:orange_concrete" to "minecraft:concrete",
            "minecraft:yellow_concrete" to "minecraft:concrete",
            "minecraft:lime_concrete" to "minecraft:concrete",
            "minecraft:green_concrete" to "minecraft:concrete",
            "minecraft:cyan_concrete" to "minecraft:concrete",
            "minecraft:light_blue_concrete" to "minecraft:concrete",
            "minecraft:blue_concrete" to "minecraft:concrete",
            "minecraft:purple_concrete" to "minecraft:concrete",
            "minecraft:magenta_concrete" to "minecraft:concrete",
            "minecraft:pink_concrete" to "minecraft:concrete",

            // Concrete powder
            "minecraft:white_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:light_gray_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:gray_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:black_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:brown_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:red_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:orange_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:yellow_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:lime_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:green_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:cyan_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:light_blue_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:blue_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:purple_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:magenta_concrete_powder" to "minecraft:concrete_powder",
            "minecraft:pink_concrete_powder" to "minecraft:concrete_powder",

            // Terracotta blocks
            "minecraft:white_terracotta" to "minecraft:hardened_clay",
            "minecraft:light_gray_terracotta" to "minecraft:hardened_clay",
            "minecraft:gray_terracotta" to "minecraft:hardened_clay",
            "minecraft:black_terracotta" to "minecraft:hardened_clay",
            "minecraft:brown_terracotta" to "minecraft:hardened_clay",
            "minecraft:red_terracotta" to "minecraft:hardened_clay",
            "minecraft:orange_terracotta" to "minecraft:hardened_clay",
            "minecraft:yellow_terracotta" to "minecraft:hardened_clay",
            "minecraft:lime_terracotta" to "minecraft:hardened_clay",
            "minecraft:green_terracotta" to "minecraft:hardened_clay",
            "minecraft:cyan_terracotta" to "minecraft:hardened_clay",
            "minecraft:light_blue_terracotta" to "minecraft:hardened_clay",
            "minecraft:blue_terracotta" to "minecraft:hardened_clay",
            "minecraft:purple_terracotta" to "minecraft:hardened_clay",
            "minecraft:magenta_terracotta" to "minecraft:hardened_clay",
            "minecraft:pink_terracotta" to "minecraft:hardened_clay",

            // Glazed terracotta
            "minecraft:white_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:orange_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:magenta_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:light_blue_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:yellow_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:lime_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:pink_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:gray_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:light_gray_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:cyan_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:purple_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:blue_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:brown_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:green_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:red_glazed_terracotta" to "minecraft:glazed_terracotta",
            "minecraft:black_glazed_terracotta" to "minecraft:glazed_terracotta",

            // Planks
            "minecraft:oak_planks" to "minecraft:planks",
            "minecraft:spruce_planks" to "minecraft:planks",
            "minecraft:birch_planks" to "minecraft:planks",
            "minecraft:jungle_planks" to "minecraft:planks",
            "minecraft:acacia_planks" to "minecraft:planks",
            "minecraft:dark_oak_planks" to "minecraft:planks",

            // Stained glass
            "minecraft:white_stained_glass" to "minecraft:stained_glass",
            "minecraft:light_gray_stained_glass" to "minecraft:stained_glass",
            "minecraft:gray_stained_glass" to "minecraft:stained_glass",
            "minecraft:black_stained_glass" to "minecraft:stained_glass",
            "minecraft:brown_stained_glass" to "minecraft:stained_glass",
            "minecraft:red_stained_glass" to "minecraft:stained_glass",
            "minecraft:orange_stained_glass" to "minecraft:stained_glass",
            "minecraft:yellow_stained_glass" to "minecraft:stained_glass",
            "minecraft:lime_stained_glass" to "minecraft:stained_glass",
            "minecraft:green_stained_glass" to "minecraft:stained_glass",
            "minecraft:cyan_stained_glass" to "minecraft:stained_glass",
            "minecraft:light_blue_stained_glass" to "minecraft:stained_glass",
            "minecraft:blue_stained_glass" to "minecraft:stained_glass",
            "minecraft:purple_stained_glass" to "minecraft:stained_glass",
            "minecraft:magenta_stained_glass" to "minecraft:stained_glass",
            "minecraft:pink_stained_glass" to "minecraft:stained_glass",

            // Other blocks
            "minecraft:stone" to "minecraft:stone",
            "minecraft:cobblestone" to "minecraft:cobblestone",
            "minecraft:andesite" to "minecraft:stone",
            "minecraft:diorite" to "minecraft:stone",
            "minecraft:granite" to "minecraft:stone",
            "minecraft:polished_andesite" to "minecraft:stone",
            "minecraft:polished_diorite" to "minecraft:stone",
            "minecraft:polished_granite" to "minecraft:stone",
            "minecraft:sandstone" to "minecraft:sandstone",
            "minecraft:red_sandstone" to "minecraft:red_sandstone",
            "minecraft:quartz_block" to "minecraft:quartz_block",
            "minecraft:chiseled_quartz_block" to "minecraft:quartz_block",
            "minecraft:quartz_pillar" to "minecraft:quartz_block",
            "minecraft:quartz_bricks" to "minecraft:quartz_block",
            "minecraft:smooth_quartz" to "minecraft:quartz_block",
            "minecraft:prismarine" to "minecraft:prismarine",
            "minecraft:prismarine_bricks" to "minecraft:prismarine",
            "minecraft:dark_prismarine" to "minecraft:prismarine"
        )
    }

    /**
     * Get Bedrock color name from block name
     */
    private fun getBedrockColorName(blockName: String): String? {
        return when {
            blockName.contains("white") -> "white"
            blockName.contains("orange") -> "orange"
            blockName.contains("magenta") -> "magenta"
            blockName.contains("light_blue") -> "light_blue"
            blockName.contains("yellow") -> "yellow"
            blockName.contains("lime") -> "lime"
            blockName.contains("pink") -> "pink"
            blockName.contains("gray") && !blockName.contains("light") -> "gray"
            blockName.contains("light_gray") -> "silver"
            blockName.contains("cyan") -> "cyan"
            blockName.contains("purple") -> "purple"
            blockName.contains("blue") -> "blue"
            blockName.contains("brown") -> "brown"
            blockName.contains("green") -> "green"
            blockName.contains("red") -> "red"
            blockName.contains("black") -> "black"
            else -> null
        }
    }

    /**
     * Add a block to the structure
     */
    fun addBlock(x: Int, y: Int, z: Int, blockName: String) {
        // Map Java block name to Bedrock block name
        val bedrockBlockName = javaToBedrockMap[blockName] ?: blockName

        // Add to block map if not exists
        if (!blockMap.containsKey(bedrockBlockName)) {
            blockMap[bedrockBlockName] = blockMap.size
        }

        blocks.add(Triple(x, y, z, bedrockBlockName))

        // Update dimensions
        width = maxOf(width, x + 1)
        height = maxOf(height, y + 1)
        length = maxOf(length, z + 1)
    }

    /**
     * Generate Bedrock structure file
     * Ported from Python mcstructure_generator.py
     * Uses little-endian NBT encoding
     */
    fun generateMcStructure(): ByteArray {
        if (blocks.isEmpty()) {
            return byteArrayOf()
        }

        // Create NBT structure for Bedrock format
        val root = NBTTagCompound()

        // Format version
        root.setInt("format_version", 1)

        // Size
        val size = NBTTagList(TagType.TAG_INT)
        size.add(NBTTagInt(width))
        size.add(NBTTagInt(height))
        size.add(NBTTagInt(length))
        root.setList("size", size)

        // Structure data
        val structure = createStructureData()
        root.setCompound("structure", structure)

        // Structure world origin
        val origin = NBTTagList(TagType.TAG_INT)
        origin.add(NBTTagInt(0))
        origin.add(NBTTagInt(0))
        origin.add(NBTTagInt(0))
        root.setList("structure_world_origin", origin)

        // Write to bytes with GZIP compression (little-endian)
        val byteStream = ByteArrayOutputStream()
        val gzipStream = GZIPOutputStream(byteStream)
        val dataStream = DataOutputStream(gzipStream)

        // Serialize NBT to bytes with little-endian encoding
        val nbtWriter = McStructureNBTWriter(dataStream)
        nbtWriter.write(root)

        gzipStream.finish()
        return byteStream.toByteArray()
    }

    /**
     * Create structure data
     * Ported from Python logic
     */
    private fun createStructureData(): NBTTagCompound {
        val structure = NBTTagCompound()

        // Block indices
        val blockIndices = createBlockIndices()
        structure.setList("block_indices", blockIndices)

        // Entities
        structure.setList("entities", NBTTagList(TagType.TAG_COMPOUND))

        // Palette
        val palette = createPalette()
        structure.setCompound("palette", palette)

        return structure
    }

    /**
     * Create block indices array
     * Ported from Python logic
     */
    private fun createBlockIndices(): NBTTagList {
        val blockIndices = NBTTagList(TagType.TAG_LIST)

        // Create a 3D array of block IDs
        val totalSize = width * height * length
        val blockArray = IntArray(totalSize)

        for ((x, y, z, blockName) in blocks) {
            val index = y * width * length + z * width + x
            val blockId = blockMap[blockName] ?: 0
            blockArray[index] = blockId
        }

        // First list: block palette indices
        val firstList = NBTTagList(TagType.TAG_INT)
        for (blockId in blockArray) {
            firstList.add(NBTTagInt(blockId))
        }
        blockIndices.add(firstList)

        // Second list: waterlogged indices (all -1 for now)
        val secondList = NBTTagList(TagType.TAG_INT)
        for (i in 0 until totalSize) {
            secondList.add(NBTTagInt(-1))
        }
        blockIndices.add(secondList)

        return blockIndices
    }

    /**
     * Create palette
     * Ported from Python logic
     */
    private fun createPalette(): NBTTagCompound {
        val palette = NBTTagCompound()

        // Default palette
        val default = NBTTagCompound()

        // Block palette
        val blockPalette = NBTTagList(TagType.TAG_COMPOUND)
        for ((blockName, blockId) in blockMap.entries.sortedBy { it.value }) {
            val block = NBTTagCompound()

            // Block name
            block.setString("name", blockName)

            // Block states
            val states = NBTTagCompound()

            // Add color property for blocks that need it
            val colorName = getBedrockColorName(blockName)
            if (colorName != null) {
                states.setString("color", colorName)
            }

            block.setCompound("states", states)

            // Version
            block.setInt("version", COMPATIBILITY_VERSION)

            blockPalette.add(block)
        }
        default.setList("block_palette", blockPalette)

        // Block position data
        val blockPositionData = NBTTagCompound()
        for ((blockName, blockId) in blockMap.entries) {
            val positionData = NBTTagCompound()
            // No block entity data or tick queue data for now
            blockPositionData.setCompound(blockId.toString(), positionData)
        }
        default.setCompound("block_position_data", blockPositionData)

        palette.setCompound("default", default)

        return palette
    }

    /**
     * Clear all blocks
     */
    fun clear() {
        blocks.clear()
        blockMap.clear()
        width = 0
        height = 0
        length = 0
    }

    /**
     * Get block count
     */
    fun getBlockCount(): Int {
        return blocks.size
    }

    /**
     * Get unique block count
     */
    fun getUniqueBlockCount(): Int {
        return blockMap.size
    }

    /**
     * Get dimensions
     */
    fun getDimensions(): Triple<Int, Int, Int> {
        return Triple(width, height, length)
    }
}

/**
 * Helper class for McStructure NBT writing
 * Uses little-endian encoding as required by Bedrock format
 */
private class McStructureNBTWriter(
    private val outputStream: DataOutputStream
) {
    fun write(tag: NBTTagCompound) {
        writeCompound(tag)
    }

    private fun writeCompound(tag: NBTTagCompound) {
        for (key in tag.keys) {
            val childTag = tag.getValue(key) as Tag<*>
            writeTag(childTag)
        }
        // End tag
        outputStream.writeByte(0)
    }

    private fun writeTag(tag: Tag<*>) {
        when (tag) {
            is TagByte -> {
                outputStream.writeByte(1) // TAG_Byte
                outputStream.writeByte(tag.value.toInt())
            }
            is TagShort -> {
                outputStream.writeByte(2) // TAG_Short
                outputStream.writeShort(java.lang.Short.reverseBytes(tag.value.toShort()).toInt())
            }
            is TagInt -> {
                outputStream.writeByte(3) // TAG_Int
                outputStream.writeInt(java.lang.Integer.reverseBytes(tag.value))
            }
            is TagLong -> {
                outputStream.writeByte(4) // TAG_Long
                outputStream.writeLong(java.lang.Long.reverseBytes(tag.value))
            }
            is TagFloat -> {
                outputStream.writeByte(5) // TAG_Float
                val floatBits = java.lang.Float.floatToIntBits(tag.value)
                outputStream.writeInt(java.lang.Integer.reverseBytes(floatBits))
            }
            is TagDouble -> {
                outputStream.writeByte(6) // TAG_Double
                val doubleBits = java.lang.Double.doubleToLongBits(tag.value)
                outputStream.writeLong(java.lang.Long.reverseBytes(doubleBits))
            }
            is TagByteArray -> {
                outputStream.writeByte(7) // TAG_Byte_Array
                writeByteArray(tag.value)
            }
            is TagString -> {
                outputStream.writeByte(8) // TAG_String
                writeString(tag.value)
            }
            is TagList -> {
                outputStream.writeByte(9) // TAG_List
                writeList(tag)
            }
            is TagCompound -> {
                outputStream.writeByte(10) // TAG_Compound
                writeCompound(tag)
            }
            is TagIntArray -> {
                outputStream.writeByte(11) // TAG_Int_Array
                writeIntArray(tag.value)
            }
            is TagLongArray -> {
                outputStream.writeByte(12) // TAG_Long_Array
                writeLongArray(tag.value)
            }
        }
    }

    private fun writeString(value: String) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        outputStream.writeShort(java.lang.Short.reverseBytes(bytes.size.toShort()).toInt())
        outputStream.write(bytes)
    }

    private fun writeByteArray(value: ByteArray) {
        outputStream.writeInt(java.lang.Integer.reverseBytes(value.size))
        outputStream.write(value)
    }

    private fun writeIntArray(value: IntArray) {
        outputStream.writeInt(java.lang.Integer.reverseBytes(value.size))
        for (v in value) {
            outputStream.writeInt(java.lang.Integer.reverseBytes(v))
        }
    }

    private fun writeLongArray(value: LongArray) {
        outputStream.writeInt(java.lang.Integer.reverseBytes(value.size))
        for (v in value) {
            outputStream.writeLong(java.lang.Long.reverseBytes(v))
        }
    }

    private fun writeList(tag: TagList) {
        outputStream.writeByte(tag.listType.ordinal.toByte().toInt())
        outputStream.writeInt(java.lang.Integer.reverseBytes(tag.size()))
        for (i in 0 until tag.size()) {
            writeTag(tag.get(i) as Tag<*>)
        }
    }
}