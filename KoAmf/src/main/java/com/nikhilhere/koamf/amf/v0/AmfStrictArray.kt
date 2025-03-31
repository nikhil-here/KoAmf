package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.toUInt32
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Strict Array data type.
 *
 * The [AmfDataType.STRICT_ARRAY] (marker 0x0A) represents an array containing only ordinal indices.
 * A strict array is encoded as follows:
 * 1. A 32-bit big-endian count header indicating the number of elements.
 * 2. A sequence of elements, where each element is encoded by writing its type marker followed by its payload.
 *
 * When reading, the helper method [AmfData.readAmfData] is used to determine the type of each element and
 * instantiate the appropriate [AmfData] subclass.
 *
 * @property value A mutable list containing the array elements.
 */
class AmfStrictArray(var value: MutableList<AmfData> = mutableListOf()) : AmfData() {

    /**
     * Writes the content (payload) of the AMF Strict Array to the specified output stream.
     *
     * The payload is structured as follows:
     * 1. A 32-bit big-endian count header representing the number of elements in the array.
     * 2. For each element:
     *    - The element's type marker is written via its [AmfData.writeMarker] method.
     *    - The element's payload is written via its [AmfData.writeContent] method.
     *
     * @param output The [DataOutputStream] to which the strict array data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        // Write the 32-bit big-endian count header.
        output.write(value.size.toUInt32())
        // Write each element: first its marker, then its content.
        value.forEach { element ->
            element.writeMarker(output)
            element.writeContent(output)
        }
    }

    /**
     * Reads the content (payload) of the AMF Strict Array from the specified input stream.
     *
     * The method reads:
     * 1. A 32-bit big-endian count header to determine the number of elements.
     * 2. For each element, it reads the type marker and then uses [AmfData.readAmfData]
     *    to instantiate and populate the appropriate [AmfData] subclass.
     *
     * The elements are stored in the [value] list.
     *
     * @param input The [DataInputStream] from which the strict array data is read.
     */
    override fun readContent(input: DataInputStream) {
        value.clear()
        // Read the count header (number of elements).
        val count = input.readInt()  // Assumes big-endian order.
        repeat(count) {
            val element = readAmfData(input)
            value.add(element)
        }
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.STRICT_ARRAY] indicating that this element is a Strict Array.
     */
    override fun getType() = AmfDataType.STRICT_ARRAY

    /**
     * Calculates and returns the size of the content (payload) for this AMF Strict Array.
     *
     * The total size is computed as:
     * - 4 bytes for the 32-bit count header.
     * - For each element, 1 byte for its type marker plus the size of its content.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        var size = 4 // 4 bytes for the count header.
        value.forEach { element ->
            size += 1 // 1 byte for the element's type marker.
            size += element.getContentSize() // The size of the element's payload.
        }
        return size
    }
}
