package com.nikhilhere.koamf.amf.v0


import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class AmfNumberTest {

    @Test
    fun `GIVEN an AMF number WHEN written to output stream THEN output buffer matches IEEE-754 encoding`() {
        // Given: an AmfNumber with value 42.0
        val value = 42.0
        val amfNumber = AmfNumber(value)
        val outputStream = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(outputStream)

        // When: writing the content (payload) of the AmfNumber to the output stream.
        amfNumber.writeContent(dataOutput)

        // Then: the output buffer should contain the IEEE-754 big-endian representation of 42.0.
        // Expected IEEE-754 representation for 42.0 is: 0x40 0x45 0x00 0x00 0x00 0x00 0x00 0x00
        val expectedBytes = byteArrayOf(
            0x40, 0x45, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        )

        assertArrayEquals(expectedBytes, outputStream.toByteArray())
    }

    @Test
    fun `GIVEN an IEEE-754 encoded AMF number WHEN read from input stream THEN value equals 42_0`() {
        // Given: the 8-byte IEEE-754 big-endian representation for 42.0
        // Expected bytes: 0x40, 0x45, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        val expectedBytes = byteArrayOf(
            0x40, 0x45, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        )
        val inputStream = ByteArrayInputStream(expectedBytes)
        val dataInput = DataInputStream(inputStream)

        // Create an AmfNumber instance (initial value doesn't matter).
        val amfNumber = AmfNumber()

        // When: reading the content (payload) of the AmfNumber from the input stream.
        amfNumber.readContent(dataInput)

        // Then: the value should be 42.0.
        assertEquals(42.0, amfNumber.value, 0.0)
    }
}
