package com.koonny.dialog

import com.koonny.dialog.base.BaseBottomSheetDialogFragment
import com.koonny.dialog.databinding.DialogConfirmBinding

class ConfirmDialog private constructor() : BaseBottomSheetDialogFragment<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private var mMessage: String = "提示消息"
    private var mPositive: String = "确定"
    private var mNegative: String = "取消"
    private var mPositiveBlock: (() -> Unit)? = null
    private var mNegativeBlock: (() -> Unit)? = null

    fun setMessage(text: String): ConfirmDialog {
        mMessage = text
        return this
    }

    fun setPositive(text: String): ConfirmDialog {
        mPositive = text
        return this
    }

    fun positiveClick(block: () -> Unit): ConfirmDialog {
        mPositiveBlock = block
        return this
    }

    fun setNegative(text: String): ConfirmDialog {
        mNegative = text
        return this
    }

    fun negativeClick(block: () -> Unit): ConfirmDialog {
        mNegativeBlock = block
        return this
    }

    override fun onPrepareWidget() {
        super.onPrepareWidget()
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
        fun get(): ConfirmDialog {
            return ConfirmDialog()
        }
    }

}