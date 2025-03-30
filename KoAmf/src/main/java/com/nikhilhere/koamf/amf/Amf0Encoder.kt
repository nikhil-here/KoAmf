package com.nikhilhere.koamf.amf

import java.nio.ByteBuffer
import java.time.LocalDateTime
import kotlin.jvm.javaClass

class Amf0Encoder : Encoder {

    override fun <T> encode(data: T) {
        when (data) {
            is Double -> encodeNumber(data.toDouble())
            is Int -> encodeNumber(data.toDouble())
            is Boolean -> encodeBoolean(data)
            is String -> encodeString(data)
            is Map<*, *> -> encodeObject(data)
            is LocalDateTime -> encodeDate(data)
            is List<*> -> encodeStrictArray(data)
            else -> throw IllegalArgumentException("Type ${(data as Any).javaClass} not valid AMF Data Type")
        }
    }

    private fun encodeStrictArray(data: List<*>): ByteArray {
        return byteArrayOf()
    }

    private fun encodeDate(data: LocalDateTime): ByteArray {
        return byteArrayOf()
    }

    private fun encodeObject(data: Map<*, *>): ByteArray {
        return byteArrayOf()
    }

    private fun encodeString(data: String): ByteArray {
        val utf8Bytes = data.toByteArray(Charsets.UTF_8)
        // Check if string is within valid length for AMF0 String type.
        if (utf8Bytes.size > 0xFFFF) {
            throw IllegalArgumentException("String is too long for AMF0 String encoding")
        }
        //marker (1 byte) + length (2 bytes) + string data.
        val output = ByteBuffer.allocate(1 + 2 + utf8Bytes.size)
            .put(DataType.STRING.marker)
            .put(utf8Bytes.size.toUInt16())
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