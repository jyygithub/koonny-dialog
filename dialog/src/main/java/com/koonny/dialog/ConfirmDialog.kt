package com.koonny.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.koonny.dialog.base.*

class ConfirmDialog : BaseDialogFragment(R.layout.koonny_dialog_confirm) {

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnPositive: Button
    private lateinit var btnNegative: Button
    private lateinit var dividerBtn: View

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mPositiveText: String? = null
    private var mNegativeText: String? = null

    private var mTitleConfig: (TextView.() -> Unit)? = null
    private var mMessageConfig: (TextView.() -> Unit)? = null
    private var mPositiveConfig: (Button.() -> Unit)? = null
    private var mNegativeConfig: (Button.() -> Unit)? = null

    private var mPositiveBlock: (() -> Unit)? = null
    private var mNegativeBlock: (() -> Unit)? = null

    override fun config(): DialogConfig {
        return super.config().apply {
            width = 0.8f
        }
    }

    override fun preView(rootView: View) {
        tvTitle = rootView.findViewById(R.id.tvTitle)
        tvMessage = rootView.findViewById(R.id.tvMessage)
        btnPositive = rootView.findViewById(R.id.btnPositive)
        btnNegative = rootView.findViewById(R.id.btnNegative)
        dividerBtn = rootView.findViewById(R.id.dividerBtn)
        (rootView.background.mutate() as GradientDrawable?)?.color = ColorStateList.valueOf(Color.WHITE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setMessage()
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

    private fun setMessage() {
        if (mMessage.isNullOrBlank()) {
            tvMessage.visibility = View.GONE
        } else {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = mMessage
        }
        mMessageConfig?.invoke(tvMessage)
    }

    private fun setButton() {
        dividerBtn.visibility = View.VISIBLE
        btnPositive.visibility = View.VISIBLE
        btnNegative.visibility = View.VISIBLE
        btnPositive.asBRRadii()
        btnNegative.asBLRadii()

        if (mPositiveText.isNullOrBlank()) {
            dividerBtn.visibility = View.GONE
            btnPositive.visibility = View.GONE
        }
        if (mNegativeText.isNullOrBlank()) {
            dividerBtn.visibility = View.GONE
            btnNegative.visibility = View.GONE
        }

        btnPositive.text = mPositiveText.toString()
        btnNegative.text = mNegativeText.toString()


        if (mPositiveText.isNullOrBlank() || mNegativeText.isNullOrBlank()) {
            btnNegative.asBottomRadii()
            btnPositive.asBottomRadii()
        }
        mPositiveConfig?.invoke(btnPositive)
        mNegativeConfig?.invoke(btnNegative)
        btnPositive.setOnClickListener {
            mPositiveBlock?.invoke()
            dismiss()
        }
        btnNegative.setOnClickListener {
            mNegativeBlock?.invoke()
            dismiss()
        }
    }

    fun title(title: String?, block: (TextView.() -> Unit)? = null): ConfirmDialog {
        mTitle = title
        mTitleConfig = block
        return this
    }

    fun message(message: String?, block: (TextView.() -> Unit)? = null): ConfirmDialog {
        mMessage = message
        mMessageConfig = block
        return this
    }

    fun positive(text: String?, block: (Button.() -> Unit)? = null): ConfirmDialog {
        mPositiveText = text
        mPositiveConfig = block
        return this
    }

    fun positiveClick(block: (() -> Unit)? = null): ConfirmDialog {
        mPositiveBlock = block
        return this
    }

    fun negative(text: String?, block: (Button.() -> Unit)? = null): ConfirmDialog {
        mNegativeText = text
        mNegativeConfig = block
        return this
    }

    fun negativeClick(block: (() -> Unit)? = null): ConfirmDialog {
        mNegativeBlock = block
        return this
    }

}