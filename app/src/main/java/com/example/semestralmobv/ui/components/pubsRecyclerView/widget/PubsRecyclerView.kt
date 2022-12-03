package com.example.semestralmobv.ui.components.pubsRecyclerView.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralmobv.data.db.models.PubItem
import com.example.semestralmobv.ui.components.pubsRecyclerView.adapter.PubsAdapter

class PubsRecyclerView : RecyclerView {
    private lateinit var pubsAdapter: PubsAdapter

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        pubsAdapter = PubsAdapter()
        adapter = pubsAdapter
    }
}


@BindingAdapter(value = ["pubItems"])
fun PubsRecyclerView.applyItems(
    pubItems: List<PubItem>?
) {
    (adapter as PubsAdapter).items = pubItems ?: emptyList()
}

@BindingAdapter(value = ["navController"])
fun PubsRecyclerView.applyItems(
    navController: NavController
) {
    (adapter as PubsAdapter).nav = navController
}