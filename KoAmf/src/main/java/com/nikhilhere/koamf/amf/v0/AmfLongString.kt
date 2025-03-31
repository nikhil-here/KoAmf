package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUInt32
import com.nikhilhere.koamf.util.readUntil
import com.nikhilhere.koamf.util.toUInt32
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Long String data type.
 *
 * The [AmfDataType.LONG_STRING] (marker 0x0C) is used to represent a UTF-8 string whose length exceeds 65,535 bytes.
 * In this format, the string payload is preceded by a 32-bit (4-byte) big-endian length header that specifies the number of bytes
 * in the UTF-8 encoded string.
 *
 * Note:
 * - Use [AmfString] for strings with a length that fits in 16 bits.
 * - [AmfLongString] should be used only when the UTF-8 byte length of the string exceeds 65,535 bytes.
 *
 * @property value The string value represented by this AMF Long String.
 */
class AmfLongString(var value: String = "") : AmfData() {

    /**
     * Writes the content (payload) of the AMF Long String to the specified output stream.
     *
     * This method writes:
     * 1. A 32-bit big-endian length header that represents the number of bytes in the UTF-8 encoded string.
     * 2. The UTF-8 encoded string payload.
     *
     * @param output The [DataOutputStream] to which the long string data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        val utf8Bytes = value.toByteArray(Charsets.UTF_8)
        // Write the 32-bit big-endian length header.
        output.write(utf8Bytes.size.toUInt32())
        // Write the UTF-8 encoded string payload.
        output.write(utf8Bytes)
    }

    /**
     * Reads the content (payload) of the AMF Long String from the specified input stream.
     *
     * This method reads:
     * 1. A 32-bit big-endian length header to determine the payload size.
     * 2. The UTF-8 encoded string payload, and updates [value] with the resulting string.
     *
     * @param input The [DataInputStream] from which the long string data is read.
     */
    override fun readContent(input: DataInputStream) {
        // Read the 32-bit big-endian length header.
        val payloadSize = input.readUInt32()
        val payload = ByteArray(payloadSize)
        // Read exactly 'length' bytes into the payload.
        input.readUntil(payload)
        // Convert the payload from UTF-8 and update the data property.
        value = String(payload, Charsets.UTF_8)
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.LONG_STRING] indicating that this is a Long String type.
     */
    override fun getType() = AmfDataType.LONG_STRING

    /**
     * Returns the size of the content (payload) for this AMF Long String.
     *
     * The content size includes:
     * - 4 bytes for the 32-bit length header.
     * - The number of bytes in the UTF-8 encoded string.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        return 4 + value.toByteArray(Charsets.UTF_8).size
    }
}
