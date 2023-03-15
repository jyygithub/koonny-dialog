package com.koonny.sample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.koonny.dialog.BottomListDialog
import com.koonny.dialog.BottomMenuDialog
import com.koonny.dialog.ConfirmDialog
import com.koonny.wheelview.widget.WheelView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = listOf("ConfirmDialog", "BottomListDialog<String>", "BottomListDialog<Any>", "BottomMenuDialog", "CustomDialog")
        val adapter = MyAdapter(data)
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
        adapter.itemClick {
            when (it) {
                0 -> ConfirmDialog()
                    .title("提示")
                    .message("确认退出当前账号？") {
                        setTextColor(Color.RED)
                    }
                    .positive("YES") {
                        setTypeface(null, Typeface.BOLD)
                    }
                    .negative("Go")
                    .positiveClick {
                        Toast.makeText(this@MainActivity, "Yes", Toast.LENGTH_SHORT).show()
                    }
                    .show(supportFragmentManager)
                1 -> BottomListDialog<String>()
                    .title("提示")
                    .negative("Go") {
                        setTextColor(Color.RED)
                        setTypeface(null, Typeface.BOLD)
                    }
                    .items("Item1", "Item2", "Item3") { _, _ ->

                    }
                    .show(supportFragmentManager)
                2 -> BottomListDialog<User>()
                    .title("提示")
                    .negative("Go")
                    .bind { user -> user.name }
                    .items(User("张三"), User("李四")) { _, _ ->

                    }
                    .show(supportFragmentManager)
                3 -> BottomMenuDialog()
                    .title("提示")
                    .negative("Go")
                    .items("Item1" to R.mipmap.ic_launcher, "Item2" to R.mipmap.ic_launcher, "Item3" to R.mipmap.ic_launcher) {

                    }
                    .show(supportFragmentManager)
                4 -> CustomDialog()
                    .show(supportFragmentManager)
            }
        }

        findViewById<WheelView>(R.id.wheelview).apply {
            setStyle(com.koonny.wheelview.R.style.Widget_Koonny_WheelView)
            setData(mutableListOf("1", "2", "3"))
        }

    }

}