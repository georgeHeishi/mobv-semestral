package com.example.semestralmobv.ui.components.pubsRecyclerView.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import com.example.semestralmobv.R
import com.example.semestralmobv.data.db.models.PubItem
import com.example.semestralmobv.ui.fragments.pubs.FragmentPubsDirections
import com.example.semestralmobv.utils.autoNotify
import kotlin.properties.Delegates

class PubsAdapter(
) : RecyclerView.Adapter<PubsAdapter.ItemViewHolder>() {
    var items: List<PubItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n ->
            o.localId.compareTo(n.localId) == 0
        }
    }

    var nav: NavController? by Delegates.observable(null) { _, old, new -> old != new }

    class ItemViewHolder(
        private val parent: ViewGroup, view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.pub_list_item, parent, false
        )
    ) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.pub_item_name)
        val typeView: TextView = view.findViewById(R.id.pub_item_type)
        val usersView: TextView = view.findViewById(R.id.pub_item_users)
        val card: CardView = view.findViewById(R.id.pub_item_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val id = item.id

        holder.nameView.text = item.name
        holder.typeView.text = item.amenity
        holder.usersView.text = item.users.toString()
        holder.card.setOnClickListener {
            nav?.navigate(FragmentPubsDirections.actionToFragmentPubDetail(id))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}