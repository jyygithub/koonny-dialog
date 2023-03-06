package com.koonny.dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.R

class BottomListAdapter<T : Any>(private val bind: ((T) -> String)?) : RecyclerView.Adapter<BottomListAdapter.ViewHolder>() {

    private var data = emptyList<T>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn = itemView.findViewById<TextView>(R.id.btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.koonny_dialog_list_recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (bind == null) holder.btn.text = data[position].toString()
        else holder.btn.text = bind.invoke(data[position])
        holder.btn.setOnClickListener {
            items?.invoke(position)
        }
    }

    private var items: ((position: Int) -> Unit)? = null

    fun getItem(position: Int): T = data[position]

    fun items(block: (position: Int) -> Unit) {
        this.items = block
    }

    fun setList(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size
}