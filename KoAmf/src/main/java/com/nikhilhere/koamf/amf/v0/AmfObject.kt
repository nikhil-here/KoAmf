package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream


/**
 * AMF Object data type.
 *
 * The [AmfDataType.OBJECT] (marker 0x03) represents an anonymous object in AMF.
 * An AMF Object is encoded as a series of key-value pairs. Each key is encoded as a UTF‑8 string
 * with a 16‑bit big-endian length header (using an [AmfString]), and each value is encoded according
 * to its own AMF type. The object is terminated by an object‑end marker, which consists of an empty
 * key (two zero bytes) followed by the [AmfDataType.OBJECT_END] marker (0x09).
 *
 * During reading, the helper method [AmfData.readAmfData] (defined in the companion object of [AmfData])
 * is used to determine the type of each property value by reading the type marker and instantiating the
 * appropriate [AmfData] subclass.
 *
 * The [writeContent] method iterates over all key-value pairs, writing the key and then the value
 * (first writing its marker and then its content), before finally writing the object-end marker.
 *
 * The [readContent] method uses a mark/reset mechanism on the input stream to peek ahead and detect
 * the object-end marker. It repeatedly reads a property key (as an [AmfString]) and then uses [readAmfData]
 * to read the corresponding value, storing the pair in the [value] map.
 *
 * @property value A mutable map containing the object's properties, where keys are represented as [AmfString]
 *                and values as [AmfData].
 */
class AmfObject(var value: MutableMap<AmfString, AmfData> = mutableMapOf()) : AmfData() {

    /**
     * Writes the content (payload) of the AMF Object to the specified output stream.
     *
     * For each property:
     * - The key is written using its [AmfString.writeContent] method.
     * - The value's marker is written using [AmfData.writeMarker], followed by the value's content
     *   using [AmfData.writeContent].
     * Finally, the object is terminated by writing the object-end marker (an empty key followed by 0x09).
     *
     * @param output The [DataOutputStream] to which the object data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        value.forEach { k, v ->
            k.writeContent(output)
            v.writeMarker(output)
            v.writeContent(output)
        }
        // Write the object-end marker.
        val objectEnd = AmfObjectEnd(endOfObject = true)
        objectEnd.writeContent(output)
    }

    /**
     * Reads the content (payload) of the AMF Object from the specified input stream.
     *
     * This method processes key-value pairs until the object-end marker is encountered.
     * It uses a mark/reset mechanism on the input stream to check for the object-end marker:
     * - The stream is marked with a limit equal to the size of the object-end marker.
     * - [AmfObjectEnd.readContent] is called to attempt to read the end marker.
     * - If the marker is not found, the stream is reset, and a property is read:
     *   - A new [AmfString] is created to read the key.
     *   - The property value is read using the helper function [AmfData.readAmfData].
     * The key and value are then stored in the [value] map.
     *
     * @param input The [DataInputStream] from which the object data is read.
     */
    override fun readContent(input: DataInputStream) {
        value.clear()
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
                this@AmfObject.value[k] = v
            }
        }
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.OBJECT] indicating that this element is an Object.
     */
    override fun getType() = AmfDataType.OBJECT

    /**
     * Calculates and returns the size of the content (payload) for this AMF Object.
     *
     * The total content size is determined by summing:
     * - The size of each property key (as determined by [AmfString.getContentSize]).
     * - The size of each property value (as determined by [AmfData.getContentSize]).
     * - Plus the size of the object-end marker.
     *
     * Note: This method assumes that a helper function, such as [getAmfValueSize], is available
     * to calculate the size of each encoded value.
     *
     * @return The total payload size in bytes.
     */
    override fun getContentSize(): Int {
        var size = 0
        value.forEach { k, v ->
            size += k.getContentSize()
            size += v.getContentSize()
        }
        // Add the size for the object-end marker.
        size += AmfObjectEnd().getContentSize()
        return size
    }
}
