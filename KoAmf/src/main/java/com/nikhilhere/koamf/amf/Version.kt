package com.nikhilhere.koamf.amf

/**
 * Represents the two versions of the Action Message Format (AMF)
 *
 * - **AMF0:** This version encodes data types such as numbers, booleans, strings, objects, and arrays
 *   with fixed type markers.
 *
 * - **AMF3:** It provides improvements like reference-based encoding of strings, objects, and traits,
 *   support for additional data types (e.g., Vector and Dictionary), and a more efficient encoding
 *   scheme using variable length integers.
 *
 * For further details, refer to the AMF0 specification:
 * [AMF0 Specification](https://rtmp.veriskope.com/pdf/amf0-file-format-specification.pdf)
 * [AMF3 Specification](https://rtmp.veriskope.com/pdf/amf3-file-format-spec.pdf)
 */
enum class Version {
    AMF0,
    AMF3
}