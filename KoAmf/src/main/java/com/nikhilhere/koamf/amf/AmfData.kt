package com.nikhilhere.koamf.amf

import com.nikhilhere.koamf.amf.v0.AmfBoolean
import com.nikhilhere.koamf.amf.v0.AmfDate
import com.nikhilhere.koamf.amf.v0.AmfECMAArray
import com.nikhilhere.koamf.amf.v0.AmfLongString
import com.nikhilhere.koamf.amf.v0.AmfNull
import com.nikhilhere.koamf.amf.v0.AmfNumber
import com.nikhilhere.koamf.amf.v0.AmfObject
import com.nikhilhere.koamf.amf.v0.AmfObjectEnd
import com.nikhilhere.koamf.amf.v0.AmfStrictArray
import com.nikhilhere.koamf.amf.v0.AmfString
import com.nikhilhere.koamf.amf.v0.AmfUndefined
import com.nikhilhere.koamf.amf.v0.AmfUnsupported
import com.nikhilhere.koamf.amf.v0.AmfXmlDocument
import com.nikhilhere.koamf.util.UnsupportedAMFDataType
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Abstract base class for all AMF data types in KOAMF.
 *
 * All concrete AMF data classes (for example, AmfNumber, AmfBoolean, AmfString) extend this class.
 * This base class provides a consistent interface for writing and reading AMF data.
 * The responsibilities of this class include:
 * - Writing the type marker (header) to an output stream using [writeMarker].
 * - Reading the type marker (header) from an input stream using [readMarker].
 * - Providing abstract methods [writeContent] and [readContent] to handle the type-specific data.
 * - Reporting the AMF data type via [getType] and the size of its content via [getContentSize].
 *
 * This design enables each AMF data type to implement its own encoding/decoding logic for content payload while
 * adhering to a standardized interface for reading and writing header marker
 *
 * @see AmfDataType
 */
abstract class AmfData {


    companion object {

        /**
         * Helper function to read an AMF value from the given input stream.
         *
         * This function should determine the AMF type by reading the type marker,
         * instantiate the appropriate AmfData subclass, call its readContent method,
         * and return the decoded value.
         *
         * @param input The [DataInputStream] to read the value from.
         * @return The decoded value.
         */
        fun readAmfData(input: DataInputStream): AmfData {
            val type = input.read()
            val amfType = AmfDataType
                .entries.firstOrNull { it.marker.toInt() == type }
                ?: AmfDataType.UNDEFINED
            val amfData = when (amfType) {
                AmfDataType.NUMBER -> AmfNumber()
                AmfDataType.BOOLEAN -> AmfBoolean()
                AmfDataType.STRING -> AmfString()
                AmfDataType.LONG_STRING -> AmfLongString()
                AmfDataType.DATE -> AmfDate()
                AmfDataType.OBJECT -> AmfObject()
                AmfDataType.ECMA_ARRAY -> AmfECMAArray()
                AmfDataType.STRICT_ARRAY -> AmfStrictArray()
                AmfDataType.OBJECT_END -> AmfObjectEnd()
                AmfDataType.XML_DOCUMENT -> AmfXmlDocument()
                AmfDataType.NULL -> AmfNull()
                AmfDataType.UNDEFINED -> AmfUndefined()
                AmfDataType.UNSUPPORTED -> AmfUnsupported()
                AmfDataType.RECORDSET,
                AmfDataType.TYPED_OBJECT,
                AmfDataType.MOVIE_CLIP,
                AmfDataType.REFERENCE -> {
                    throw UnsupportedAMFDataType(amfType.name)
                }
            }
            amfData.readContent(input)
            return amfData
        }
    }

    /**
     * Writes the AMF data type marker (header) to the specified output stream.
     *
     * The marker identifies the AMF data type (e.g. Number, String, etc.) and is written as a single byte.
     *
     * @param output The [DataOutputStream] to which the marker is written.
     */
    fun writeMarker(output: DataOutputStream) {
        output.write(getType().marker.toInt())
    }

    /**
     * Reads the AMF data type marker (header) from the specified input stream.
     *
     * Reads one byte from the input stream and returns the corresponding [AmfDataType].
     * If no matching type is found, [AmfDataType.UNSUPPORTED] is returned.
     *
     * @param input The [DataInputStream] from which the marker is read.
     * @return The [AmfDataType] corresponding to the marker.
     */
    fun readMarker(input: DataInputStream): AmfDataType {
        val type = input.read()
        return AmfDataType.entries.firstOrNull { it.marker.toInt() == type }
            ?: AmfDataType.UNSUPPORTED
    }

    /**
     * Writes the content (payload) of this AMF data type to the specified output stream.
     *
     * Concrete subclasses must implement this method to write the type-specific data.
     *
     * @param output The [DataOutputStream] to which the content is written.
     */
    abstract fun writeContent(output: DataOutputStream)

    /**
     * Reads the content (payload) of this AMF data type from the specified input stream.
     *
     * Concrete subclasses must implement this method to read and process the type-specific data.
     *
     * @param input The [DataInputStream] from which the content is read.
     */
    abstract fun readContent(input: DataInputStream)

    /**
     * Returns the AMF data type represented by this instance.
     *
     * @return The [AmfDataType] associated with this data.
     */
    abstract fun getType(): AmfDataType

    /**
     * Returns the size (in bytes) of the content (payload), excluding the type marker.
     *
     * This size indicates how many bytes the payload occupies.
     *
     * @return The size of the payload in bytes.
     */
    abstract fun getContentSize(): Int
}