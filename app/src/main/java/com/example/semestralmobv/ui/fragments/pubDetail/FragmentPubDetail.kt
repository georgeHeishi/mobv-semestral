package com.example.semestralmobv.ui.fragments.pubDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentPubDetailBinding
import com.example.semestralmobv.ui.viewmodels.PubDetailViewModel
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.ViewModelFactoryProvider

class FragmentPubDetail : Fragment() {
    private lateinit var pubDetailViewModel: PubDetailViewModel

    private var _binding: FragmentPubDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var animation: LottieAnimationView

    private lateinit var id: String
    private var destination: String? = null
    private var lat = ""
    private var long = ""
    private var pubName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        pubDetailViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[PubDetailViewModel::class.java]
        super.onCreate(savedInstanceState)
        id = arguments?.getString("id").toString()
        destination = arguments?.getString("destination").toString()
        pubDetailViewModel.loadPub(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val backButton = activity?.findViewById<ImageView>(R.id.back_button)
        backButton?.visibility = View.VISIBLE
        activity?.findViewById<TextView>(R.id.screen_title)?.text = ""
        val logoutButton = activity?.findViewById<ImageView>(R.id.logout)
        logoutButton?.visibility = View.VISIBLE

        super.onViewCreated(view, savedInstanceState)
        nav = view.findNavController()

        pubDetailViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = pubDetailViewModel
        }.also { bnd ->
            animation = bnd.animationView
            swipeContainer = bnd.swipeContainer

            pubDetailViewModel.loading.observe(
                viewLifecycleOwner
            ) {
                if (it == true) {
                    startLoading()
                } else {
                    stopLoading()
                }
            }

            pubDetailViewModel.pub.observe(viewLifecycleOwner) { pubDetail ->
                pubName = pubDetail.name.toString()
                lat = pubDetail.lat.toString()
                long = pubDetail.long.toString()

                bnd.pubName.text = pubDetail.name
                bnd.pubType.text = pubDetail.type
                bnd.pubUsers.text = pubDetail.users.toString()
                bnd.pubLat.text = pubDetail.lat.toString()
                bnd.pubLon.text = pubDetail.long.toString()
                pubDetail.tags?.phone.let {
                    bnd.pubPhone.text = it ?: ""
                }
                pubDetail.tags?.website.let {
                    bnd.pubWebsite.text = it ?: ""
                }
            }

            bnd.swipeContainer.setOnRefreshListener {
                pubDetailViewModel.refresh(id)
            }

            bnd.showButton.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:$lat,$long?q=$pubName")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

            backButton?.setOnClickListener {
                nav.popBackStack(
                    if (destination == "fiends") {
                        R.id.fragmentFriends
                    } else {
                        R.id.fragmentPubs
                    }, false
                )
            }

            logoutButton?.setOnClickListener {
                PreferencesData.getInstance().clearData(context)
                nav.navigate(R.id.action_to_login)
            }
        }

        pubDetailViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }
    }

    private fun stopLoading(
    ) {
        animation.cancelAnimation()
        swipeContainer.isRefreshing = false
    }

    private fun startLoading() {
        animation.playAnimation()
        swipeContainer.isRefreshing = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}