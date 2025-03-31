package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Null data type.
 *
 * The [AmfDataType.NULL] (marker 0x05) is used to represent a null value in AMF.
 * A null value is encoded solely by its type marker with no additional content.
 */
class AmfNull : AmfData() {

    /**
     * Writes the content (payload) of the AMF Null type.
     *
     * Since a null value has no content, nothing is written to the output stream.
     *
     * @param output The [DataOutputStream] to which any payload would be written (none in this case).
     */
    override fun writeContent(output: DataOutputStream) {
        // No additional content for null.
    }

    /**
     * Reads the content (payload) of the AMF Null type.
     *
     * Since a null value has no content, nothing is read from the input stream.
     *
     * @param input The [DataInputStream] from which any payload would be read (none in this case).
     */
    override fun readContent(input: DataInputStream) {
        // No additional content for null.
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.NULL] indicating that this is a Null type.
     */
    override fun getType() = AmfDataType.NULL

    /**
     * Returns the size of the content (payload) for this AMF Null type.
     *
     * Since a null value is encoded solely by its marker, the content size is 0.
     *
     * @return The payload size (0 bytes).
     */
    override fun getContentSize(): Int = 0
}
