package com.nikhilhere.koamf.util

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

fun Int.toUInt32(): ByteArray {
    return byteArrayOf(
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        this.toByte()
    )
}