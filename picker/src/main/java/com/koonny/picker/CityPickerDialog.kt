package com.koonny.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koonny.wheelview.WheelView
import com.koonny.wheelview.contract.OnWheelChangedListener
import com.koonny.wheelview.contract.WheelFormatter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CityPickerDialog : BottomSheetDialogFragment(), WheelFormatter, OnWheelChangedListener {

    class Area(val code: String, val name: String, val children: MutableList<Area>?)

    private lateinit var wheelView0: WheelView
    private lateinit var wheelView1: WheelView
    private lateinit var wheelView2: WheelView
    private lateinit var tvTitle: TextView
    private lateinit var btnConfirm: TextView
    private lateinit var btnCancel: TextView

    private var orgData: MutableList<Area>? = null

    private var block: ((province: String, city: String, area: String) -> Unit)? = null

    fun confirm(block: (province: String, city: String, area: String) -> Unit): CityPickerDialog {
        this.block = block
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_three_wheelview, container, false)
        wheelView0 = view.findViewById(R.id.wheelView0)
        wheelView1 = view.findViewById(R.id.wheelView1)
        wheelView2 = view.findViewById(R.id.wheelView2)
        tvTitle = view.findViewById(R.id.tvTitle)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        btnCancel = view.findViewById(R.id.btnCancel)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCancel.setOnClickListener { dismiss() }

        wheelView0.setOnWheelChangedListener(this)
        wheelView1.setOnWheelChangedListener(this)
        btnConfirm.setOnClickListener { _ ->
            block?.invoke(
                wheelView0.getCurrentItem<Area>().name,
                wheelView1.getCurrentItem<Area>().name,
                wheelView2.getCurrentItem<Area>().name
            )
            dismiss()
        }

        val jsonBean = readJson()
        orgData = Gson().fromJson(jsonBean, object : TypeToken<MutableList<Area>>() {}.type)

        if (orgData.isNullOrEmpty()) return

        wheelView0.setFormatter(this)
        wheelView0.setStyle(com.koonny.wheelview.R.style.WheelDefault)
        wheelView0.setData(orgData!!, 0)

        wheelView1.setFormatter(this)
        wheelView1.setStyle(com.koonny.wheelview.R.style.WheelDefault)
        wheelView1.setData(orgData!![0].children, 0)

        wheelView2.setFormatter(this)
        wheelView2.setStyle(com.koonny.wheelview.R.style.WheelDefault)
        wheelView2.setData(orgData!![0].children!![0].children, 0)
    }

    override fun onWheelScrolled(view: WheelView?, offset: Int) {
        if (orgData.isNullOrEmpty()) return
        if (view?.id == R.id.wheelView2) return
        if (view?.id == R.id.wheelView1) {
            wheelView2.data = orgData!![wheelView0.currentPosition].children!![wheelView1.currentPosition].children
        }
        if (view?.id == R.id.wheelView0) {
            wheelView1.data = orgData!![wheelView0.currentPosition].children
            wheelView2.data = orgData!![wheelView0.currentPosition].children!![wheelView1.currentPosition].children
        }

    }

    override fun onWheelSelected(view: WheelView?, position: Int) {
    }

    override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
    }

    override fun onWheelLoopFinished(view: WheelView?) {
    }

    override fun formatItem(item: Any): String {
        return (item as Area).name
    }

    private fun readJson(): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = requireActivity().assets
            val bf = BufferedReader(InputStreamReader(assetManager.open("pca.json")))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    fun show(manager: FragmentManager) {
        super.show(manager, javaClass.simpleName)
    }

}