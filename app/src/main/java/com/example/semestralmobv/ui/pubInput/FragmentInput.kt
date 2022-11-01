package com.example.semestralmobv.ui.pubInput

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.semestralmobv.databinding.FragmentInputBinding

class FragmentInput : Fragment() {
    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = binding.button

        button.setOnClickListener {
            val nav: NavController = view.findNavController()
            val action = transformUserValues()
            nav.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun transformUserValues(): FragmentInputDirections.ActionFragmentInputToFragmentDrink {
        val name = binding.nameInput.text.toString()
        val pubName = binding.pubNameInput.text.toString()
        val gpsLat = binding.pubLatInput.text.toString()
        val gpsLong = binding.pubLongInput.text.toString()

        return FragmentInputDirections.actionFragmentInputToFragmentDrink(
            name,
            pubName,
            gpsLat,
            gpsLong
        )
    }
}