package com.nikhilhere.koamf.util

import java.io.InputStream

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

fun InputStream.readUInt16() : Int {
    val data = ByteArray(2)
    read(data)
    return data.toUInt16()
}

fun InputStream.readUInt32(): Int {
    val data = ByteArray(4)
    read(data)
    return data.toUInt32()
}

/**
 * Converts the first two bytes of this ByteArray into an unsigned 16-bit integer.
 *
 * This extension function interprets the first two bytes as a big-endian unsigned integer.
 *
 * Steps:
 * 1. `this[0].toInt() and 0xff`:
 *    - Converts the first byte to an Int and masks it with 0xff.
 *    - This ensures the byte is treated as an unsigned value (0 to 255),
 *      effectively clearing any sign extension.
 *
 * 2. `shl 8`:
 *    - Shifts the resulting value 8 bits to the left,
 *      positioning it as the high-order byte in the 16-bit integer.
 *
 * 3. `this[1].toInt() and 0xff`:
 *    - Converts the second byte to an Int and masks it with 0xff,
 *      ensuring it is treated as an unsigned value.
 *
 * 4. `or`:
 *    - Combines the shifted high-order byte with the low-order byte using bitwise OR.
 *
 * For example, if the first two bytes are 0x12 and 0x34, the calculation proceeds as:
 * - High byte: 0x12 becomes 0x12 shl 8 = 0x1200.
 * - Low byte: 0x34 remains 0x34.
 * - Combined: 0x1200 or 0x34 = 0x1234 (4660 in decimal).
 *
 * @return The unsigned 16-bit integer represented by the first two bytes of this ByteArray.
 */
fun ByteArray.toUInt16(): Int {
    return this[0].toInt() and 0xff shl 8 or (this[1].toInt() and 0xff)
}

fun ByteArray.toUInt32(): Int {
    return this[0].toInt() and 0xff shl 24 or (this[1].toInt() and 0xff shl 16) or (this[2].toInt() and 0xff shl 8) or (this[3].toInt() and 0xff)
}


/**
 * Reads bytes from this InputStream into the given [byteArray] until the array is completely filled.
 *
 * This extension function repeatedly calls the standard read() method on the InputStream, starting at the
 * current offset (tracked by [bytesRead]) and attempting to read as many bytes as remain in the [byteArray].
 *
 * The process is as follows:
 * 1. Initialize [bytesRead] to 0.
 * 2. In a loop, call read(byteArray, bytesRead, byteArray.size - bytesRead) to read data into the remaining
 *    portion of the array.
 * 3. If the read() call returns a non-negative number, add that value to [bytesRead].
 * 4. The loop continues until [bytesRead] equals byteArray.size, meaning the array is fully populated.
 *
 */
fun InputStream.readUntil(byteArray: ByteArray) {
    var bytesRead = 0
    while (bytesRead < byteArray.size) {
        val result = read(byteArray, bytesRead, byteArray.size - bytesRead)
        if (result != -1) bytesRead += result
    }
}