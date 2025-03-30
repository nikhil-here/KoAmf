package com.nikhilhere.koamf.amf

interface Encoder {
    fun <T> encode(data: T)
}