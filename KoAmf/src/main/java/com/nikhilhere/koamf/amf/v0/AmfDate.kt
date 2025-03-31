package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.AmfDataType
import com.nikhilhere.koamf.util.readUntil
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * AMF Date data type.
 *
 * The [AmfDataType.DATE] (marker 0x0B) is used to represent a date/time value in AMF.
 * The value is encoded as an 8-byte IEEE-754 double representing the number of milliseconds
 * since midnight, January 1, 1970 (UTC), followed by a 16-bit big-endian time zone offset,
 * which is typically zero.
 *
 * @property value The [LocalDateTime] value represented by this AMF Date.
 */
class AmfDate(var value: LocalDateTime = LocalDateTime.now()) : AmfData() {

    /**
     * Writes the content (payload) of the AMF Date to the specified output stream.
     *
     * The payload consists of:
     * 1. An 8-byte IEEE-754 double representing the number of milliseconds since the epoch (UTC).
     * 2. A 16-bit big-endian time zone offset, which is always 0.
     *
     * @param output The [DataOutputStream] to which the date data is written.
     */
    override fun writeContent(output: DataOutputStream) {
        // Convert the LocalDateTime to epoch milliseconds in UTC.
        val millis = value.atZone(ZoneOffset.UTC).toInstant().toEpochMilli().toDouble()
        // Convert the double to its 8-byte IEEE-754 representation.
        val byteBuffer = ByteBuffer.allocate(8).putLong(millis.toRawBits())
        output.write(byteBuffer.array())
        // Write the 16-bit time zone offset (0).
        output.write(0x00)
        output.write(0x00)
    }

    /**
     * Reads the content (payload) of the AMF Date from the specified input stream.
     *
     * This method reads:
     * 1. An 8-byte IEEE-754 double representing the number of milliseconds since the epoch (UTC).
     * 2. A 16-bit time zone offset, which is ignored.
     *
     * The [value] property is updated with the corresponding [LocalDateTime] in UTC.
     *
     * @param input The [DataInputStream] from which the date data is read.
     */
    override fun readContent(input: DataInputStream) {
        val payload = ByteArray(8)
        input.readUntil(payload)
        val value = ByteBuffer.wrap(payload).long
        val millis = Double.fromBits(value)
        // Read and ignore the 16-bit time zone offset (2 bytes).
        input.read() // High byte
        input.read() // Low byte
        this@AmfDate.value =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis.toLong()), ZoneOffset.UTC)
    }

    /**
     * Returns the AMF data type for this element.
     *
     * @return [AmfDataType.DATE] indicating that this is a Date type.
     */
    override fun getType() = AmfDataType.DATE

    /**
     * Returns the size of the content (payload) for this AMF Date.
     *
     * The content size includes:
     * - 8 bytes for the IEEE-754 double.
     * - 2 bytes for the time zone offset.
     *
     * @return The total payload size in bytes (10 bytes).
     */
    override fun getContentSize(): Int = 8 + 2
}
