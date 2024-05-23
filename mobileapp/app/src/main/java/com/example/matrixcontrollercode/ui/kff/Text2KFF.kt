package com.example.matrixcontrollercode.ui.kff

import android.graphics.Color
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class Text2KFF {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        var symbolsMap :Map<String, UByteArray>   = mapOf(
        "A" to ubyteArrayOf(
        0b01111110.toUByte(),
        0b00010001.toUByte(),
        0b00010001.toUByte(),
        0b01111110.toUByte()
        ),
        "B" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01001001.toUByte(),
        0b01001001.toUByte(),
        0b00110110.toUByte()
        ),
        "C" to ubyteArrayOf(
        0b00111110.toUByte(),
        0b01000001.toUByte(),
        0b01000001.toUByte(),
        0b00100010.toUByte()
        ),
        "D" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01000001.toUByte(),
        0b01000001.toUByte(),
        0b00111110.toUByte()
        ),
        "E" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01001001.toUByte(),
        0b01001001.toUByte(),
        0b01000001.toUByte()
        ),
        "F" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00001001.toUByte(),
        0b00001001.toUByte(),
        0b00000001.toUByte()
        ),
        "G" to ubyteArrayOf(
        0b00111110.toUByte(),
        0b01000001.toUByte(),
        0b01001001.toUByte(),
        0b01111010.toUByte()
        ),
        "H" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00001000.toUByte(),
        0b00001000.toUByte(),
        0b01111111.toUByte()
        ),
        "I" to ubyteArrayOf(
        0b01000001.toUByte(),
        0b01111111.toUByte(),
        0b01000001.toUByte()
        ),
        "J" to ubyteArrayOf(
        0b00110000.toUByte(),
        0b01000000.toUByte(),
        0b01000001.toUByte(),
        0b00111111.toUByte()
        ),
        "K" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00001000.toUByte(),
        0b00010100.toUByte(),
        0b01100011.toUByte()
        ),
        "L" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte()
        ),
        "M" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00000010.toUByte(),
        0b00001100.toUByte(),
        0b00000010.toUByte(),
        0b01111111.toUByte()
        ),
        "N" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00000100.toUByte(),
        0b00001000.toUByte(),
        0b00010000.toUByte(),
        0b01111111.toUByte()
        ),
        "O" to ubyteArrayOf(
        0b00111110.toUByte(),
        0b01000001.toUByte(),
        0b01000001.toUByte(),
        0b00111110.toUByte()
        ),
        "P" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00001001.toUByte(),
        0b00001001.toUByte(),
        0b00000110.toUByte()
        ),
        "Q" to ubyteArrayOf(
        0b00111110.toUByte(),
        0b01000001.toUByte(),
        0b01000001.toUByte(),
        0b10111110.toUByte()
        ),
        "R" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00001001.toUByte(),
        0b00001001.toUByte(),
        0b01110110.toUByte()
        ),
        "S" to ubyteArrayOf(
        0b01000110.toUByte(),
        0b01001001.toUByte(),
        0b01001001.toUByte(),
        0b00110010.toUByte()
        ),
        "T" to ubyteArrayOf(
        0b00000001.toUByte(),
        0b00000001.toUByte(),
        0b01111111.toUByte(),
        0b00000001.toUByte(),
        0b00000001.toUByte()
        ),
        "U" to ubyteArrayOf(
        0b00111111.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte(),
        0b00111111.toUByte()
        ),
        "V" to ubyteArrayOf(
        0b00001111.toUByte(),
        0b00110000.toUByte(),
        0b01000000.toUByte(),
        0b00110000.toUByte(),
        0b00001111.toUByte()
        ),
        "W" to ubyteArrayOf(
        0b00111111.toUByte(),
        0b01000000.toUByte(),
        0b00111000.toUByte(),
        0b01000000.toUByte(),
        0b00111111.toUByte()
        ),
        "X" to ubyteArrayOf(
        0b01100011.toUByte(),
        0b00010100.toUByte(),
        0b00001000.toUByte(),
        0b00010100.toUByte(),
        0b01100011.toUByte()
        ),
        "Y" to ubyteArrayOf(
        0b00000111.toUByte(),
        0b00001000.toUByte(),
        0b01110000.toUByte(),
        0b00001000.toUByte(),
        0b00000111.toUByte()
        ),
        "Z" to ubyteArrayOf(
        0b01100001.toUByte(),
        0b01010001.toUByte(),
        0b01001001.toUByte(),
        0b01000111.toUByte()
        ),
        "a" to ubyteArrayOf(
        0b00100000.toUByte(),
        0b01010100.toUByte(),
        0b01010100.toUByte(),
        0b01111000.toUByte()
        ),
        "b" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01000100.toUByte(),
        0b01000100.toUByte(),
        0b00111000.toUByte()
        ),
        "c" to ubyteArrayOf(
        0b00111000.toUByte(),
        0b01000100.toUByte(),
        0b01000100.toUByte(),
        0b00101000.toUByte()
        ),
        "d" to ubyteArrayOf(
        0b00111000.toUByte(),
        0b01000100.toUByte(),
        0b01000100.toUByte(),
        0b01111111.toUByte()
        ),
        "e" to ubyteArrayOf(
        0b00111000.toUByte(),
        0b01010100.toUByte(),
        0b01010100.toUByte(),
        0b00011000.toUByte()
        ),
        "f" to ubyteArrayOf(
        0b00000100.toUByte(),
        0b01111110.toUByte(),
        0b00000101.toUByte()
        ),
        "g" to ubyteArrayOf(
        0b10011000.toUByte(),
        0b10100100.toUByte(),
        0b10100100.toUByte(),
        0b01111000.toUByte()
        ),
        "h" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00000100.toUByte(),
        0b00000100.toUByte(),
        0b01111000.toUByte()
        ),
        "i" to ubyteArrayOf(
        0b01000100.toUByte(),
        0b01111101.toUByte(),
        0b01000000.toUByte()
        ),
        "j" to ubyteArrayOf(
        0b01000000.toUByte(),
        0b10000000.toUByte(),
        0b10000100.toUByte(),
        0b01111101.toUByte()
        ),
        "k" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b00010000.toUByte(),
        0b00101000.toUByte(),
        0b01000100.toUByte()
        ),
        "l" to ubyteArrayOf(
        0b01000001.toUByte(),
        0b01111111.toUByte(),
        0b01000000.toUByte()
        ),
        "m" to ubyteArrayOf(
        0b01111100.toUByte(),
        0b00000100.toUByte(),
        0b01111100.toUByte(),
        0b00000100.toUByte(),
        0b01111000.toUByte()
        ),
        "n" to ubyteArrayOf(
        0b01111100.toUByte(),
        0b00000100.toUByte(),
        0b00000100.toUByte(),
        0b01111000.toUByte()
        ),
        "o" to ubyteArrayOf(
        0b00111000.toUByte(),
        0b01000100.toUByte(),
        0b01000100.toUByte(),
        0b00111000.toUByte()
        ),
        "p" to ubyteArrayOf(
        0b11111100.toUByte(),
        0b00100100.toUByte(),
        0b00100100.toUByte(),
        0b00011000.toUByte()
        ),
        "q" to ubyteArrayOf(
        0b00011000.toUByte(),
        0b00100100.toUByte(),
        0b00100100.toUByte(),
        0b11111100.toUByte()
        ),
        "r" to ubyteArrayOf(
        0b01111100.toUByte(),
        0b00001000.toUByte(),
        0b00000100.toUByte(),
        0b00000100.toUByte()
        ),
        "s" to ubyteArrayOf(
        0b01001000.toUByte(),
        0b01010100.toUByte(),
        0b01010100.toUByte(),
        0b00100100.toUByte()
        ),
        "t" to ubyteArrayOf(
        0b00000100.toUByte(),
        0b00111111.toUByte(),
        0b01000100.toUByte()
        ),
        "u" to ubyteArrayOf(
        0b00111100.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte(),
        0b01111100.toUByte()
        ),
        "v" to ubyteArrayOf(
        0b00011100.toUByte(),
        0b00100000.toUByte(),
        0b01000000.toUByte(),
        0b00100000.toUByte(),
        0b00011100.toUByte()
        ),
        "w" to ubyteArrayOf(
        0b00111100.toUByte(),
        0b01000000.toUByte(),
        0b00111100.toUByte(),
        0b01000000.toUByte(),
        0b00111100.toUByte()
        ),
        "x" to ubyteArrayOf(
        0b01000100.toUByte(),
        0b00101000.toUByte(),
        0b00010000.toUByte(),
        0b00101000.toUByte(),
        0b01000100.toUByte()
        ),
        "y" to ubyteArrayOf(
        0b10011100.toUByte(),
        0b10100000.toUByte(),
        0b10100000.toUByte(),
        0b01111100.toUByte()
        ),
        "z" to ubyteArrayOf(
        0b01100100.toUByte(),
        0b01010100.toUByte(),
        0b01001100.toUByte()
        ),
        " " to ubyteArrayOf(
        0b00000000.toUByte()
        ),
        "!" to ubyteArrayOf(
        0b01011111.toUByte()
        ),
        "0" to ubyteArrayOf(
        0b00111110u,
        0b01000001u,
        0b01000001u,
        0b00111110u
        ),
        "1" to ubyteArrayOf(
        0b01000010u,
        0b01111111u,
        0b01000000u
        ),
        "2" to ubyteArrayOf(
        0b01100010u,
        0b01010001u,
        0b01001001u,
        0b01000110u
        ),
        "3" to ubyteArrayOf(
        0b00100010u,
        0b01000001u,
        0b01001001u,
        0b00110110u
        ),
        "4" to ubyteArrayOf(
        0b00011000u,
        0b00010100u,
        0b00010010u,
        0b01111111u
        ),
        "5" to ubyteArrayOf(
        0b00100111u,
        0b01000101u,
        0b01000101u,
        0b00111001u
        ),
        "6" to ubyteArrayOf(
        0b00111110u,
        0b01001001u,
        0b01001001u,
        0b00110000u
        ),
        "7" to ubyteArrayOf(
        0b01100001u,
        0b00010001u,
        0b00001001u,
        0b00000111u
        ),
        "8" to ubyteArrayOf(
        0b00110110u,
        0b01001001u,
        0b01001001u,
        0b00110110u
        ),
        "9" to ubyteArrayOf(
        0b00000110u,
        0b01001001u,
        0b01001001u,
        0b00111110u
        ),
        ":" to ubyteArrayOf(
        0b01010000.toUByte()
        ),
        "#" to ubyteArrayOf(
        0b00010100.toUByte(),
        0b00111110.toUByte(),
        0b00010100.toUByte(),
        0b00111110.toUByte(),
        0b00010100.toUByte()
        ),
        "$" to ubyteArrayOf(
        0b00100100.toUByte(),
        0b01101010.toUByte(),
        0b00101011.toUByte(),
        0b00010010.toUByte(),
        ),
        "%" to ubyteArrayOf(
        0b01100011.toUByte(),
        0b00010011.toUByte(),
        0b00001000.toUByte(),
        0b01100100.toUByte(),
        0b01100011.toUByte()
        ),
        "&" to ubyteArrayOf(
        0b00110110.toUByte(),
        0b01001001.toUByte(),
        0b01010110.toUByte(),
        0b00100000.toUByte(),
        0b01010000.toUByte()
        ),
        "'" to ubyteArrayOf(
        0b00011100.toUByte(),
        0b00100010.toUByte(),
        0b01000001.toUByte(),
        ),
        "(" to ubyteArrayOf(
        0b00001100.toUByte(),
        0b00010010.toUByte(),
        0b00100001.toUByte(),
        ),
        ")" to ubyteArrayOf(
        0b00100001.toUByte(),
        0b00010010.toUByte(),
        0b00001100.toUByte(),
        ),
        "*" to ubyteArrayOf(
        0b00001000.toUByte(),
        0b00010100.toUByte(),
        0b00111110.toUByte(),
        0b00010100.toUByte(),
        0b00001000.toUByte()
        ),
        "+" to ubyteArrayOf(
        0b00001000.toUByte(),
        0b00001000.toUByte(),
        0b00111110.toUByte(),
        0b00001000.toUByte(),
        0b00001000.toUByte()
        ),
        "," to ubyteArrayOf(
        0b00110000.toUByte(),
        0b00110000.toUByte(),
        ),
        "-" to ubyteArrayOf(
        0b00000100.toUByte(),
        0b00000100.toUByte(),
        0b00000100.toUByte(),
        0b00000100.toUByte(),
        ),
        "." to ubyteArrayOf(
        0b01100000.toUByte(),
        0b01100000.toUByte(),
        ),
        "/" to ubyteArrayOf(
        0b01100000.toUByte(),
        0b00011000.toUByte(),
        0b00000110.toUByte(),
        0b00000001.toUByte()
        ),
        ";" to ubyteArrayOf(
        0b10000000.toUByte(),
        0b01010000.toUByte()
        ),
        "<" to ubyteArrayOf(
        0b00010000.toUByte(),
        0b00101000.toUByte(),
        0b01000100.toUByte()
        ),
        "=" to ubyteArrayOf(
        0b00010100.toUByte(),
        0b00010100.toUByte(),
        0b00010100.toUByte()
        ),
        ">" to ubyteArrayOf(
        0b01000100.toUByte(),
        0b00101000.toUByte(),
        0b00010000.toUByte()
        ),
        "?" to ubyteArrayOf(
        0b00000010.toUByte(),
        0b01011001.toUByte(),
        0b00001001.toUByte(),
        0b00000110.toUByte()
        ),
        "@" to ubyteArrayOf(
        0b00111110.toUByte(),
        0b01001001.toUByte(),
        0b01010101.toUByte(),
        0b01011101.toUByte(),
        0b00001110.toUByte()
        ),
        "[" to ubyteArrayOf(
        0b01111111.toUByte(),
        0b01000001.toUByte()
        ),
        "]" to ubyteArrayOf(
        0b01000001.toUByte(),
        0b01111111.toUByte()
        ),
        "_" to ubyteArrayOf(
        0b01000000.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte(),
        0b01000000.toUByte()
        ),
        "{" to ubyteArrayOf(
        0b00001000.toUByte(),
        0b00110110.toUByte(),
        0b01000001.toUByte()
        ),
        "|" to ubyteArrayOf(
        0b01111111.toUByte()
        ),
        "}" to ubyteArrayOf(
        0b01000001.toUByte(),
        0b00110110.toUByte(),
        0b00001000.toUByte()
        ),
        "~" to ubyteArrayOf(
        0b00001000.toUByte(),
        0b00000100.toUByte(),
        0b00001000.toUByte(),
        0b00000100.toUByte()
        ))
    }

    //TODO only works for matrixX = 8 make it use 0 for matrixX > 8
    @OptIn(ExperimentalUnsignedTypes::class)
    fun convertToByteArray(text: String, autoEntry: Boolean, autoExit: Boolean, autoSpace: Boolean, matrixX: Int, matrixY: Int, color: Int, bgColor: Int): ByteArray? {
        if (text.isEmpty()) return null
        Log.d("Text2KFF",
            "$text $autoEntry $autoSpace $autoExit $matrixX $matrixY $color $bgColor"
        )
        var byteArrayCache = createByteArrayCache(text, autoEntry, autoSpace, autoExit, matrixX)
        if (byteArrayCache.isEmpty()) return null
        val frames = calculateFrames(byteArrayCache.size, matrixX)
        val binaryData = constructBinaryData(byteArrayCache, matrixY, matrixX, color, bgColor, frames)
        return binaryData
    }

    private fun createByteArrayCache(text: String, autoEntry: Boolean, autoSpace: Boolean, autoExit: Boolean, matrixX: Int): UByteArray {
        var byteArrayCache = ubyteArrayOf()
        if (autoEntry) byteArrayCache += ByteArray(matrixX) { 0 }.toUByteArray()
        text.forEach { char ->
            symbolsMap[char.toString()]?.also {
                byteArrayCache += it
                if (autoSpace) byteArrayCache += ubyteArrayOf(0u) // Assumes space is represented as a single column of off pixels
            }
        }
        if (autoExit) byteArrayCache += ByteArray(matrixX) { 0 }.toUByteArray()
        return byteArrayCache
    }

    private fun constructBinaryData(byteArrayCache: UByteArray, matrixY: Int, matrixX: Int, color: Int, bgColor: Int, frames: Int): ByteArray {
        val pixelData = mutableListOf<Byte>()
        for (f in 0 until frames) {
            for (y in 0 until matrixY) {
                for (x in 0 until matrixX) {
                    val pixel = if (x + f < byteArrayCache.size) {
                        val binaryString = byteArrayCache[x + f].toString(2).padStart(8, '0')
                        val bit = binaryString[7 - y].toString().toInt()
                        bit
                    } else {
                        0
                    }
                    if (pixel == 0) {
                        pixelData.add(Color.red(bgColor).toByte())
                        pixelData.add(Color.green(bgColor).toByte())
                        pixelData.add(Color.blue(bgColor).toByte())
                    } else {
                        pixelData.add(Color.red(color).toByte())
                        pixelData.add(Color.green(color).toByte())
                        pixelData.add(Color.blue(color).toByte())
                    }
                }
            }
        }
        return pixelData.toByteArray()
    }

    private fun calculateFrames(byteArraySize: Int, matrixX: Int): Int =
        if (byteArraySize <= matrixX) 1 else byteArraySize - matrixX + 1

}