package com.nikhilhere.koamf.amf.v0

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class AmfStringTest {
    @Test
    fun `GIVEN an AMF string WHEN written to output stream THEN output buffer matches expected format`() {
        // Given:
        val value = "hello world"
        val amfString = AmfString(value)
        val outputStream = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(outputStream)

        // When:
        amfString.writeContent(dataOutput)

        // Then:
        val expectedBytes = byteArrayOf(
            0x00, 0x0B,                   // Length header: 11 bytes
            0x68, 0x65, 0x6C, 0x6C, 0x6F,   // "hello"
            0x20,                         // space
            0x77, 0x6F, 0x72, 0x6C, 0x64    // "world"
        )

        assertArrayEquals(expectedBytes, outputStream.toByteArray())
    }

    @Test
    fun `GIVEN an AMF string WHEN read from input stream THEN value equals expected string`() {
        // Given:
        val bytes = byteArrayOf(
            0x00, 0x0B,                   // Length header: 11 bytes
            0x68, 0x65, 0x6C, 0x6C, 0x6F,   // "hello"
            0x20,                         // space
            0x77, 0x6F, 0x72, 0x6C, 0x64    // "world"
        )
        val inputStream = ByteArrayInputStream(bytes)
        val dataInput = DataInputStream(inputStream)

        // When:
        val amfString = AmfString()
        amfString.readContent(dataInput)

        // Then:
        assertEquals("hello world", amfString.value)
    }
}