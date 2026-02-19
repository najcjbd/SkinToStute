package com.skintostatue.android.core

/**
 * Custom exception classes for Skin to Statue converter
 * Ported from Python exceptions.py
 * Includes all exception types and error codes
 */

/**
 * Base exception for all Skin to Statue converter errors
 */
open class SkinStatueException(
    message: String,
    val errorCode: String? = null
) : Exception(message) {
    override fun toString(): String {
        return if (errorCode != null) {
            "[$errorCode] $message"
        } else {
            message
        }
    }
}

/**
 * Exception raised when skin loading fails
 */
class SkinLoadException(message: String, errorCode: String? = "SKIN_003") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when skin format is invalid
 */
class SkinFormatException(message: String, errorCode: String? = "SKIN_002") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when configuration is invalid
 */
class ConfigurationException(message: String, errorCode: String? = "CFG_001") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when armor processing fails
 */
class ArmorException(message: String, errorCode: String? = "ARM_001") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when color matching fails
 */
class ColorException(message: String, errorCode: String? = "CLR_002") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when schematic generation fails
 */
class SchematicException(message: String, errorCode: String? = "SCH_001") 
    : SkinStatueException(message, errorCode)

/**
 * Exception raised when network operations fail
 */
class NetworkException(message: String, errorCode: String? = "NET_002") 
    : SkinStatueException(message, errorCode)

/**
 * Error code constants
 * Ported from Python ErrorCode class
 */
object ErrorCode {
    // Skin errors
    const val SKIN_NOT_FOUND = "SKIN_001"
    const val SKIN_INVALID_FORMAT = "SKIN_002"
    const val SKIN_LOAD_FAILED = "SKIN_003"
    const val SKIN_API_FAILED = "SKIN_004"
    
    // Configuration errors
    const val CONFIG_INVALID = "CFG_001"
    const val CONFIG_MISSING = "CFG_002"
    const val CONFIG_PARSE_FAILED = "CFG_003"
    
    // Armor errors
    const val ARMOR_INVALID = "ARM_001"
    const val ARMOR_LOAD_FAILED = "ARM_002"
    const val ARMOR_DYE_FAILED = "ARM_003"
    
    // Color errors
    const val COLOR_INVALID = "CLR_001"
    const val COLOR_MATCH_FAILED = "CLR_002"
    
    // Schematic errors
    const val SCHEMATIC_SAVE_FAILED = "SCH_001"
    const val SCHEMATIC_INVALID = "SCH_002"
    
    // Network errors
    const val NETWORK_TIMEOUT = "NET_001"
    const val NETWORK_CONNECTION_FAILED = "NET_002"
}