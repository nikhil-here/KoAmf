package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.AMF0StringTooLong
import com.nikhilhere.koamf.util.Constants.AMF0_MAX_STRING_SIZE
import com.nikhilhere.koamf.util.readUInt16
import com.nikhilhere.koamf.util.readUntil
import com.nikhilhere.koamf.util.toUInt16
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF String data type implementation.
 *
 * The [AmfDataType.STRING] (marker 0x02) is used to encode a UTF-8 string with a 16-bit length header.
 * The first 2 bytes of the payload represent the length (in bytes) of the string, followed by the UTF-8 encoded string itself.
 *
 * Note:
 * - The 16-bit length header limits the maximum size of an AMF String to 65,535 bytes.
 * - For longer strings, the [AmfDataType.LONG_STRING] should be used instead.
 *
 * @property value The string value represented by this AMF string.
 */
class AmfString(var value: String = "") : AmfData() {

    override fun writeContent(output: DataOutputStream) {
        val utf8Bytes = value.toByteArray(Charsets.UTF_8)
        if (utf8Bytes.size > AMF0_MAX_STRING_SIZE) {
            throw AMF0StringTooLong(value = value)
        }
        // Write the 16-bit big-endian length header
        output.write(utf8Bytes.size.toUInt16())
        // Write the UTF-8 encoded string payload
        output.write(utf8Bytes)
    }

    override fun readContent(input: DataInputStream) {
        // Read the 16-bit big-endian length header to determine the payload size
        val payloadSize = input.readUInt16()
        val payload = ByteArray(payloadSize)
        // Read exactly payloadSize bytes into the byte array
        input.readUntil(payload)
        // Decode the payload from UTF-8 and update the data property
        value = String(payload, Charsets.UTF_8)
    }

    override fun getType() = AmfDataType.STRING

    /**
     * Calculates and returns the total size of the content (payload) for this AMF string.
     *
     * The content size includes:
     * - 2 bytes for the 16-bit length header
     * - The number of bytes in the UTF-8 encoded string.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        return 2 + value.toByteArray(Charsets.UTF_8).size
    }
}