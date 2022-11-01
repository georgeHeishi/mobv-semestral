package com.example.semestralmobv.ui.pubDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.semestralmobv.databinding.FragmentPubInfoBinding
import com.example.semestralmobv.data.pubs.model.Pub
import com.example.semestralmobv.data.pubs.viewmodel.PubsViewModel

class FragmentPubInfo : Fragment() {
    private var _binding: FragmentPubInfoBinding? = null
    private val binding get() = _binding!!
    private val pubsViewModel: PubsViewModel by viewModels()

    private var id: String = ""
    private lateinit var pub: Pub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getString("id").toString()

        val foundPub = pubsViewModel.pubs.value?.find { it.id == id }

        if (foundPub === null) {
            pub = Pub(id, 0.0f, 0.0f, null)
            return
        }

        pub = foundPub
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.infoPubName.text = pub.tags?.name
        binding.infoPubType.text = pub.tags?.amenity
        binding.infoPubLat.text = pub.lat.toString()
        binding.infoPubLon.text = pub.lon.toString()
        binding.infoPubPhone.text = pub.tags?.phone
        binding.infoPubWebsite.text = pub.tags?.website

        val deleteButton: Button = binding.deleteButton
        val nav: NavController = view.findNavController()
//        deleteButton.setOnClickListener {
//            PubsSingleton.pubs.remove(pub)
//            nav.navigate(FragmentPubInfoDirections.actionFragmentPubInfoToPubList())
//        }
    }
}