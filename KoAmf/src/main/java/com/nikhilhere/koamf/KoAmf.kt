package com.nikhilhere.koamf

import com.nikhilhere.koamf.encoder.Amf0Encoder
import com.nikhilhere.koamf.amf.Version

class KoAmf(amfVersion: Version) {

    private val encoder by lazy {
        when (amfVersion) {
            Version.AMF0 -> Amf0Encoder()
            Version.AMF3 ->
                throw IllegalStateException("AMF3 Encoding is not supported by KoAmf yet.")
        }
    }

    fun <T> encode(data: T) {
        encoder.encode(data)
    }
}