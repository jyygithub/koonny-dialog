package com.koonny.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.adapter.BottomMenuAdapter
import com.koonny.dialog.base.BaseDialogFragment
import com.koonny.dialog.base.DialogConfig

class BottomMenuDialog : BaseDialogFragment(R.layout.koonny_dialog_bottom_menu) {

    private lateinit var tvTitle: TextView
    private lateinit var btnNegative: Button
    private lateinit var recyclerView: RecyclerView
    private val mAdapter = BottomMenuAdapter()

    private var mTitle: CharSequence? = null
    private var mNegativeText: CharSequence? = "取消"

    private var mItems = emptyList<Pair<String, Int>>()
    private var mItemBlock: ((position: Int) -> Unit)? = null

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
        recyclerView.adapter = mAdapter
        mAdapter.setList(mItems)
        mAdapter.items {
            mItemBlock?.invoke(it)
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

    fun title(title: CharSequence?, block: (TextView.() -> Unit)? = null): BottomMenuDialog {
        mTitle = title
        mTitleConfig = block
        return this
    }

    fun items(vararg items: Pair<String, Int>, block: (position: Int) -> Unit): BottomMenuDialog {
        mItems = items.toList()
        this.mItemBlock = block
        return this
    }

    fun items(items: List<Pair<String, Int>>, block: (position: Int) -> Unit): BottomMenuDialog {
        mItems = items
        this.mItemBlock = block
        return this
    }

    fun negative(text: CharSequence?, block: (Button.() -> Unit)? = null): BottomMenuDialog {
        mNegativeText = text
        mNegativeConfig = block
        return this
    }

    fun negativeClick(block: (() -> Unit)? = null): BottomMenuDialog {
        mNegativeBlock = block
        return this
    }

}