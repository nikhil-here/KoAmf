package com.nikhilhere.koamf.amf.v0

import com.nikhilhere.koamf.amf.v0.AmfObject
import com.nikhilhere.koamf.amf.v0.AmfString
import com.nikhilhere.koamf.amf.v0.AmfNumber
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Unit tests for [AmfObject].
 */
class AmfObjectTest {

    /**
     * Round-trip test: write an object with two properties, then read it back.
     */
    @Test
    fun `writeContent and readContent should roundtrip AMF object properties`() {
        // GIVEN: an AMF object with two entries
        val src = AmfObject(mutableMapOf(
            AmfString("age") to AmfNumber(30.0),
            AmfString("name") to AmfString("Alice")
        ))
        val baos = ByteArrayOutputStream().also { out ->
            // WHEN: serializing
            src.writeContent(DataOutputStream(out))
        }
        
        // THEN: deserializing yields identical Map
        val input = DataInputStream(ByteArrayInputStream(baos.toByteArray()))
        val parsed = AmfObject()
        parsed.readContent(input)

        // verify size
        assertEquals(2, parsed.value.size)

        // verify entries
        assertEquals(
            src.value[AmfString("age")],
            parsed.value[AmfString("age")]
        )
    }

    /**
     * Empty object should serialize to only the object-end marker and parse back empty.
     */
    @Test
    fun `empty object should produce only end marker`() {
        // GIVEN: empty AmfObject
        val empty = AmfObject()
        val baos = ByteArrayOutputStream().also { empty.writeContent(DataOutputStream(it)) }

        // WHEN: reading back
        val parsed = AmfObject()
        parsed.readContent(DataInputStream(ByteArrayInputStream(baos.toByteArray())))

        // THEN: value map remains empty
        assertTrue(parsed.value.isEmpty())
    }
}