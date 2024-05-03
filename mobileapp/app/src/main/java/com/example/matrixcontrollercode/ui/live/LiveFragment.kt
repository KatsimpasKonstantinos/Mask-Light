package com.example.matrixcontrollercode.ui.live

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.matrixcontrollercode.data.KFFFileHelper
import com.example.matrixcontrollercode.data.WebSocketClientManager
import com.example.matrixcontrollercode.databinding.FragmentLiveBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.*

class LiveFragment : Fragment() {

    private var _binding: FragmentLiveBinding? = null
    private val binding get() = _binding!!
    private var matrixData: ByteArray = byteArrayOf()
    private var matrixX: Int = 0
    private var matrixY: Int = 0
    private var speed: Int = 1
    private var currentFrame = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tablePadding = 9
        val textSize = 26F
        val textSizeSmall = 16F
        var activeFile = ""
        var frameTimer = Timer()
        var isPlaying = false
        var isLoaded = false

        val playButton = binding.buttonPlay
        playButton.isClickable = false
        playButton.alpha = 0.5f

        val sendButton = binding.buttonSend
        sendButton.isClickable = false
        sendButton.alpha = 0.5f

        fun fillTable() {
            binding.tableLayout.removeAllViewsInLayout()
            val frameSize = matrixX * matrixY * 3
            val frameStartIndex = currentFrame * frameSize
            if (frameStartIndex + frameSize <= matrixData.size) {
                for (y in 0 until matrixY) {
                    val row = TableRow(binding.tableLayout.context)
                    for (x in 0 until matrixX) {
                        val textView = TextView(binding.tableLayout.context)
                        textView.text = "█"
                        textView.textSize = textSize
                        val pixelIndex = frameStartIndex + (y * matrixX + x) * 3
                        if (pixelIndex + 2 < matrixData.size) {
                            val r = matrixData[pixelIndex].toInt() and 0xFF
                            val g = matrixData[pixelIndex + 1].toInt() and 0xFF
                            val b = matrixData[pixelIndex + 2].toInt() and 0xFF
                            textView.setTextColor(Color.rgb(r, g, b))
                        }
                        textView.setPadding(tablePadding, tablePadding, tablePadding, tablePadding)
                        row.addView(textView)
                    }
                    binding.tableLayout.addView(row)
                }
            }
        }


        fun tableNextFrame() {
            activity?.runOnUiThread {
                if (currentFrame + 1 < matrixData.size / (matrixX * matrixY * 3)) {
                    currentFrame++
                } else {
                    currentFrame = 0
                }
                fillTable()
            }
        }

        fun togglePlaying() {
            isPlaying = !isPlaying
            if (isPlaying && isLoaded) {
                playButton.text = "PAUSE"
                frameTimer = Timer()
                frameTimer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        tableNextFrame()
                    }
                }, 0, (1000 / speed).toLong())
                Log.d("Replay Speed", "${(1000 / speed).toLong()}ms")
            } else {
                playButton.text = "PLAY"
                frameTimer.cancel()
            }
        }
        binding.buttonPlay.setOnClickListener { togglePlaying() }

        binding.buttonLoad.setOnClickListener {
            val kffFileHelper = KFFFileHelper(context, activeFile)
            val data = kffFileHelper.load()
            try {
                data?.let {
                    if (it.size >= 3) {
                        matrixX = it[it.size - 3].toInt() and 0xFF
                        matrixY = it[it.size - 2].toInt() and 0xFF
                        speed = it[it.size - 1].toInt() and 0xFF
                        matrixData = it.copyOfRange(0, it.size - 3)
                        isLoaded = true
                        playButton.isClickable = true
                        playButton.alpha = 1f
                        sendButton.isClickable = true
                        sendButton.alpha = 1f
                        currentFrame = 0
                        fillTable()
                    }
                }
            } catch (exception: Exception) {
                Log.d("InterpretError", "Probably not a kff")
            }
        }

        val tableSave = binding.tableLayoutSaves
        var activeRow: TableRow = TableRow(context)

        fun updateSavedFiles() {//saved files get updated -> rebuild table rows
            tableSave.removeAllViewsInLayout()
            val kffFileHelper = KFFFileHelper(context, "")
            val files = kffFileHelper.findFiles()
            for (file in files.indices) {
                val row = TableRow(tableSave.context)

                val textView = TextView(tableSave.context)
                textView.text = files[file].toString().split("/").last().removeSuffix(".bin")
                textView.textSize = textSize
                textView.setTextColor(rgb(255,255,255))
                textView.setPadding(tablePadding, tablePadding, tablePadding, tablePadding)
                row.addView(textView)

                activeRow = row

                row.setOnClickListener{
                    activeRow.setBackgroundColor(rgb(30,30,30))
                    activeRow = row
                    activeRow.setBackgroundColor(rgb(100,100,100))
                    val textView = row.getChildAt(0) as TextView
                    activeFile = textView.text.toString()
                    Log.d("row", activeFile)
                }
                row.setBackgroundColor(rgb(30,30,30))
                tableSave.addView(row)
            }
        }

        updateSavedFiles()

        binding.buttonDelete.setOnClickListener {
            KFFFileHelper(context, activeFile).delete()
            activeFile = ""
            matrixData = byteArrayOf()
            isLoaded = false
            togglePlaying()
            updateSavedFiles()
            fillTable()
            playButton.isClickable = false
            playButton.alpha = 0.5f
            sendButton.isClickable = false
            sendButton.alpha = 0.5f
        }

        fun appendFooter(matrixData: ByteArray, matrixX: Int, matrixY: Int, speed: Int): ByteArray {
            val footer = byteArrayOf(matrixX.toByte(), matrixY.toByte(), speed.toByte())
            return matrixData + footer
        }

        binding.buttonSend.setOnClickListener {
            val finalData = appendFooter(matrixData, matrixX, matrixY, speed)
            WebSocketClientManager.send(finalData)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}