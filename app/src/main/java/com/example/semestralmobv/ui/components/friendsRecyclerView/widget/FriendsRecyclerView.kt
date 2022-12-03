package com.example.semestralmobv.ui.components.friendsRecyclerView.widget

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralmobv.data.db.models.FriendItem
import com.example.semestralmobv.data.db.models.PubItem
import com.example.semestralmobv.ui.components.friendsRecyclerView.adapter.FriendsAdapter
import com.example.semestralmobv.ui.components.pubsRecyclerView.adapter.PubsAdapter
import com.example.semestralmobv.ui.components.pubsRecyclerView.widget.PubsRecyclerView

class FriendsRecyclerView : RecyclerView {
    private lateinit var friendsAdapter: FriendsAdapter

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        friendsAdapter = FriendsAdapter()
        adapter = friendsAdapter
    }
}

@BindingAdapter(value = ["friendItems"])
fun FriendsRecyclerView.applyItems(
    friendItems: List<FriendItem>?
) {
    (adapter as FriendsAdapter).items = friendItems ?: emptyList()
}

@BindingAdapter(value = ["navController"])
fun FriendsRecyclerView.applyItems(
    navController: NavController
) {
    (adapter as FriendsAdapter).nav = navController
}