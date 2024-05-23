package com.example.matrixcontrollercode.ui.kff

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.matrixcontrollercode.R
import com.example.matrixcontrollercode.data.KFFFile
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
        initTextInput()
        initColorInput()
        initAutoSpaceInput()
        initAutoEntryExitInput()
        initButtons()
        initSpeedInput()
    }


    private fun initTextInput() {
        val textInput = binding.editTextTextMultiLine
        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optionally implement this method if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Optionally implement this method if needed
            }

            override fun afterTextChanged(s: Editable?) {
                canBeSaved(false)
                canBeSent(false)
            }
        })
    }

    private fun initColorInput() {
        var red = 0
        var green = 0
        var blue = 0

        fun updateColorBall() {
            if (binding.switchColor.isChecked) {
                binding.textViewBackgroundColorBox.setTextColor(Color.rgb(red, green, blue))
            } else {
                binding.textViewColorBox.setTextColor(Color.rgb(red, green, blue))
            }
        }

        fun updateSeekBar() {
            var color = 0
            if(binding.switchColor.isChecked) {
                color = binding.textViewBackgroundColorBox.currentTextColor
            } else {
                color = binding.textViewColorBox.currentTextColor
            }
            binding.seekBarRed.progress = Color.red(color)
            binding.seekBarGreen.progress = Color.green(color)
            binding.seekBarBlue.progress = Color.blue(color)
        }

        fun updateSwitch() {
            if(binding.switchColor.isChecked) {
                binding.switchColor.thumbTintList = ColorStateList.valueOf(binding.textViewBackgroundColorBox.currentTextColor)
            } else {
                binding.switchColor.thumbTintList = ColorStateList.valueOf(binding.textViewColorBox.currentTextColor)
            }
        }

        val switchCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            updateSeekBar()
            updateSwitch()
        }


        val colorSeekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.let {
                    when (it.id) {
                        R.id.seekBarRed -> red = it.progress
                        R.id.seekBarGreen -> green = it.progress
                        R.id.seekBarBlue -> blue = it.progress
                    }
                    updateSwitch()
                    updateColorBall()
                    canBeSaved(false)
                    canBeSent(false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        with(binding) {
            seekBarRed.setOnSeekBarChangeListener(colorSeekBarListener)
            seekBarGreen.setOnSeekBarChangeListener(colorSeekBarListener)
            seekBarBlue.setOnSeekBarChangeListener(colorSeekBarListener)
            switchColor.setOnCheckedChangeListener(switchCheckedChangeListener)
        }
    }

    private fun initAutoSpaceInput() {
        var autoSpace = true // Default value
        binding.checkBoxAutoSpace.setOnCheckedChangeListener { _, isChecked ->
            autoSpace = isChecked
            canBeSaved(false)
            canBeSent(false)
        }
    }

    private fun initAutoEntryExitInput() {
        var autoEntry = true // Default value
        var autoExit = true // Default value
        binding.checkBoxAutoEntry.setOnCheckedChangeListener { _, isChecked ->
            autoEntry = isChecked
            canBeSaved(false)
            canBeSent(false)
        }
        binding.checkBoxAutoExit.setOnCheckedChangeListener { _, isChecked ->
            autoExit = isChecked
            canBeSaved(false)
            canBeSent(false)
        }
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
        var kffVersion = 0
        var standardWidth = 16
        var standardHeight = 8
        var standardSpeed = 10
        var maxWidth = 32
        var maxHeight = 32
        var kff = KFFFile(kffVersion.toUByte(), standardWidth.toUByte(), standardHeight.toUByte(), standardSpeed.toUByte(), byteArrayOf())
        var name = ""

        with(binding) {
            // Initially disable save and send buttons
            canBeSaved(false)
            canBeSent(false)

            buttonSave.setOnClickListener {
                if (kff.data.isNotEmpty()) {
                    val kffFileHelper = KFFFileHelper(context, name)
                    kffFileHelper.save(kff)
                }
            }

            buttonCalculate.setOnClickListener {
                name = editTextTextMultiLine.text.toString() // Filename
                var width = textViewMatrixX.text.toString().toIntOrNull() ?: standardWidth
                if (width >= maxWidth) width = standardWidth
                val height = textViewMatrixY.text.toString().toIntOrNull() ?: standardHeight
                if (height >= maxHeight) width = standardHeight
                val speed = seekBarSpeed.progress

                val byteArray = Text2KFF().convertToByteArray(
                    name,
                    checkBoxAutoEntry.isChecked,
                    checkBoxAutoExit.isChecked,
                    checkBoxAutoSpace.isChecked,
                    width,
                    height,
                    textViewColorBox.currentTextColor,
                    textViewBackgroundColorBox.currentTextColor
                )

                if (byteArray == null) {
                    Log.d("Text2KFF", "Error: got null back")
                } else {
                    Log.d("Text2KFF", "Success: got kff back")
                    kff.width = width.toUByte()
                    kff.height = height.toUByte()
                    kff.speed = speed.toUByte()
                    kff.data = byteArray
                    canBeSaved(true)
                    canBeSent(true)
                }
            }

            buttonSend.setOnClickListener {
                //WebSocketClientManager.send(kff)
            }
        }
    }

    fun canBeSaved(boolean: Boolean) {
        binding.buttonSave.isClickable = boolean
        binding.buttonSave.alpha = if (boolean) 1f else 0.5f
    }

    fun canBeSent(boolean: Boolean) {
        binding.buttonSend.isClickable = boolean
        binding.buttonSend.alpha = if (boolean) 1f else 0.5f
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
