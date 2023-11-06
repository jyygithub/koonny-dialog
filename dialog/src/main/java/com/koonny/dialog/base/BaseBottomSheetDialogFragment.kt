package com.koonny.dialog.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koonny.dialog.R

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet = findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.decorView?.setPadding(resources.getDimensionPixelOffset(R.dimen.dp_16))
        dialog?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
            setGravity(Gravity.BOTTOM)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
        onPrepareData()
        onPrepareValue()
        onPrepareWidget()
    }

    open fun onPrepareValue() {
    }

    open fun onPrepareWidget() {
    }

    open fun onPrepareData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(manager: FragmentManager) {
        super.show(manager, javaClass.simpleName)
    }

}