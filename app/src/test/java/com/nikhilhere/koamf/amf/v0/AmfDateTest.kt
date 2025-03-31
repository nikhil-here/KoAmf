package com.nikhilhere.koamf.amf.v0

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset


class AmfDateTest {


    @Test
    fun `GIVEN an AMF Date WHEN written to output stream THEN output matches expected format`() {
        // Given: a LocalDateTime representing 2000-01-01T00:00:00 UTC.
        val date = LocalDateTime.ofEpochSecond(1,0, ZoneOffset.UTC)
        val amfDate = AmfDate(date)
        val outputStream = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(outputStream)

        // When: writing the content (payload) of the AmfDate.
        amfDate.writeContent(dataOutput)

        // Then: the output should match the expected byte array.
        // Expected IEEE-754 representation for 946684800000.0 is:
        // [0x41, 0xD2, 0x1F, 0x8C, 0x6B, 0x40, 0x00, 0x00] for the double value,
        // followed by [0x00, 0x00] for the 16-bit time zone offset.
        val expectedBytes = byteArrayOf(
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte(),
            0x00.toByte(), 0x00.toByte()
        )
        assertArrayEquals(expectedBytes, outputStream.toByteArray())
    }

    @Test
    fun `GIVEN an AMF Date byte array WHEN read from input stream THEN value equals expected LocalDateTime`() {
        // Given: a byte array representing an AMF Date with epoch millis 946684800000.0 and a 0 time zone offset.
        val bytes = byteArrayOf(
            0x41.toByte(), 0xD2.toByte(), 0x1F.toByte(), 0x8C.toByte(),
            0x6B.toByte(), 0x40.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte()
        )
        val inputStream = ByteArrayInputStream(bytes)
        val dataInput = DataInputStream(inputStream)
        val amfDate = AmfDate()

        // When: reading the content (payload) of the AmfDate from the input stream.
        amfDate.readContent(dataInput)

        // Then: the parsed LocalDateTime should equal 2000-01-01T00:00:00 UTC.
        val expectedDate = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(946684800000L),
            ZoneOffset.UTC
        )
        assertEquals(expectedDate, amfDate.value)
    }
}