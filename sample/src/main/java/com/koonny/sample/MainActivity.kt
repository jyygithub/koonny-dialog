package com.koonny.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.Confirm2Dialog
import com.koonny.dialog.ConfirmDialog
import com.koonny.dialog.LoadingDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = listOf("ConfirmDialog", "Confirm2Dialog", "LoadingDialog")
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

                1 -> Confirm2Dialog.get()
                    .setTitle("发现新版本")
                    .setMessage("是否下载新版本？")
                    .setPositive("下载并安装")
                    .setNegative("忽略")
                    .positiveClick {
                        Toast.makeText(this@MainActivity, "Yes", Toast.LENGTH_SHORT).show()
                    }
                    .show(supportFragmentManager)

                2 -> LoadingDialog.get().setMessage("加载...").show(supportFragmentManager)
            }
        }

    }

}