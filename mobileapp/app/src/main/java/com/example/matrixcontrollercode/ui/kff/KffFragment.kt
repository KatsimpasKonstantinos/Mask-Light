package com.example.matrixcontrollercode.ui.kff

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.matrixcontrollercode.R
import com.example.matrixcontrollercode.data.KFFFileHelper
import com.example.matrixcontrollercode.data.WebSocketClientManager
import com.example.matrixcontrollercode.databinding.FragmentKffBinding

class KffFragment : Fragment() {

    private var _binding: FragmentKffBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentKffBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        initColorInput()
        initAutoSpaceInput()
        initAutoEntryExitInput()
        initButtons()
        initSpeedInput()
    }

    private fun initColorInput() {
        var red = 0
        var green = 0
        var blue = 0

        fun updateColorBall() {
            binding.textViewColorBox.setTextColor(Color.rgb(red, green, blue))
        }

        val colorSeekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.let {
                    when (it.id) {
                        R.id.seekBarRed -> red = it.progress
                        R.id.seekBarGreen -> green = it.progress
                        R.id.seekBarBlue -> blue = it.progress
                    }
                    updateColorBall()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        with(binding) {
            seekBarRed.setOnSeekBarChangeListener(colorSeekBarListener)
            seekBarGreen.setOnSeekBarChangeListener(colorSeekBarListener)
            seekBarBlue.setOnSeekBarChangeListener(colorSeekBarListener)
        }
    }

    private fun initAutoSpaceInput() {
        var autoSpace = true // Default value
        binding.checkBoxAutoSpace.setOnCheckedChangeListener { _, isChecked ->
            autoSpace = isChecked
        }
    }

    private fun initAutoEntryExitInput() {
        var autoEntry = true // Default value
        var autoExit = true // Default value
        binding.checkBoxAutoEntry.setOnCheckedChangeListener { _, isChecked -> autoEntry = isChecked }
        binding.checkBoxAutoExit.setOnCheckedChangeListener { _, isChecked -> autoExit = isChecked }
    }

    private fun initSpeedInput() {
        val textViewSpeedValue = binding.textViewSpeed

        binding.seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the TextView to display the current speed
                textViewSpeedValue.text = "$progress fps"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Optionally implement this method if needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Optionally implement this method if needed
            }
        })
    }

    private fun initButtons() {
        var kff = ByteArray(0)
        var name = ""

        with(binding) {
            // Initially disable save and send buttons
            buttonSave.isClickable = false
            buttonSave.alpha = 0.5f
            buttonSend.isClickable = false
            buttonSend.alpha = 0.5f

            buttonSave.setOnClickListener {
                if (kff.isNotEmpty()) {
                    val kffFileHelper = KFFFileHelper(context, name)
                    kffFileHelper.save(kff)
                }
            }

            buttonCalculate.setOnClickListener {
                name = editTextTextMultiLine.text.toString() // Filename

                // Retrieve the values from matrixX and matrixY TextViews
                val matrixXValue = textViewMatrixX.text.toString().toIntOrNull() ?: 16
                val matrixYValue = textViewMatrixY.text.toString().toIntOrNull() ?: 8
                val speedValue = seekBarSpeed.progress

                val cache = Text2KFF().convertToBinary(
                    name,
                    checkBoxAutoEntry.isChecked,
                    checkBoxAutoExit.isChecked,
                    checkBoxAutoSpace.isChecked,
                    matrixXValue,
                    matrixYValue,
                    speedValue,
                    seekBarRed.progress,
                    seekBarGreen.progress,
                    seekBarBlue.progress
                )
                if (cache == null) {
                    Log.d("Text2KFF", "Error: got null back")
                } else {
                    Log.d("Text2KFF", "Success: got kff back")
                    kff = cache
                    buttonSave.isClickable = true
                    buttonSave.alpha = 1f
                    buttonSend.isClickable = true
                    buttonSend.alpha = 1f
                }
            }

            buttonSend.setOnClickListener {
                WebSocketClientManager.send(kff)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
