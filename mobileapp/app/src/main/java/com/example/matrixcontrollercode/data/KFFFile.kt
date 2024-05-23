package com.example.matrixcontrollercode.data

import android.util.Log
import java.nio.ByteBuffer

data class KFFFile(
    val version: UByte,
    var width: UByte,
    var height: UByte,
    var speed: UByte,
    var data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KFFFile

        if (version != other.version) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (speed != other.speed) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    fun serialize(): ByteArray {
        val buffer = ByteBuffer.allocate(4 + data.size)
        buffer.put(version.toByte())
        buffer.put(width.toByte())
        buffer.put(height.toByte())
        buffer.put(speed.toByte())
        buffer.put(data)
        return buffer.array()
    }

    companion object {
        fun deserialize(byteArray: ByteArray): KFFFile {
            val buffer = ByteBuffer.wrap(byteArray)
            val version = buffer.get().toUByte()
            val x = buffer.get().toUByte()
            val y = buffer.get().toUByte()
            val speed = buffer.get().toUByte()
            val dataLength = byteArray.size - 4
            val data = ByteArray(dataLength)
            buffer.get(data)
            if (data.size % x.toInt() * y.toInt() * 3 != 0) {
                Log.d("KFFFile", "Data length is incorrect. Corrupted file?")
                Log.d("KFFFile", "Data length: ${data.size} Expected multiple of length: ${x.toInt() * y.toInt() * 3}")
                throw IllegalArgumentException("Data length is incorrect. Corrupted file?")
            }
            return KFFFile(version, x, y, speed, data)
        }
    }
}

