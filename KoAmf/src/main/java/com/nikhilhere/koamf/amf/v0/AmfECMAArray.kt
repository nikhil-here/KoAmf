package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.toUInt32
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream

/**
 * AMF ECMA Array data type.
 *
 * The [AmfDataType.ECMA_ARRAY] (marker 0x08) represents an associative array in AMF.
 * It is used to encode arrays with non-ordinal (string) keys. The ECMA Array is encoded as:
 *
 * 1. A 32-bit big-endian unsigned integer that indicates the number of key-value pairs.
 *    (This count is informative; the actual reading of properties continues until an object-end marker is encountered.)
 * 2. A sequence of key-value pairs, where each key is encoded as an [AmfString] (a UTF‑8 string with a 16‑bit length header)
 *    and each value is encoded according to its AMF type (using its marker and payload).
 * 3. The array is terminated by an object-end marker, which is an empty key (two zero bytes)
 *    followed by the [AmfDataType.OBJECT_END] marker (0x09).
 *
 * When reading, the helper method [AmfData.readAmfData] is used to determine and instantiate the correct
 * [AmfData] subclass for each property value.
 *
 * @property value A mutable map containing the ECMA Array's properties, where keys are represented as [AmfString]
 *                and values as [AmfData].
 */
class AmfECMAArray(var value: MutableMap<AmfString, AmfData> = mutableMapOf()) : AmfData() {

    /**
     * Writes the content (payload) of the AMF ECMA Array to the specified output stream.
     *
     * The output is structured as follows:
     * 1. A 32-bit big-endian count header representing the number of key-value pairs.
     * 2. For each key-value pair:
     *    - The key is written using its [AmfString.writeContent] method.
     *    - The value is written by first writing its type marker via [AmfData.writeMarker] and then
     *      its payload via [AmfData.writeContent].
     * 3. The object-end marker is written at the end to denote the termination of the array.
     *
     * @param output The [DataOutputStream] to which the ECMA Array data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        // Write the 32-bit count header.
        output.write(value.size.toUInt32())
        // Write each key-value pair.
        value.forEach { (k, v) ->
            k.writeContent(output)
            v.writeMarker(output)
            v.writeContent(output)
        }
        // Write the object-end marker.
        val objectEnd = AmfObjectEnd(endOfObject = true)
        objectEnd.writeContent(output)
    }

    /**
     * Reads the content (payload) of the AMF ECMA Array from the specified input stream.
     *
     * The method processes the following:
     * 1. Reads a 32-bit big-endian count header which specifies the number of key-value pairs.
     *    (Note that this count is informative; reading continues until the object-end marker is encountered.)
     * 2. Uses a mark/reset mechanism to peek ahead in the stream for the object-end marker.
     * 3. For each property, reads the key using an [AmfString] and then reads the corresponding value
     *    using the helper method [AmfData.readAmfData].
     * 4. The loop terminates when an object-end marker (an empty key followed by 0x09) is detected.
     *
     * @param input The [DataInputStream] from which the ECMA Array data is read.
     */
    override fun readContent(input: DataInputStream) {
        value.clear()
        // Read the 32-bit count header (informative).
        val count = input.readInt()
        val objectEnd = AmfObjectEnd()
        val markInputStream: InputStream =
            if (input.markSupported()) input else BufferedInputStream(input)
        while (!objectEnd.endOfObject) {
            markInputStream.mark(objectEnd.getContentSize())
            objectEnd.readContent(input)
            if (!objectEnd.endOfObject) {
                markInputStream.reset()
                val k = AmfString()
                k.readContent(input)
                val v = readAmfData(input)
                this@AmfECMAArray.value[k] = v
            }
        }
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.ECMA_ARRAY] indicating that this element is an ECMA Array.
     */
    override fun getType() = AmfDataType.ECMA_ARRAY

    /**
     * Calculates and returns the size of the content (payload) for this AMF ECMA Array.
     *
     * The total size is computed as the sum of:
     * 1. 4 bytes for the 32-bit count header.
     * 2. The size of each key-value pair:
     *    - The size of the key as determined by [AmfString.getContentSize].
     *    - The size of the value as determined by [AmfData.getContentSize].
     * 3. 3 bytes for the object-end marker.
     *
     * Note: The size of each value depends on its AMF encoding, and the calculation assumes that a helper
     * function (e.g., [getAmfValueSize]) is available for this purpose.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        var size = 4 // 4 bytes for the count header.
        value.forEach { (k, v) ->
            size += k.getContentSize()
            size += v.getContentSize()
        }
        // Add 3 bytes for the object-end marker.
        size += AmfObjectEnd().getContentSize()
        return size
    }
}
