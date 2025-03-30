package com.nikhilhere.koamf.amf

import kotlin.jvm.javaClass

class Amf0Encoder : Encoder {

    override fun <T> encode(data: T): Int {
        when (data) {
            else -> throw IllegalArgumentException("Type ${(data as Any).javaClass} not supported")
        }
    }

}