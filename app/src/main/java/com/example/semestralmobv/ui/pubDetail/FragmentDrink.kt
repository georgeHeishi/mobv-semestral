package com.example.semestralmobv.ui.pubDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.databinding.FragmentDrinkBinding

class FragmentDrink : Fragment() {
    private var _binding: FragmentDrinkBinding? = null
    private val binding get() = _binding!!


    private var name = ""
    private var pubName = ""
    private var gpsLat = ""
    private var gpsLong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString("name").toString()
        pubName = arguments?.getString("pubName").toString()
        gpsLat = arguments?.getString("gpsLat").toString()
        gpsLong = arguments?.getString("gpsLong").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameView: TextView = binding.name
        val pubNameView: TextView = binding.pubName

        nameView.text = name
        pubNameView.text = pubName

        val animationView: LottieAnimationView = binding.animationView
        animationView.playAnimation()

        val playButton: Button = binding.showButton
        playButton.setOnClickListener {
            animationView.cancelAnimation()

            //TODO: sanitize user input
            val gmmIntentUri = Uri.parse("geo:$gpsLat,$gpsLong?q=$pubName")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}