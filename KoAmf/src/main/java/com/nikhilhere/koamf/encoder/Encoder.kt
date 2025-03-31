package com.nikhilhere.koamf.encoder

interface Encoder {
    fun <T> encode(data: T) : ByteArray
}