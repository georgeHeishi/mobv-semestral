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
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.databinding.FragmentPubListBinding
import com.example.semestralmobv.ui.pubList.adapters.PubItemViewAdapter
import com.example.semestralmobv.data.pubs.viewmodel.PubsViewModel
import com.google.android.material.chip.Chip

class FragmentPubList : Fragment() {
    enum class SortBy {
        DEFAULT, NAME
    }

    private var _binding: FragmentPubListBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubsViewModel: PubsViewModel
    private lateinit var nav: NavController
    private lateinit var recyclerView: RecyclerView
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
        recyclerView = binding.recyclerView
        spinner = binding.spinner

        val idToRemove = arguments?.getString("idToRemove")
        idToRemove?.let { removePub(it) }

        setAdapter()
        recyclerView.setHasFixedSize(true)

        pubsViewModel.pubs.observe(viewLifecycleOwner) {
            setAdapter()
            stopLoading()
        }

        val addButton = binding.addButton
        addButton.setOnClickListener {
            nav.navigate(FragmentPubListDirections.actionPubListToFragmentInput())
        }

        val defaultSortChip: Chip = binding.defaultSortChip
        val nameSortChip: Chip = binding.nameSortChip

        defaultSortChip.setOnClickListener {
            startLoading()
            if (sortBy === SortBy.DEFAULT) {
                sortBy = SortBy.NAME
                sortByName()
            } else {
                sortBy = SortBy.DEFAULT
                setDefaultDataset()
            }
            nameSortChip.isChecked = sortBy === SortBy.NAME
        }

        nameSortChip.setOnClickListener {
            startLoading()
            if (sortBy === SortBy.NAME) {
                sortBy = SortBy.DEFAULT
                setDefaultDataset()
            } else {
                sortBy = SortBy.NAME
                sortByName()
            }

            defaultSortChip.isChecked = sortBy === SortBy.DEFAULT
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
