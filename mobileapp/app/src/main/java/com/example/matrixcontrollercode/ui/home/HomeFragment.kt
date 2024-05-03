package com.example.matrixcontrollercode.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matrixcontrollercode.data.WebSocketClientManager
import com.example.matrixcontrollercode.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonConnect.setOnClickListener {
            val url = binding.textURL.text.toString().trim()
            if(url.isNotEmpty()) {
                WebSocketClientManager.connect("ws://$url")
            } else {
                Log.d("HomeFragment", "URL is empty")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
