package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUInt32
import com.nikhilhere.koamf.util.readUntil
import com.nikhilhere.koamf.util.toUInt32
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF XML Document data type.
 *
 * The [AmfDataType.XML_DOCUMENT] (marker 0x0F) is used to represent an XML document in AMF.
 * The XML document is encoded as a UTF-8 string preceded by a 32-bit big-endian length header that
 * specifies the number of bytes in the XML payload.
 *
 * @property value The XML document content represented as a String.
 */
class AmfXmlDocument(var value: String = "") : AmfData() {

    /**
     * Writes the content (payload) of the AMF XML Document to the specified output stream.
     *
     * This method writes:
     * 1. A 32-bit big-endian length header indicating the number of bytes in the UTF-8 encoded XML.
     * 2. The UTF-8 encoded XML payload.
     *
     * @param output The [DataOutputStream] to which the XML document data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        val utf8Bytes = value.toByteArray(Charsets.UTF_8)
        output.write(utf8Bytes.size.toUInt32())
        output.write(utf8Bytes)
    }

    /**
     * Reads the content (payload) of the AMF XML Document from the specified input stream.
     *
     * This method reads:
     * 1. A 32-bit big-endian length header to determine the byte length of the XML payload.
     * 2. The corresponding number of bytes containing the UTF-8 encoded XML.
     * The [value] property is then updated with the XML content.
     *
     * @param input The [DataInputStream] from which the XML document data is read.
     */
    override fun readContent(input: DataInputStream) {
        val payloadSize = input.readUInt32()
        val payload = ByteArray(payloadSize)
        input.readUntil(payload)
        value = String(payload, Charsets.UTF_8)
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.XML_DOCUMENT] indicating that this is an XML Document type.
     */
    override fun getType() = AmfDataType.XML_DOCUMENT

    /**
     * Returns the size of the content (payload) for this AMF XML Document.
     *
     * The content size includes:
     * - 4 bytes for the 32-bit length header.
     * - The number of bytes in the UTF-8 encoded XML data.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        return 4 + value.toByteArray(Charsets.UTF_8).size
    }
}
