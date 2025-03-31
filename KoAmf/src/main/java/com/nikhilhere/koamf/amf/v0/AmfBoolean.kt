package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Boolean data type.
 *
 * The [AmfDataType.BOOLEAN] (marker 0x01) represents a Boolean value in AMF.
 * The Boolean is encoded as a single byte following the marker: non zero bytes represent True and zero bytes represent False.
 *
 * @property value The Boolean value represented by this AMF Boolean.
 */
class AmfBoolean(var value: Boolean = false) : AmfData() {

    /**
     * Writes the content (payload) of the AMF Boolean to the specified output stream.
     *
     * This method writes one byte:
     * - 0x01 if [value] is true.
     * - 0x00 if [value] is false.
     *
     * @param output The [DataOutputStream] to which the Boolean data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        output.write(if (value) 0x01 else 0x00)
    }

    /**
     * Reads the content (payload) of the AMF Boolean from the specified input stream.
     *
     * This method reads a single byte from the stream. If the byte is non zero, [value] is set to true;
     * otherwise, it is set to false.
     *
     * @param input The [DataInputStream] from which the Boolean data is read.
     */
    override fun readContent(input: DataInputStream) {
        val value = input.read()
        this@AmfBoolean.value = value != 0
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.BOOLEAN] indicating that this is a Boolean type.
     */
    override fun getType() = AmfDataType.BOOLEAN

    /**
     * Returns the size of the content (payload) for this AMF Boolean.
     *
     * Since an AMF Boolean is encoded as a single byte, the content size is fixed at 1 byte.
     *
     * @return The size of the payload (1 byte).
     */
    override fun getContentSize(): Int = 1
}