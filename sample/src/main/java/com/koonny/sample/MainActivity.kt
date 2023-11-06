package com.koonny.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.ConfirmDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = listOf("ConfirmDialog", "BottomListDialog<String>", "BottomListDialog<Any>", "BottomMenuDialog", "CustomDialog")
        val adapter = MyAdapter(data)
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
        adapter.itemClick {
            when (it) {
                0 -> ConfirmDialog.get()
                    .setMessage("确认退出当前账号？")
                    .positiveClick {
                        Toast.makeText(this@MainActivity, "Yes", Toast.LENGTH_SHORT).show()
                    }
                    .show(supportFragmentManager)
            }
        }

    }

}