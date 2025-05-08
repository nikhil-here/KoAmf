package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUntil
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * AMF0 Date data type.
 *
 * The [AmfDataType.DATE] (marker 0x0B) represents a date/time as:
 * 1. An 8-byte IEEE-754 double (big-endian) of milliseconds since
 *    the Unix epoch (midnight, January 1, 1970 UTC).
 * 2. A 16-bit big-endian time zone offset, which is always 0 in AMF0.
 *
 * @property value The epoch milliseconds since 1970-01-01T00:00:00Z,
 *                  stored as a [Double].
 */
class AmfDate(var value: Double = System.currentTimeMillis().toDouble()) : AmfData() {

    /**
     * Writes the AMF Date payload to the given output stream.
     *
     * Payload layout:
     * 1. 8 bytes: IEEE-754 double (big-endian) of epoch milliseconds (UTC).
     * 2. 2 bytes: signed 16-bit time zone offset (always 0).
     *
     * @param output the [DataOutputStream] to write to.
     */
    override fun writeContent(output: DataOutputStream) {
        val buffer = ByteBuffer.allocate(8)
            .order(ByteOrder.BIG_ENDIAN)
            .putDouble(value)
        output.write(buffer.array())
        output.writeShort(0)
    }

    /**
     * Reads the AMF Date payload from the given input stream.
     *
     * Reads:
     * 1. 8 bytes: IEEE-754 double (big-endian) of epoch milliseconds (UTC).
     * 2. 2 bytes: signed 16-bit time zone offset (ignored).
     *
     * Populates [value] with the epoch milliseconds as a [Double].
     *
     * @param input the [DataInputStream] to read from.
     */
    override fun readContent(input: DataInputStream) {
        val bytes = ByteArray(8)
        input.readUntil(bytes)
        value = ByteBuffer.wrap(bytes)
            .order(ByteOrder.BIG_ENDIAN)
            .double
        input.readShort() // discard timezone offset
    }

    /**
     * @return the AMF0 type marker for Date.
     */
    override fun getType() = AmfDataType.DATE

    /**
     * @return content payload size: 8 bytes for the double + 2 bytes for offset = 10.
     */
    override fun getContentSize(): Int = 10
}
