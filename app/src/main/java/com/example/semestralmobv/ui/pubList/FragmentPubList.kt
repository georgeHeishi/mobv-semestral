package com.example.semestralmobv.ui.pubList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
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
    private val pubsViewModel: PubsViewModel by viewModels()

    private var adapter: RecyclerView.Adapter<PubItemViewAdapter.ItemViewHolder>? = null

    //    private var pubsDeepCopy = PubsSingleton.pubs.map { it.copy() }.toMutableList()
    private var sortBy: SortBy = SortBy.DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.recyclerView
        val nav: NavController = view.findNavController()
        adapter = pubsViewModel.pubs.value?.let { PubItemViewAdapter(it, nav) }

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        pubsViewModel.pubs.observe(viewLifecycleOwner) { newPubs ->
            adapter = pubsViewModel.pubs.value?.let { PubItemViewAdapter(it, nav) }
            recyclerView.adapter = adapter
        }

        val addButton = binding.addButton
        addButton.setOnClickListener {
            nav.navigate(FragmentPubListDirections.actionPubListToFragmentInput())
        }

        val defaultSortChip: Chip = binding.defaultSortChip
        val nameSortChip: Chip = binding.nameSortChip
//        defaultSortChip.setOnClickListener {
//            if (sortBy === SortBy.DEFAULT) {
//                sortBy = SortBy.NAME
//                sortByName()
//            } else {
//                sortBy = SortBy.DEFAULT
//                deepCopyPubs()
//            }
//
//            adapter = PubItemViewAdapter(pubsDeepCopy, nav)
//            recyclerView.adapter = adapter
//            nameSortChip.isChecked = sortBy === SortBy.NAME
//        }
//
        nameSortChip.setOnClickListener {
            if (sortBy === SortBy.NAME) {
                sortBy = SortBy.DEFAULT
//                deepCopyPubs()
            } else {
                sortBy = SortBy.NAME
                sortByName()
            }

//            adapter = PubItemViewAdapter(pubsDeepCopy, nav)
//            recyclerView.adapter = adapter
//            defaultSortChip.isChecked = sortBy === SortBy.DEFAULT
        }
    }

    private fun sortByName() {
        pubsViewModel.pubs.value?.sortBy { it ->
            it.tags?.name.toString()
        }
        adapter?.notifyDataSetChanged()
    }
//
//    private fun deepCopyPubs() {
//        pubsDeepCopy = PubsSingleton.pubs.map { it.copy() }.toMutableList()
//    }
}
