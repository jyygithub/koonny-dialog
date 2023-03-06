package com.koonny.dialog.base

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment(@LayoutRes private val layoutRes: Int) : DialogFragment() {

    private val dialogConfig = DialogConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, config().style)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutRes, container, false)
        preView(rootView)
        return rootView
    }

    abstract fun preView(rootView: View)

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(config().dismissOnTouchOutside)
        dialog?.setCancelable(config().dismissOnBackPressed)
        dialog?.window?.apply {
            setGravity(config().gravity)
            val customWidth: Int = when {
                config().width == 0f -> ViewGroup.LayoutParams.WRAP_CONTENT
                config().width == 1f -> ViewGroup.LayoutParams.MATCH_PARENT
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> (windowManager.currentWindowMetrics.bounds.width() * config().width).toInt()
                else -> {
                    val dm = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(dm)
                    (dm.widthPixels * config().width).toInt()
                }
            }
            val customHeight: Int = when {
                config().height == 0f -> ViewGroup.LayoutParams.WRAP_CONTENT
                config().height == 1f -> ViewGroup.LayoutParams.MATCH_PARENT
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> (windowManager.currentWindowMetrics.bounds.height() * config().height).toInt()
                else -> {
                    val dm = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(dm)
                    (dm.heightPixels * config().height).toInt()
                }
            }

            setLayout(customWidth, customHeight)
        }
    }

    open fun config(): DialogConfig = dialogConfig

    fun show(manager: FragmentManager) {
        show(manager, javaClass.simpleName)
    }

}