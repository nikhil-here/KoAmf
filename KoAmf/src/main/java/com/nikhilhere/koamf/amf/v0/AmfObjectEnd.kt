package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUntil
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * AMF Object End marker.
 *
 * In AMF0, an object is terminated by an object-end marker.
 * This marker is encoded as an empty string (represented by two zero bytes) followed by the marker 0x09.
 *
 * This class represents the end-of-object marker, which has no additional payload.
 */
class AmfObjectEnd(var endOfObject: Boolean = false) : AmfData() {

    val endSequence = byteArrayOf(
        0x00,   // First zero byte of the empty string.
        0x00,   // Second zero byte of the empty string.
        getType().marker // The object-end marker.
    )


    /**
     * Writes the object-end marker to the specified output stream.
     *
     * The marker is written as two zero bytes (for an empty string) followed by 0x09.
     *
     * @param output The [DataOutputStream] to which the marker is written.
     */
    override fun writeContent(output: DataOutputStream) {
        output.write(endSequence)
    }

    /**
     * Reads the object-end marker from the specified input stream.
     *
     * This method reads three bytes from the input stream:
     * - Two zero bytes representing an empty string.
     * - The marker 0x09.
     *
     * @param input The [DataInputStream] from which the marker is read.
     */
    override fun readContent(input: DataInputStream) {
        val payload = ByteArray(getContentSize())
        input.readUntil(payload)
        endOfObject = payload contentEquals endSequence
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.OBJECT_END] indicating that this element is an Object End marker.
     */
    override fun getType() = AmfDataType.OBJECT_END

    /**
     * Returns the size of the content (payload) for this AMF Object End marker.
     *
     * The payload consists of two zero bytes plus one marker byte, totaling 3 bytes.
     *
     * @return The size of the payload (3 bytes).
     */
    override fun getContentSize(): Int = endSequence.size
}
