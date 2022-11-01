package com.example.semestralmobv.ui.pubDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.semestralmobv.databinding.FragmentPubInfoBinding
import com.example.semestralmobv.data.pubs.model.Pub
import com.example.semestralmobv.data.pubs.viewmodel.PubsViewModel

class FragmentPubInfo : Fragment() {
    private var _binding: FragmentPubInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubsViewModel: PubsViewModel
    private lateinit var nav: NavController
    private lateinit var pub: Pub

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { pubsViewModel = ViewModelProvider(it)[PubsViewModel::class.java] }
        nav = view.findNavController()

        val id = arguments?.getString("id").toString()
        val foundPub = pubsViewModel.pubs.value?.find { it.id == id }

        if (foundPub === null) {
            nav.navigate(FragmentPubInfoDirections.actionFragmentPubInfoToPubList(null))
            return
        }

        pub = foundPub

        binding.infoPubName.text = pub.tags?.name
        binding.infoPubType.text = pub.tags?.amenity
        binding.infoPubLat.text = pub.lat.toString()
        binding.infoPubLon.text = pub.lon.toString()
        binding.infoPubPhone.text = pub.tags?.phone
        binding.infoPubWebsite.text = pub.tags?.website

        val deleteButton: Button = binding.deleteButton
        deleteButton.setOnClickListener {
            nav.navigate(FragmentPubInfoDirections.actionFragmentPubInfoToPubList(pub.id))
        }
    }
}