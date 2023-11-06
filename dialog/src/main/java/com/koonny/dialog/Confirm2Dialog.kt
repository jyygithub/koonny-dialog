package com.koonny.dialog

import com.koonny.dialog.base.BaseBottomSheetDialogFragment
import com.koonny.dialog.databinding.DialogConfirm2Binding

class Confirm2Dialog private constructor() : BaseBottomSheetDialogFragment<DialogConfirm2Binding>(DialogConfirm2Binding::inflate) {

    private var mTitle: String = "提示标题"
    private var mMessage: String = "提示消息"
    private var mPositive: String = "确定"
    private var mNegative: String = "取消"
    private var mPositiveBlock: (() -> Unit)? = null
    private var mNegativeBlock: (() -> Unit)? = null

    fun setTitle(text: String): Confirm2Dialog {
        mTitle = text
        return this
    }

    fun setMessage(text: String): Confirm2Dialog {
        mMessage = text
        return this
    }

    fun setPositive(text: String): Confirm2Dialog {
        mPositive = text
        return this
    }

    fun positiveClick(block: () -> Unit): Confirm2Dialog {
        mPositiveBlock = block
        return this
    }

    fun setNegative(text: String): Confirm2Dialog {
        mNegative = text
        return this
    }

    fun negativeClick(block: () -> Unit): Confirm2Dialog {
        mNegativeBlock = block
        return this
    }

    override fun onPrepareWidget() {
        super.onPrepareWidget()
        binding.tvTitle.text = mTitle
        binding.tvMessage.text = mMessage
        binding.tvPositive.text = mPositive
        binding.tvNegative.text = mNegative
        binding.tvNegative.setOnClickListener {
            mNegativeBlock?.invoke()
            dismiss()
        }
        binding.tvPositive.setOnClickListener {
            mPositiveBlock?.invoke()
            dismiss()
        }
    }

    companion object {
        fun get(): Confirm2Dialog {
            return Confirm2Dialog()
        }
    }

}