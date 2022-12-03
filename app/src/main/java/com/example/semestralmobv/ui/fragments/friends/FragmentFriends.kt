package com.example.semestralmobv.ui.fragments.friends

import android.annotation.SuppressLint
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
import com.example.semestralmobv.databinding.FragmentFriendsBinding
import com.example.semestralmobv.databinding.FragmentPubsBinding
import com.example.semestralmobv.ui.viewmodels.FriendsViewModel
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentFriends : Fragment() {
    private lateinit var friendsViewModel: FriendsViewModel

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var spinner: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        friendsViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[FriendsViewModel::class.java]
        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility =
            View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = view.findNavController()
        initializeToolbar()

        val foundUser = PreferencesData.getInstance().getUserItem(requireContext())
        if ((foundUser?.uid ?: "").isBlank()) {
            nav.navigate(R.id.action_to_login)
            return
        }

        friendsViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = friendsViewModel
            navController = nav
        }.also { bnd ->
            swipeContainer = bnd.swipeContainer
            spinner = bnd.spinner

            swipeContainer.setOnRefreshListener {
                friendsViewModel.refreshData()
            }

            bnd.addFriends.setOnClickListener {
                nav.navigate(R.id.action_to_add_friend)
            }
        }

        friendsViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }

        friendsViewModel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                startLoading()
            } else {
                stopLoading()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initializeToolbar() {
        activity?.findViewById<ImageView>(R.id.back_button)?.visibility = View.GONE
        activity?.findViewById<TextView>(R.id.screen_title)?.text = "Friends"
        val logoutButton = activity?.findViewById<ImageView>(R.id.logout)
        logoutButton?.visibility = View.VISIBLE
        logoutButton?.setOnClickListener {
            PreferencesData.getInstance().clearData(context)
            nav.navigate(R.id.action_to_login)
        }
    }

    private fun stopLoading(
    ) {
        spinner.cancelAnimation()
        swipeContainer.isRefreshing = false
    }

    private fun startLoading() {
        spinner.playAnimation()
        swipeContainer.isRefreshing = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}