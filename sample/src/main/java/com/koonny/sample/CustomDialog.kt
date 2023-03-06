package com.koonny.sample

import android.os.Bundle
import android.view.Gravity
import android.view.View
import coil.load
import com.koonny.dialog.base.BaseDialogFragment
import com.koonny.dialog.base.DialogConfig
import com.koonny.sample.databinding.DialogCustomBinding

class CustomDialog : BaseDialogFragment(R.layout.dialog_custom) {

    private lateinit var binding: DialogCustomBinding

    override fun config(): DialogConfig {
        return super.config().apply {
            width = 0.7f
            gravity = Gravity.CENTER
            dismissOnBackPressed = false
            dismissOnTouchOutside = false
        }
    }

    override fun preView(rootView: View) {
        binding = DialogCustomBinding.bind(rootView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.load("https://s1.ax1x.com/2023/03/01/ppia5Wj.png")
        binding.ivClose.setOnClickListener { dismiss() }
    }

}