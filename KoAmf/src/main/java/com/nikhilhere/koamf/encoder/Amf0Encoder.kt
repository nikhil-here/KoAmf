package com.nikhilhere.koamf.encoder

import com.nikhilhere.koamf.amf.AmfData
import com.nikhilhere.koamf.amf.v0.AmfBoolean
import com.nikhilhere.koamf.amf.v0.AmfDate
import com.nikhilhere.koamf.amf.v0.AmfECMAArray
import com.nikhilhere.koamf.amf.v0.AmfNull
import com.nikhilhere.koamf.amf.v0.AmfNumber
import com.nikhilhere.koamf.amf.v0.AmfObject
import com.nikhilhere.koamf.amf.v0.AmfStrictArray
import com.nikhilhere.koamf.amf.v0.AmfString
import com.nikhilhere.koamf.amf.v0.AmfUndefined
import com.nikhilhere.koamf.amf.v0.AmfUnsupported
import com.nikhilhere.koamf.amf.v0.AmfXmlDocument
import com.nikhilhere.koamf.util.AMFObjectKeysMustBeString
import com.nikhilhere.koamf.util.UnsupportedKotlinDataTypeException
import java.io.DataInputStream
import java.io.DataOutputStream
import java.time.LocalDateTime

/**
 * The [Amf0Encoder] class is responsible for converting Kotlin data types into their
 * corresponding AMF0 representations and writing them to an output stream, as well as
 * reading AMF0 encoded data from an input stream and converting it back into [AmfData] objects.
 *
 * Supported Kotlin data types include:
 * - Numbers (Double, Int) → [AmfNumber]
 * - Boolean → [AmfBoolean]
 * - String → [AmfString]
 * - Map (with String keys) → [AmfObject]
 * - LocalDateTime → [AmfDate]
 * - List → [AmfStrictArray]
 * - null → [AmfNull]
 *
 * If an unsupported Kotlin data type is provided, an [UnsupportedKotlinDataTypeException] is thrown.
 *
 * The encoder uses the helper method [AmfData.readAmfData] (defined in the companion object of [AmfData])
 * to decode AMF values from an input stream.
 */
class Amf0Encoder : Encoder {

    /**
     * Encodes the provided Kotlin data into its corresponding AMF0 representation
     * and writes it to the given [DataOutputStream].
     *
     * The method converts the Kotlin data to an [AmfData] instance via [getAmfDataFromKotlinData],
     * writes the type marker using [AmfData.writeMarker], and then writes the data content using [AmfData.writeContent].
     *
     * @param data The Kotlin data to be encoded.
     * @param outputStream The [DataOutputStream] to which the AMF0 data will be written.
     */
    override fun <T> encode(data: T, outputStream: DataOutputStream) {
        val amfData = getAmfDataFromKotlinData(data)
        amfData.writeMarker(outputStream)
        amfData.writeContent(outputStream)
    }

    /**
     * Reads AMF0 encoded data from the provided [DataInputStream] and returns the corresponding [AmfData] object.
     *
     * This method delegates to the helper function [AmfData.readAmfData], which reads the type marker,
     * instantiates the appropriate [AmfData] subclass, and populates it by reading the content.
     *
     * @param input The [DataInputStream] from which the AMF0 data is read.
     * @return The decoded [AmfData] object.
     */
    override fun decode(input: DataInputStream): Any? {
        return getKotlinDataFromAmfData(AmfData.readAmfData(input))
    }

    /**
     * Helper method that maps a given Kotlin data type to its corresponding [AmfData] representation.
     *
     * This method handles common Kotlin data types by instantiating the appropriate AMF data subclass:
     * - [Double] and [Int] are mapped to [AmfNumber].
     * - [Boolean] is mapped to [AmfBoolean].
     * - [String] is mapped to [AmfString].
     * - [Map] with string keys is mapped to [AmfObject]. If a map key is not a string,
     *   an [AMFObjectKeysMustBeString] exception is thrown.
     * - [LocalDateTime] is mapped to [AmfDate].
     * - [List] is mapped to [AmfStrictArray].
     * - null is mapped to [AmfNull].
     * - Unsupported data types trigger an [UnsupportedKotlinDataTypeException].
     *
     * @param data The Kotlin data to be converted.
     * @return An [AmfData] instance corresponding to the provided data.
     * @throws UnsupportedKotlinDataTypeException if the provided data type is not supported.
     */
    private fun <T> getAmfDataFromKotlinData(data: T): AmfData {
        return when (data) {
            is Double -> AmfNumber(data)
            is Int -> AmfNumber(data.toDouble())
            is Boolean -> AmfBoolean(data)
            is String -> AmfString(data)
            is Map<*, *> -> {
                val map = mutableMapOf<AmfString, AmfData>()
                data.forEach { key, value ->
                    val keyStr = (key as? String) ?: throw AMFObjectKeysMustBeString()
                    val k = AmfString(keyStr)
                    val v = getAmfDataFromKotlinData(value)
                    map.put(k, v)
                }
                AmfObject(map)
            }

            is LocalDateTime -> AmfDate(data)
            is List<*> -> {
                val amfDataList = data.map { getAmfDataFromKotlinData(it) }.toMutableList()
                AmfStrictArray(value = amfDataList)
            }

            null -> AmfNull()
            else -> throw UnsupportedKotlinDataTypeException((data as Any).javaClass.toString())
        }
    }

    /**
     * Helper method to convert an [AmfData] object back into its corresponding Kotlin data type.
     *
     * This method examines the specific [AmfData] subclass and extracts the underlying data:
     * - [AmfString] returns its string value.
     * - [AmfBoolean] returns its Boolean value.
     * - [AmfDate] returns its [LocalDateTime] value.
     * - [AmfECMAArray], [AmfObject], and [AmfStrictArray] return their corresponding map or list data.
     * - [AmfNull], [AmfUndefined], and [AmfUnsupported] return null.
     * - [AmfNumber] returns its numeric value.
     * - [AmfXmlDocument] returns its XML string.
     *
     * @param data The [AmfData] object to convert.
     * @return The corresponding Kotlin data value.
     */
    private fun getKotlinDataFromAmfData(data: AmfData): Any? {
        return when (data) {
            is AmfString -> data.value
            is AmfBoolean -> data.value
            is AmfDate -> data.value
            is AmfECMAArray -> data.value
            is AmfNull -> null
            is AmfNumber -> data.value
            is AmfObject -> data.value
            is AmfStrictArray -> data.value
            is AmfUndefined -> null
            is AmfUnsupported -> null
            is AmfXmlDocument -> data.value
            else -> null
        }
    }
}
