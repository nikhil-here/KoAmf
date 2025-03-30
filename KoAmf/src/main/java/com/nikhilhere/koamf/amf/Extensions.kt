package com.nikhilhere.koamf.amf

fun Int.toUInt16(): ByteArray = byteArrayOf(
    (this ushr 8).toByte(),
    this.toByte()
)

fun Int.toUInt24(): ByteArray {
    return byteArrayOf(
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        this.toByte()
    )
}