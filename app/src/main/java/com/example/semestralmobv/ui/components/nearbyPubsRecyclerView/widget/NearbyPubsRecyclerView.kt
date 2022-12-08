package com.example.semestralmobv.ui.components.nearbyPubsRecyclerView.widget

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralmobv.ui.components.nearbyPubsRecyclerView.adapter.NearbyPubsAdapter
import com.example.semestralmobv.ui.fragments.nearbyPubs.SelectPubAction
import com.example.semestralmobv.ui.viewmodels.NearbyPub

class NearbyPubsRecyclerView : RecyclerView {
    private lateinit var nearbyPubsAdapter: NearbyPubsAdapter
    var selectPubAction: SelectPubAction? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        nearbyPubsAdapter = NearbyPubsAdapter(object : SelectPubAction {
            override fun selectPub(pub: NearbyPub) {
                selectPubAction?.selectPub(pub)
            }
        })
        adapter = nearbyPubsAdapter
    }
}

@BindingAdapter(value = ["nearbyPubs"])
fun NearbyPubsRecyclerView.applyItems(
    nearbyPubs: List<NearbyPub>?
) {
    (adapter as NearbyPubsAdapter).items = nearbyPubs ?: emptyList()
}

@BindingAdapter(value = ["selectedId"])
fun NearbyPubsRecyclerView.applyItems(
    selectedId: String?
) {
    (adapter as NearbyPubsAdapter).selectedId = selectedId
}