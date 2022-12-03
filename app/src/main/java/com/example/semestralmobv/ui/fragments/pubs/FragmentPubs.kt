package com.example.semestralmobv.ui.fragments.pubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentPubsBinding
import com.example.semestralmobv.ui.viewmodels.PubsViewModel
import com.example.semestralmobv.ui.viewmodels.SortBy
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentPubs : Fragment() {
    private lateinit var pubsViewModel: PubsViewModel

    private var _binding: FragmentPubsBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var spinner: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        pubsViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[PubsViewModel::class.java]

        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = view.findNavController()

        val foundUser = PreferencesData.getInstance().getUserItem(requireContext())
        if ((foundUser?.uid ?: "").isBlank()) {
            nav.navigate(R.id.action_to_login)
            return
        }

        pubsViewModel.setSortBy(SortBy.DEFAULT)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = pubsViewModel
            navController = nav
        }.also { bnd ->
            swipeContainer = bnd.swipeContainer
            spinner = bnd.spinner

            swipeContainer.setOnRefreshListener {
                pubsViewModel.refreshPubsFromRepository()
            }

            bnd.defaultSortChip.setOnClickListener {
                bnd.recycleView.scrollToPosition(0)
                pubsViewModel.setSortBy(SortBy.DEFAULT)
            }

            bnd.nameSortChip.setOnClickListener {
                bnd.recycleView.scrollToPosition(0)
                pubsViewModel.setSortBy(SortBy.NAME)
            }
        }

        pubsViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }

        pubsViewModel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                startLoading()
            } else {
                stopLoading()
            }
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
