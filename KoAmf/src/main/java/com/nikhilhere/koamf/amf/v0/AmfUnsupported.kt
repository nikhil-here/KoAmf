package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Unsupported data type.
 *
 * The [AmfDataType.UNSUPPORTED] (marker 0x0D) is used to indicate a data type that is not supported
 * in AMF. An unsupported value is encoded solely by its type marker with no additional payload.
 */
class AmfUnsupported : AmfData() {

    /**
     * Writes the content (payload) of the AMF Unsupported type to the specified output stream.
     *
     * Since an unsupported value has no additional content beyond its marker, nothing is written.
     *
     * @param output The [DataOutputStream] to which any payload would be written (none in this case).
     */
    override fun writeContent(output: DataOutputStream) {
        // No additional content for unsupported type.
    }

    /**
     * Reads the content (payload) of the AMF Unsupported type from the specified input stream.
     *
     * Since an unsupported value has no additional content, nothing is read from the input stream.
     *
     * @param input The [DataInputStream] from which any payload would be read (none in this case).
     */
    override fun readContent(input: DataInputStream) {
        // No additional content for unsupported type.
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.UNSUPPORTED] indicating that this is an Unsupported type.
     */
    override fun getType() = AmfDataType.UNSUPPORTED

    /**
     * Returns the size of the content (payload) for this AMF Unsupported type.
     *
     * Since an unsupported value is encoded solely by its marker, the content size is 0 bytes.
     *
     * @return The payload size (0 bytes).
     */
    override fun getContentSize(): Int = 0
}
