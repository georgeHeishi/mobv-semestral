package com.example.semestralmobv.ui.pubList.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import com.example.semestralmobv.R
import com.example.semestralmobv.api.models.Pub
import com.example.semestralmobv.ui.pubList.FragmentPubListDirections

class PubItemViewAdapter(
    private val dataset: MutableList<Pub>,
    private val nav: NavController
) : RecyclerView.Adapter<PubItemViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.pub_item_name)
        val typeView: TextView = view.findViewById(R.id.pub_item_type)
        val card: CardView = view.findViewById(R.id.pub_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //Remember that a view holder represents a single list item view.
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.pub_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val id = item.id
        holder.nameView.text = item.tags?.name
        holder.typeView.text = item.tags?.amenity
        holder.card.setOnClickListener {
            nav.navigate(FragmentPubListDirections.actionPubListToFragmentPubInfo(id))
        }
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }
}