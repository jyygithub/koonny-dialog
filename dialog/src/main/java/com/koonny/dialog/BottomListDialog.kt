package com.koonny.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.adapter.BottomListAdapter
import com.koonny.dialog.base.BaseDialogFragment
import com.koonny.dialog.base.DialogConfig

class BottomListDialog<T : Any> : BaseDialogFragment(R.layout.koonny_dialog_bottom_list) {

    private lateinit var tvTitle: TextView
    private lateinit var btnNegative: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: BottomListAdapter<T>

    private var mTitle: CharSequence? = null
    private var mNegativeText: CharSequence? = "取消"

    private var mItems = mutableListOf<T>()
    private var mItemBlock: ((position: Int, item: T) -> Unit)? = null
    private var mItemBind: ((T) -> String)? = null

    private var mTitleConfig: (TextView.() -> Unit)? = null
    private var mNegativeConfig: (Button.() -> Unit)? = null
    private var mNegativeBlock: (() -> Unit)? = null

    override fun config(): DialogConfig {
        return super.config().apply {
            gravity = Gravity.BOTTOM
            style = R.style.Theme_Koonny_Dialog_Bottom
            width = 1f
            height = 0f
        }
    }

    override fun preView(rootView: View) {
        tvTitle = rootView.findViewById(R.id.tvTitle)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        btnNegative = rootView.findViewById(R.id.btnNegative)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setList()
        setButton()
    }

    private fun setTitle() {
        if (mTitle.isNullOrBlank()) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = mTitle
        }
        mTitleConfig?.invoke(tvTitle)
    }

    private fun setList() {
        mAdapter = BottomListAdapter(mItemBind)
        recyclerView.adapter = mAdapter
        mAdapter.setList(mItems)
        mAdapter.items {
            mItemBlock?.invoke(it, mAdapter.getItem(it))
            dismiss()
        }
    }

    private fun setButton() {
        if (mNegativeText.isNullOrBlank()) {
            btnNegative.visibility = View.GONE
        }
        mNegativeConfig?.invoke(btnNegative)
        btnNegative.text = mNegativeText.toString()
        btnNegative.setOnClickListener {
            mNegativeBlock?.invoke()
            dismiss()
        }
    }

    fun title(title: CharSequence?, block: (TextView.() -> Unit)? = null): BottomListDialog<T> {
        mTitle = title
        mTitleConfig = block
        return this
    }

    fun bind(bind: (T) -> String): BottomListDialog<T> {
        mItemBind = bind
        return this
    }

    fun items(vararg items: T, block: (position: Int, item: T) -> Unit): BottomListDialog<T> {
        mItems = items.toMutableList()
        this.mItemBlock = block
        return this
    }

    fun items(items: MutableList<T>, block: (position: Int, item: T) -> Unit): BottomListDialog<T> {
        mItems = items
        this.mItemBlock = block
        return this
    }

    fun negative(text: CharSequence?, block: (Button.() -> Unit)? = null): BottomListDialog<T> {
        mNegativeText = text
        mNegativeConfig = block
        return this
    }

    fun negativeClick(block: (() -> Unit)? = null): BottomListDialog<T> {
        mNegativeBlock = block
        return this
    }

}