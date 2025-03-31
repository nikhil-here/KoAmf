package com.nikhilhere.koamf.encoder

import com.nikhilhere.koamf.util.AMF0ObjectKeysMustBeString
import com.nikhilhere.koamf.util.AMF0StringTooLong
import com.nikhilhere.koamf.amf.DataType
import com.nikhilhere.koamf.amf.ECMAArray
import com.nikhilhere.koamf.amf.Undefined
import com.nikhilhere.koamf.amf.XMLDocument
import com.nikhilhere.koamf.util.Constants.AMF0_MAX_STRING_SIZE
import com.nikhilhere.koamf.util.InvalidAMFDataType
import com.nikhilhere.koamf.util.toUInt16
import com.nikhilhere.koamf.util.toUInt32
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.component1
import kotlin.collections.component2

class Amf0Encoder : Encoder {

    override fun <T> encode(data: T): ByteArray {
        return when (data) {
            is Double -> encodeNumber(data.toDouble())
            is Int -> encodeNumber(data.toDouble())
            is Boolean -> encodeBoolean(data)
            is String -> encodeString(data)
            is Map<*, *> -> encodeObject(data)
            is LocalDateTime -> encodeDate(data)
            is List<*> -> encodeStrictArray(data)
            is ECMAArray -> encodeECMAArray(data)
            is XMLDocument -> encodeXmlDocument(data)
            is Undefined -> encodeUndefined(data)
            null -> encodeNull()
            else -> encodeUnsupported()
        }
    }

    private fun encodeUndefined(data: Undefined): ByteArray {
        return byteArrayOf(DataType.UNDEFINED.marker)
    }

    private fun encodeXmlDocument(document : XMLDocument): ByteArray {
        val utf8Bytes = document.data.toByteArray(Charsets.UTF_8)
        val output = ByteBuffer
            .allocate(5 + utf8Bytes.size)
            .put(DataType.XML_DOCUMENT.marker)
            .put(utf8Bytes.size.toUInt32())
            .put(utf8Bytes)
            .array()
        return output
    }

    private fun encodeECMAArray(data: ECMAArray): ByteArray {
        val output = mutableListOf<Byte>()

        //1. Write the object type marker
        output.add(DataType.ECMA_ARRAY.marker)

        //2. Write the 32-bit big-endian count of properties
        output.addAll(data.properties.size.toUInt32().toList())

        //3. Encode each key value pair
        data.properties.forEach { (key, value) ->

            val keyBytes = key.toByteArray(Charsets.UTF_8)
            //verify that the key fits into a 16-bit length field
            if (keyBytes.size > AMF0_MAX_STRING_SIZE) {
                throw AMF0StringTooLong()
            }

            //write the key as a 16-bit big-endian length.
            output.addAll(keyBytes.size.toUInt16().toList())
            //write the UTF-8 bytes for the key
            output.addAll(keyBytes.toList())

            //Encode the property value and append it
            output.addAll(encode(value).toList())

        }

        //Object End Marker
        output.add(0x00)
        output.add(0x00)
        output.add(DataType.OBJECT_END.marker)

        return output.toByteArray()
    }

    private fun encodeNull(): ByteArray {
        return byteArrayOf(DataType.NULL.marker)
    }

    private fun encodeUnsupported(): ByteArray {
        return byteArrayOf(DataType.UNSUPPORTED.marker)
    }

    private fun encodeStrictArray(data: List<*>): ByteArray {
        val output = mutableListOf<Byte>()

        //1. write the strict array type marker
        output.add(DataType.STRICT_ARRAY.marker)

        //2. write the 32-bit big-endian count of array elements
        output.addAll(data.size.toUInt32().toList())

        //3. encode each element of the array using AMF0 encoding
        data.forEach { output.addAll(encode(it).toList()) }

        return output.toByteArray()
    }

    private fun encodeDate(data: LocalDateTime): ByteArray {
        val output = mutableListOf<Byte>()

        //1. Write the Date type marker (0x0B)
        output.add(DataType.DATE.marker)

        // 2. Convert the LocalDateTime to milliseconds since epoch (UTC).
        // Encode the double value as 8 bytes in big-endian order.
        val millis = data.atZone(ZoneOffset.UTC).toInstant().toEpochMilli().toDouble()
        output.addAll(
            ByteBuffer
                .allocate(8)
                .putLong(millis.toRawBits())
                .array()
                .toList()
        )

        //3. write the 16-bit time zone value, which is set to zero
        output.add(0x00)
        output.add(0x00)

        return output.toByteArray()
    }

    private fun encodeObject(data: Map<*, *>): ByteArray {
        val output = mutableListOf<Byte>()

        //1. Write the object type marker
        output.add(DataType.OBJECT.marker)

        data.forEach { (key, value) ->

            //key must be string
            val keyStr = (key as? String) ?: throw AMF0ObjectKeysMustBeString()
            val keyBytes = keyStr.toByteArray(Charsets.UTF_8)
            //verify that the key fits into a 16-bit length field
            if (keyBytes.size > AMF0_MAX_STRING_SIZE) {
                throw AMF0StringTooLong()
            }

            //write the key as a 16-bit big-endian length.
            output.addAll(keyBytes.size.toUInt16().toList())
            //write the UTF-8 bytes for the key
            output.addAll(keyBytes.toList())

            //Encode the property value and append it
            output.addAll(encode(value).toList())

        }

        //Object End Marker
        output.add(0x00)
        output.add(0x00)
        output.add(DataType.OBJECT_END.marker)

        return output.toByteArray()
    }

    private fun encodeString(data: String): ByteArray {
        val utf8Bytes = data.toByteArray(Charsets.UTF_8)
        // Check if string is within valid length for AMF0 String type.
        if (utf8Bytes.size > AMF0_MAX_STRING_SIZE) {
            return encodeLongString(data)
        }
        //marker (1 byte) + length (2 bytes) + string data.
        val output = ByteBuffer.allocate(1 + 2 + utf8Bytes.size)
            .put(DataType.STRING.marker)
            .put(utf8Bytes.size.toUInt16())
            .put(utf8Bytes)
            .array()
        return output
    }

    private fun encodeLongString(data: String) : ByteArray {
        val utf8Bytes = data.toByteArray(Charsets.UTF_8)
        //marker (1 byte) + length (4 bytes) + string data.
        val output = ByteBuffer.allocate(1 + 4 + utf8Bytes.size)
            .put(DataType.LONG_STRING.marker)
            .put(utf8Bytes.size.toUInt32())
            .put(utf8Bytes)
            .array()
        return output
    }

    private fun encodeBoolean(data: Boolean): ByteArray {
        val output = ByteBuffer.allocate(2)
            .put(DataType.BOOLEAN.marker)
            .put(if (data) 0x01 else 0x00)
            .array()
        return output
    }

    private fun encodeNumber(data: Double): ByteArray {
        val output = ByteBuffer.allocate(9)
            .put(DataType.NUMBER.marker)
            .putDouble(data)
            .array()
        return output
    }

}