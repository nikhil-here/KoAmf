package com.nikhilhere.koamf.util

/**
 * Exception thrown when attempting to encode an AMF object whose keys are not strings.
 *
 * In AMF0, object keys must be encoded as strings. This exception is raised when a non-string key
 * is encountered during the encoding process.
 */
class AMFObjectKeysMustBeString : IllegalStateException("AMF0 object keys must be string")

/**
 * Exception thrown when a string is too long to be encoded using the AMF0 string format.
 *
 * In AMF0, strings are encoded with a 16-bit length header, limiting the maximum size to 65,535 [Constants.AMF0_MAX_STRING_SIZE] bytes.
 * If the provided string exceeds this limit, this exception is raised.
 *
 * @param value The string value that exceeded the AMF0 encoding limit, if available.
 */
class AMF0StringTooLong(value: String? = null) : IllegalStateException(
    "$value String is too long for AMF0 encoding use AMF3 instead"
)

/**
 * Exception thrown when the provided AMF data type is not supported by the KoAmf library.
 *
 * This exception is used to indicate that the requested AMF data type (by its name) has not yet been implemented.
 *
 * @param type The name of the AMF data type that is not supported.
 */
class UnsupportedAMFDataType(type: String) : IllegalArgumentException(
    "Type $type is not supported by KoAmf yet"
)

/**
 * Exception thrown when a given Kotlin data type cannot be mapped to any supported AMF data type.
 *
 * This exception is raised when the KoAmf library encounters a Kotlin data type that does not have a corresponding
 * AMF representation.
 *
 * @param type The name of the unsupported Kotlin data type.
 */
class UnsupportedKotlinDataTypeException(type: String) : IllegalArgumentException(
    "Unsupported Kotlin data type: '$type'. No corresponding AMF data type exists."
)
