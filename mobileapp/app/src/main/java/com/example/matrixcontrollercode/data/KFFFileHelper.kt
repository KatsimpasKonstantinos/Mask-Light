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
    // Ensure the file name ends with ".bin"
    private val file: File by lazy {
        File(context?.filesDir, "${fileName.takeIf { !it.endsWith(".bin") } ?: fileName.dropLast(4)}.bin")
    }

    fun save(data: ByteArray) {
        FileOutputStream(file).use { output ->
            output.write(data)
        }
        Log.d("Saved", "Saved file to: ${file.absolutePath}")
    }

    fun load(): ByteArray? {
        if (!file.exists()) {
            return null
        }
        return FileInputStream(file).use { input ->
            input.readBytes()
        }
    }

    fun findFiles(): List<File> {
        return context?.filesDir?.listFiles { _, name ->
            name.endsWith(".bin")
        }?.toList() ?: emptyList()
    }

    fun delete(): Boolean {
        return file.delete()
    }
}
