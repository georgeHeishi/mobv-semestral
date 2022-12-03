package com.example.semestralmobv.ui.components.friendsRecyclerView.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralmobv.R
import com.example.semestralmobv.data.db.models.FriendItem
import com.example.semestralmobv.ui.fragments.pubs.FragmentPubsDirections
import com.example.semestralmobv.utils.autoNotify
import kotlin.properties.Delegates

class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>() {
    var items: List<FriendItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n ->
            o.localId.compareTo(n.localId) == 0
        }
    }
    var nav: NavController? by Delegates.observable(null) { _, old, new -> old != new }


    class ItemViewHolder(
        private val parent: ViewGroup, view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.frient_list_item, parent, false
        )
    ) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.friend_item_name)
        val pubView: TextView = view.findViewById(R.id.friend_item_pub_name)
        val card: ConstraintLayout = view.findViewById(R.id.nearby_pub_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.nameView.text = item.userName
        holder.pubView.text = item.pubName
        if (item.pubId != null && item.pubId.isNotBlank()) {
            holder.card.setOnClickListener {
                nav?.navigate(FragmentPubsDirections.actionToFragmentPubDetail(item.pubId, "fiends"))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}