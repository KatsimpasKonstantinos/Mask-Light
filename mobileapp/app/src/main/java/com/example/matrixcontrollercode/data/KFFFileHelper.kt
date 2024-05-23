package com.example.matrixcontrollercode.data

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream



class KFFFileHelper(
    private val context: Context?,
    private val fileName: String,
) {
    private val suffix = ".kff"
    private val supportedVersion = 0
    private val file: File by lazy {
        File(context?.filesDir, "$fileName$suffix")
    }

    fun save(kff: KFFFile) {
        FileOutputStream(file).use { output ->
            output.write(kff.serialize())
        }
        Log.d("Saved", "Saved file to: ${file.absolutePath}")
    }

    fun load(): KFFFile? {
        if (!file.exists()) {
            Log.d("Load", "File does not exist")
            return null
        }
        val byteArray = FileInputStream(file).use { input ->
            input.readBytes()
        }
        val kff = KFFFile.deserialize(byteArray)
        if (kff.version != supportedVersion.toUByte()) {
            Log.d("Load", "Invalid version")
            return null
        }
        return kff
    }

    fun findFiles(): List<File> {
        return context?.filesDir?.listFiles { _, name ->
            name.endsWith(suffix)
        }?.toList() ?: emptyList()
    }

    fun delete(): Boolean {
        return file.delete()
    }
}
