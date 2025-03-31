package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Undefined data type.
 *
 * The [AmfDataType.UNDEFINED] (marker 0x06) represents an undefined value in AMF.
 * An undefined value is encoded solely by its type marker, with no additional payload.
 */
class AmfUndefined : AmfData() {

    /**
     * Writes the content (payload) of the AMF Undefined type to the specified output stream.
     *
     * Since an undefined value has no content, nothing is written to the output stream.
     *
     * @param output The [DataOutputStream] to which any payload would be written (none in this case).
     */
    override fun writeContent(output: DataOutputStream) {
        // No additional content for undefined.
    }

    /**
     * Reads the content (payload) of the AMF Undefined type from the specified input stream.
     *
     * Since an undefined value has no content, nothing is read from the input stream.
     *
     * @param input The [DataInputStream] from which any payload would be read (none in this case).
     */
    override fun readContent(input: DataInputStream) {
        // No additional content for undefined.
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.UNDEFINED] indicating that this is an Undefined type.
     */
    override fun getType() = AmfDataType.UNDEFINED

    /**
     * Returns the size of the content (payload) for this AMF Undefined type.
     *
     * Since an undefined value is encoded solely by its marker, the content size is 0 bytes.
     *
     * @return The payload size (0 bytes).
     */
    override fun getContentSize(): Int = 0
}
