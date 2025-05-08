package com.nikhilhere.koamf.amf.v0

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Unit tests for [AmfDate].
 */
class AmfDateTest {

    /**
     * GIVEN a specific epoch-millisecond value
     * WHEN writing an AMF Date payload
     * THEN the output matches the expected 10-byte format
     */
    @Test
    fun `writeContent should emit big-endian IEEE-754 double plus zero timezone`() {
        // GIVEN: a Double timestamp of 2500.0 ms
        val timestamp = 2500.0
        val amfDate = AmfDate(timestamp)
        val outputStream = ByteArrayOutputStream().also {
            AmfDate(timestamp).writeContent(DataOutputStream(it))
        }

        // Expected payload: 8-byte double 2500.0 + 2-byte zero offset
        val expected = byteArrayOf(
            64, -93, -120, 0, 0, 0, 0, 0,  // 2500.0 as IEEE-754 BE
            0, 0                          // timezone == 0
        )

        // THEN
        assertArrayEquals(expected, outputStream.toByteArray())
    }

    /**
     * GIVEN a 10-byte AMF Date payload
     * WHEN reading into an AmfDate
     * THEN the value matches the original Double
     */
    @Test
    fun `readContent should parse big-endian IEEE-754 double and ignore timezone`() {
        // GIVEN: 2500.0 encoded + zero offset
        val payload = byteArrayOf(
            64, -93, -120, 0, 0, 0, 0, 0,
            0, 0
        )
        val dataInput = DataInputStream(ByteArrayInputStream(payload))
        val amfDate = AmfDate()

        // WHEN
        amfDate.readContent(dataInput)

        // THEN
        assertEquals(2500.0, amfDate.value, 0.0)
    }
}