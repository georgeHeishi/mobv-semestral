package com.example.semestralmobv.ui.pubList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.databinding.FragmentPubListBinding
import com.example.semestralmobv.ui.pubList.adapters.PubItemViewAdapter
import com.example.semestralmobv.viewmodels.PubsViewModel
import com.google.android.material.chip.Chip

class FragmentPubList : Fragment() {
    enum class SortBy {
        DEFAULT, NAME
    }

    private var _binding: FragmentPubListBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var pubsViewModel: PubsViewModel
    private lateinit var nav: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var defaultSortChip: Chip
    private lateinit var nameSortChip: Chip
    private var adapter: RecyclerView.Adapter<PubItemViewAdapter.ItemViewHolder>? = null
    private lateinit var spinner: LottieAnimationView
    private var sortBy: SortBy = SortBy.DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { pubsViewModel = ViewModelProvider(it)[PubsViewModel::class.java] }
        nav = view.findNavController()
        swipeContainer = binding.swipeContainer
        recyclerView = binding.recyclerView
        spinner = binding.spinner

        val idToRemove = arguments?.getString("idToRemove")
        idToRemove?.let { removePub(it) }

        setAdapter()
        recyclerView.setHasFixedSize(true)

        pubsViewModel.pubs.observe(viewLifecycleOwner) {
            setAdapter()
            stopLoading()
            defaultSortChip.isChecked = true
            nameSortChip.isChecked = false
        }

        swipeContainer.setOnRefreshListener {
            pubsViewModel.getPubs()
        }

        val addButton = binding.addButton
        addButton.setOnClickListener {
//            UNCOMMENT WHEN NEEDED
//            nav.navigate(FragmentPubListDirections.actionPubListToFragmentInput())
        }

        defaultSortChip = binding.defaultSortChip
        nameSortChip = binding.nameSortChip

        defaultSortChip.setOnClickListener {
            startLoading()
            sortBy = SortBy.DEFAULT
            setDefaultDataset()

            defaultSortChip.isChecked = true
            nameSortChip.isChecked = false
        }

        nameSortChip.setOnClickListener {
            startLoading()
            sortBy = SortBy.NAME
            sortByName()

            nameSortChip.isChecked = true
            defaultSortChip.isChecked = false
        }
    }

    private fun sortByName() {
        pubsViewModel.pubs.value?.sortBy { it ->
            it.tags?.name.toString()
        }
        adapter?.notifyDataSetChanged()
        stopLoading()
    }

    private fun setDefaultDataset() {
        pubsViewModel.getPubs()
    }

    private fun setAdapter() {
        adapter = pubsViewModel.pubs.value?.let { PubItemViewAdapter(it, nav) }
        recyclerView.adapter = adapter
    }

    private fun stopLoading() {
        spinner.isVisible = false
        spinner.cancelAnimation()
        recyclerView.isVisible = true
        swipeContainer.isRefreshing = false
    }

    private fun startLoading() {
        spinner.isVisible = true
        spinner.playAnimation()
        recyclerView.isVisible = false
    }

    private fun removePub(id: String) {
        val removeIndex = pubsViewModel.pubs.value?.indexOfFirst { it.id == id }
        if (removeIndex != null) {
            pubsViewModel.pubs.value?.removeAt(removeIndex)
            adapter?.notifyItemRemoved(removeIndex)
        }
    }
}
