package com.nikhilhere.koamf.encoder

import com.nikhilhere.koamf.amf.AmfData
import java.io.DataInputStream
import java.io.DataOutputStream

interface Encoder {

    fun <T> encode(data: T, output: DataOutputStream)

    fun decode(input: DataInputStream): Any?
}