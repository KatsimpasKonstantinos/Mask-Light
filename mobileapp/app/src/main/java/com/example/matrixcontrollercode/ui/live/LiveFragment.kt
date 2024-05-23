package com.example.matrixcontrollercode.ui.live

import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.matrixcontrollercode.data.KFFFile
import com.example.matrixcontrollercode.data.KFFFileHelper
import com.example.matrixcontrollercode.data.WebSocketClientManager
import com.example.matrixcontrollercode.databinding.FragmentLiveBinding
import java.util.*

class LiveFragment : Fragment() {

    private var _binding: FragmentLiveBinding? = null
    private val binding get() = _binding!!
    private var byteArray: ByteArray = byteArrayOf()
    private var matrixX: Int = 16
    private var matrixY: Int = 8
    private var speed: Int = 10
    private var currentFrame = 0
    private var supportedVersion = 0
    private var frameHandler: Handler? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tablePadding = 1
        val textSize = 26F
        val tableTextSize = 18F
        val textSizeSmall = 16F
        var activeFile = ""
        var isPlaying = false
        var isLoaded = false



        val playButton = binding.buttonPlay
        fun canBePlayed(boolean: Boolean) {
            playButton.isClickable = boolean
            playButton.alpha = if (boolean) 1f else 0.5f
        }
        canBePlayed(false)

        val sendButton = binding.buttonSend
        fun canBeSend(boolean: Boolean) {
            sendButton.isClickable = boolean
            sendButton.alpha = if (boolean) 1f else 0.5f
        }
        canBeSend(false)

        val tableLayout = binding.tableLayout

        fun fillTable() {
            val frameSize = matrixX * matrixY * 3
            val frameStartIndex = currentFrame * frameSize

            if (frameStartIndex + frameSize > byteArray.size) return

            for (y in 0 until matrixY) {
                val row: TableRow
                if (y < tableLayout.childCount) {
                    row = tableLayout.getChildAt(y) as TableRow
                } else {
                    row = TableRow(tableLayout.context)
                    tableLayout.addView(row)
                }
                for (x in 0 until matrixX) {
                    val textView: TextView
                    if (x < row.childCount) {
                        textView = row.getChildAt(x) as TextView
                    } else {
                        textView = TextView(tableLayout.context)
                        textView.text = "██"
                        textView.textSize = tableTextSize
                        textView.setPadding(tablePadding, tablePadding, tablePadding, tablePadding)
                        row.addView(textView)
                    }
                    val pixelIndex = frameStartIndex + (y * matrixX + x) * 3
                    if (pixelIndex + 2 < byteArray.size) {
                        val r = byteArray[pixelIndex].toInt() and 0xFF
                        val g = byteArray[pixelIndex + 1].toInt() and 0xFF
                        val b = byteArray[pixelIndex + 2].toInt() and 0xFF
                        textView.setTextColor(Color.rgb(r, g, b))
                    }
                }
                while (row.childCount > matrixX) {
                    row.removeViewAt(row.childCount - 1)
                }
            }
            while (tableLayout.childCount > matrixY) {
                tableLayout.removeViewAt(tableLayout.childCount - 1)
            }
        }

        fun tableNextFrame() {
            activity?.runOnUiThread {
                if (currentFrame + 1 < byteArray.size / (matrixX * matrixY * 3)) {
                    currentFrame++
                } else {
                    currentFrame = 0
                }
                fillTable()
            }
        }

        fun togglePlaying(boolean: Boolean) {
            isPlaying = boolean
            if (isPlaying && isLoaded) {
                playButton.text = "PAUSE"
                frameHandler = Handler(Looper.getMainLooper())
                val frameUpdateRunnable = object : Runnable {
                    override fun run() {
                        tableNextFrame()
                        frameHandler?.postDelayed(this, (1000 / speed).toLong())
                    }
                }
                frameHandler?.post(frameUpdateRunnable)
                Log.d("Replay Speed", "${(1000 / speed).toLong()}ms")
            } else {
                frameHandler?.removeCallbacksAndMessages(null)
                playButton.text = "PLAY"
            }
        }
        binding.buttonPlay.setOnClickListener { togglePlaying(!isPlaying) }

