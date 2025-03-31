package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUntil
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

/**
 * AMF Number data type.
 *
 * The [AmfDataType.NUMBER] (marker 0x00) is used to represent a double-precision floating point value.
 * The value is encoded in 8 bytes in IEEE-754 format, using big-endian (network) byte order.
 *
 * @property value The numeric value represented by this AMF Number.
 */
class AmfNumber(var value: Double = 0.0) : AmfData() {

    /**
     * Writes the content (payload) of the AMF Number to the specified output stream.
     *
     * This method converts the [value] into its 8-byte IEEE-754 representation (using [Double.toRawBits])
     * and writes it to the output stream in big-endian order.
     *
     * @param output The [DataOutputStream] to which the numeric data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        val byteBuffer = ByteBuffer.allocate(getContentSize()).putLong(value.toRawBits())
        output.write(byteBuffer.array())
    }

    /**
     * Reads the content (payload) of the AMF Number from the specified input stream.
     *
     * This method reads 8 bytes from the input stream, reconstructs the 64-bit representation,
     * and converts it back to a double-precision floating point value using [Double.fromBits].
     *
     * @param input The [DataInputStream] from which the numeric data is read.
     */
    override fun readContent(input: DataInputStream) {
        val payload = ByteArray(getContentSize())
        input.readUntil(payload)
        val value = ByteBuffer.wrap(payload).long
        this@AmfNumber.value = Double.fromBits(value)
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.NUMBER] indicating this is a Number type.
     */
    override fun getType() = AmfDataType.NUMBER

    /**
     * Returns the size of the content (payload) for this AMF Number.
     *
     * Since an AMF Number is encoded as an 8-byte IEEE-754 double, the content size is fixed at 8 bytes.
     *
     * @return The size of the payload (8 bytes).
     */
    override fun getContentSize(): Int = 8
}