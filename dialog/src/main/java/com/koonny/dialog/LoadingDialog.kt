package com.koonny.dialog

import com.koonny.dialog.base.BaseBottomSheetDialogFragment
import com.koonny.dialog.databinding.DialogLoadingBinding

class LoadingDialog private constructor() : BaseBottomSheetDialogFragment<DialogLoadingBinding>(DialogLoadingBinding::inflate) {

    private var mMessage = "正在加载..."

    fun setMessage(text: String): LoadingDialog {
        mMessage = text
        return this
    }

    override fun onPrepareWidget() {
        super.onPrepareWidget()
        binding.tv.text = mMessage
    }

    companion object {
        fun get(): LoadingDialog {
            return LoadingDialog()
        }
    }

}