        fun displayError(message: String) {
            Log.d("InterpretError", "Corrupted file or not supported version")
            //clear all table and print error message
            tableLayout.removeAllViewsInLayout()
            val row = TableRow(tableLayout.context)
            val textView = TextView(tableLayout.context)
            textView.text = message
            textView.textSize = textSizeSmall
            textView.setPadding(tablePadding, tablePadding, tablePadding, tablePadding)
            row.addView(textView)
            tableLayout.addView(row)
        }

        binding.buttonLoad.setOnClickListener {
            try {
                togglePlaying(false)
                isLoaded = false
                val kffFileHelper = KFFFileHelper(context, activeFile)
                val kff = kffFileHelper.load()
                if (kff == null) {
                    displayError("Corrupted file or not supported version")
                    return@setOnClickListener
                }
                kff.let {
                    if (it.data.isNotEmpty()) {
                        matrixX = it.width.toInt()
                        matrixY = it.height.toInt()
                        speed = it.speed.toInt()
                        byteArray = it.data
                        isLoaded = true
                        canBePlayed(true)
                        canBeSend(true)
                        currentFrame = 0
                        tableLayout.removeAllViewsInLayout()
                        fillTable()
                    }
                }
            } catch (exception: Exception) {
                displayError("There was an error while loading the file")
            }
        }

        val tableSave = binding.tableLayoutSaves
        var activeRow: TableRow = TableRow(context)

        fun updateSavedFiles() {//saved files get updated -> rebuild table rows
            tableSave.removeAllViewsInLayout()
            val kffFileHelper = KFFFileHelper(context, "")
            val files = kffFileHelper.findFiles()
            for (file in files.indices) {
                try {
                    val row = TableRow(tableSave.context)

                    val textView = TextView(tableSave.context)
                    textView.text = files[file].toString().split("/").last().removeSuffix(".kff")
                    textView.textSize = textSize
                    textView.setPadding(tablePadding, tablePadding, tablePadding, tablePadding)
                    row.addView(textView)
                    activeRow = row

                    if (KFFFile.deserialize(files[file].readBytes()).version.toInt() != supportedVersion) {
                        textView.setTextColor(rgb(255,0,0))
                    } else {
                        textView.setTextColor(rgb(255,255,255))
                    }

                    row.setBackgroundColor(rgb(30,30,30))

                    row.setOnClickListener{
                        activeRow.setBackgroundColor(rgb(30,30,30))
                        activeRow = row
                        activeRow.setBackgroundColor(rgb(100,100,100))
                        val textView = row.getChildAt(0) as TextView
                        activeFile = textView.text.toString()
                        Log.d("row", activeFile)
                    }

                    tableSave.addView(row)
                } catch (e: Exception) {
                    Log.d("Error", "Error while reading file")
                }
            }
        }

        updateSavedFiles()



        binding.buttonDelete.setOnClickListener {
            KFFFileHelper(context, activeFile).delete()
            activeFile = ""
            byteArray = byteArrayOf()
            isLoaded = false
            togglePlaying(false)
            updateSavedFiles()
            fillTable()
            canBePlayed(false)
            canBeSend(false)
        }



        fun appendFooter(matrixData: ByteArray, matrixX: Int, matrixY: Int, speed: Int): ByteArray {
            val footer = byteArrayOf(matrixX.toByte(), matrixY.toByte(), speed.toByte())
            return matrixData + footer
        }

        binding.buttonSend.setOnClickListener {
            val finalData = appendFooter(byteArray, matrixX, matrixY, speed)
            WebSocketClientManager.send(finalData)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}