package com.koonny.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: List<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn = itemView.findViewById<Button>(R.id.btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btn.text = data[position]
        holder.btn.setOnClickListener {
            mItemClick?.invoke(position)
        }
    }

    private var mItemClick: ((position: Int) -> Unit)? = null

    fun itemClick(block: (position: Int) -> Unit) {
        mItemClick = block
    }

    override fun getItemCount(): Int = data.size
}