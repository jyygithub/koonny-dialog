package com.koonny.dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.R
import com.koonny.dialog.base.asRoundRadii

class BottomMenuAdapter : RecyclerView.Adapter<BottomMenuAdapter.ViewHolder>() {

    private var data = mutableListOf<Pair<String, Int>>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.koonny_dialog_menu_recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = data[position].first
        holder.ivIcon.setImageResource(data[position].second)
        holder.itemView.asRoundRadii()
        holder.itemView.setOnClickListener {
            items?.invoke(position)
        }
    }

    private var items: ((position: Int) -> Unit)? = null

    fun items(block: (position: Int) -> Unit) {
        this.items = block
    }

    fun setList(data: MutableList<Pair<String, Int>>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size
}