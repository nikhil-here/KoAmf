package com.nikhilhere.koamf.amf

/**
 * Data Types
 * There are 16 Data Types in AMF0, each data type is represented by a marker which is one byte in length.
 * This enum represents the various AMF0 data types and their associated type markers.
 * For the complete specification, see:
 * [AMF0 File Format Specification](https://rtmp.veriskope.com/pdf/amf0-file-format-specification.pdf)
 */
enum class AmfDataType(val marker: Byte) {
    /**
     * NUMBER type.
     *
     * Encoded as an 8-byte IEEE-754 double precision floating point value in network byte order.
     */
    NUMBER(0x00),

    /**
     * BOOLEAN type.
     *
     * Encoded as a single byte where 0 denotes false and any non-zero value (typically 1) denotes true.
     */
    BOOLEAN(0x01),

    /**
     * STRING type.
     *
     * Uses a 16-bit length header followed by a UTF-8 encoded string.
     */
    STRING(0x02),

    /**
     * OBJECT type.
     *
     * Represents an anonymous ActionScript object encoded as a series of property name/value pairs.
     */
    OBJECT(0x03),

    /**
     * MOVIE_CLIP type.
     *
     * Reserved and not supported in AMF0 (reserved for future use).
     */
    MOVIE_CLIP(0x04),

    /**
     * NULL type.
     *
     * Represents a null value; no additional data is encoded.
     */
    NULL(0x05),

    /**
     * UNDEFINED type.
     *
     * Represents an undefined value; no additional data is encoded.
     * its an Invalid value in AMF and means that the value exists but has not been defined.
     */
    UNDEFINED(0x06),

    /**
     * REFERENCE type.
     * Note on Reference (0x07) Support in KOAMF:
     * In AMF0, the REFERENCE type (marker 0x07) is used to indicate that an object which has already been
     * serialized appears again in the object graph. Instead of re-encoding the full object, a 16-bit big-endian
     * index is written that refers back to the original object's position in the reference table. This mechanism
     * is used to avoid redundant data and to preserve object identity, particularly when dealing with circular references.
     *
     * However, many modern applications tend to use simpler data structures and do not rely on object references
     * for repeated objects. As a result, KOAMF does not provide support for encoding or decoding the REFERENCE type.
     * In KOAMF, each object is fully serialized, and the reference optimization is omitted.
     */
    REFERENCE(0x07),

    /**
     * ECMA_ARRAY type.
     *
     * Represents an associative array (i.e., an array with non-ordinal keys).
     * Encoded with a 32-bit count followed by key/value pairs, and terminated by an empty string.
     */
    ECMA_ARRAY(0x08),

    /**
     * OBJECT_END marker.
     *
     * Used to denote the end of an object or associative array.
     * Encoded as an empty string followed by this marker.
     */
    OBJECT_END(0x09),

    /**
     * STRICT_ARRAY type.
     *
     * Represents an array containing only ordinal (numeric) indices.
     * Encoded with a 32-bit array count followed by a series of values.
     */
    STRICT_ARRAY(0x0A),

    /**
     * DATE type.
     *
     * Encoded as an 8-byte IEEE-754 double representing the number of milliseconds since the epoch,
     * followed by a 16-bit time zone value (which should be set to 0).
     */
    DATE(0x0B),

    /**
     * LONG_STRING type.
     *
     * Used for strings that require more than 65,535 bytes when UTF-8 encoded.
     * Uses a 32-bit length header instead of a 16-bit header.
     */
    LONG_STRING(0x0C),

    /**
     * UNSUPPORTED type.
     *
     * Indicates that the type cannot be serialized; no additional data is encoded.
     */
    UNSUPPORTED(0x0D),

    /**
     * RECORDSET type.
     *
     * Reserved for future use and not supported.
     */
    RECORDSET(0x0E),

    /**
     * XML_DOCUMENT type.
     *
     * Represents an XML document. Encoded as a long string using UTF-8.
     */
    XML_DOCUMENT(0x0F),

    /**
     * TYPED_OBJECT type.
     *
     * It's used for objects that have a registered class alias, meaning that
     * the object includes type information (i.e. a class name) along with its properties. In practice,
     * however, this type is rarely used since most applications rely on dynamic objects or associative
     * arrays (ECMA Arrays) that do not include explicit type metadata.
     *
     * For this reason, KoAmf does not implement support for the TYPED_OBJECT type.
     */
    TYPED_OBJECT(0x10),
}