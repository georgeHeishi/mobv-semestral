package com.example.semestralmobv.ui.components.nearbyPubsRecyclerView.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralmobv.R
import com.example.semestralmobv.ui.fragments.nearbyPubs.SelectPubAction
import com.example.semestralmobv.ui.viewmodels.NearbyPub
import com.example.semestralmobv.utils.autoNotify
import kotlin.properties.Delegates

class NearbyPubsAdapter(val selectPubAction: SelectPubAction? = null) : RecyclerView.Adapter<NearbyPubsAdapter.ItemViewHolder>() {
    var items: List<NearbyPub> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n ->
            o.id.compareTo(n.id) == 0
        }
    }

    class ItemViewHolder(
        private val parent: ViewGroup, view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.nearby_pub_list_item, parent, false
        )
    ) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.nearest_pub_item_name)
        val typeView: TextView = view.findViewById(R.id.nearest_pub_item_type)
        val usersView: TextView = view.findViewById(R.id.nearby_pub_item_distance)
        val card: CardView = view.findViewById(R.id.nearby_pub_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.nameView.text = item.name
        holder.typeView.text = item.type
        holder.usersView.text = "%.2f m".format(item.distance)
        holder.card.setOnClickListener {
            selectPubAction?.selectPub(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}