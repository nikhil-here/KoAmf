package com.nikhilhere.koamf.amf.v0

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream


class AmfBooleanTest {

    @Test
    fun `GIVEN a True boolean value WHEN written to output stream THEN output buffer contains 0x01`() {
        //GIVEN
        val amfBoolean = AmfBoolean(true)
        val outputStream = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(outputStream)

        //WHEN
        amfBoolean.writeContent(dataOutput)

        //THEN
        val expectedBytes = byteArrayOf(0x01)
        assertArrayEquals(expectedBytes, outputStream.toByteArray())

    }

    @Test
    fun `GIVEN a False boolean value WHEN written to output stream THEN output buffer contains 0x00`() {
        //GIVEN
        val amfBoolean = AmfBoolean(false)
        val outputStream = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(outputStream)

        //WHEN
        amfBoolean.writeContent(dataOutput)

        //THEN
        val expectedBytes = byteArrayOf(0x00)
        assertArrayEquals(expectedBytes, outputStream.toByteArray())
    }

    @Test
    fun `GIVEN an input stream with byte 0x01 WHEN reading content THEN AmfBoolean value is True`() {
        //GIVEN
        val bytes = byteArrayOf(0x01)
        val inputStream = ByteArrayInputStream(bytes)
        val input = DataInputStream(inputStream)
        val amfBoolean = AmfBoolean()

        //WHEN
        amfBoolean.readContent(input)

        //THEN
        assertEquals(true, amfBoolean.value)
    }

    @Test
    fun `GIVEN an input stream with byte 0x00 WHEN reading content THEN AmfBoolean value is False`() {
        //GIVEN
        val bytes = byteArrayOf(0x00)
        val inputStream = ByteArrayInputStream(bytes)
        val input = DataInputStream(inputStream)
        val amfBoolean = AmfBoolean()

        //WHEN
        amfBoolean.readContent(input)

        //THEN
        assertEquals(false, amfBoolean.value)
    }
}