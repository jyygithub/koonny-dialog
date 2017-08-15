package com.jiangyy.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiangyy.easydialog.CommonDialog;
import com.jiangyy.easydialog.LoadingDialog;
import com.jiangyy.easydialog.OtherDialog;
import com.jiangyy.easydialog.SingleChoiceDialog;
import com.jiangyy.easydialog.InputDialog;
import com.jiangyy.easydialog.MultipleChoiceDialog;
import com.jiangyy.easydialog.UpdateDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void commonDialog(View view) {

        new CommonDialog.Builder(this)
                .setTitle("标题", R.color.colorAccent)
                .setMessage("这里是提示内容", R.color.colorPrimary)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, R.color.colorPrimaryDark)
                .setNegativeButton("取消", null, R.color.colorPrimaryDark).show();

    }

    public void singleDialog(View view) {

        new SingleChoiceDialog.Builder(this).setTitle("提示")
                .addList("古典风格")
                .setOnItemClickListener(new SingleChoiceDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(String title, int position) {
                        Toast.makeText(MainActivity.this, title + "," + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

    public void multipleDialog(View view) {
        new MultipleChoiceDialog.Builder(this).setTitle("提示")
                .addList(new String[]{"1", "2", "3"})
                .setMaxChoice(1)
                .addListener(new MultipleChoiceDialog.ClickListener() {
                    @Override
                    public void OnFinishClick(List<String> data, List<Integer> data0) {
                        String str = "";
                        for (int index = 0; index < data.size(); index++) {
                            str += "," + data.get(index);
                        }
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    public void inputDialog(View view) {
        new InputDialog.Builder(this)
                .setTitle("请输入")
                .setHint("这里是提示内容")
                .setLines(5)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, view.getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void loadingDialog(View view) {
        final LoadingDialog.Builder mBuilder = new LoadingDialog.Builder(this);
        mBuilder.setTitle("正在加载ing...");
        mBuilder.showProgress(true).show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                int i = 0;
                while (i < 100) {
                    try {
                        Thread.sleep(200);
                        mBuilder.setProgress(i);
                        i++;
                    } catch (Exception e) {
                    }
                }
                mBuilder.dismiss();
            }
        }).start();

    }

    public void updateDialog(View view) {
        new UpdateDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("发现新版本，1.0.2来了")
                .setMessage("【新添】1、添加功能一；\n【修复】2、修复了好几个BUG哟\n【其他】3、你猜更新了什么", R.color.colorAccent)
                .setPositiveButton("立即下载", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setNegativeButton("以后更新", null).show();

    }

    public void otherDialog(View view) {

        new OtherDialog.Builder(this)
                .setGravity(Gravity.BOTTOM)
                .setContentView(R.layout.layout_dialog)
                .setText(R.id.dialog_title, "This is title")
                .setText(R.id.dialog_message, "This is message")
                .setOnClickListener(R.id.dialog_button1, "ABC", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "btn1", Toast.LENGTH_SHORT).show();
                    }
                })
                .bundleInputListener(R.id.dialog_input, R.id.dialog_button2, new OtherDialog.InputListener() {
                    @Override
                    public void onClick(View view, String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                })
                .setWidth(0.8f).show();

    }

    public void popupDialog(View view) {

        new OtherDialog.Builder(this)
                .setContentView(R.layout.layout_popup_dialog)
                .setDismissButton(R.id.dialog_cancel)
                .setLoadImageType(new OtherDialog.ImageLoader() {
                    @Override
                    public void display(Context context, ImageView imageView, String url) {
                        Glide.with(context).load(url).into(imageView);
                    }
                })
                .setImageResource(R.id.dialog_image, "http://img.sj33.cn/uploads/allimg/201612/14153R264-52.jpg")
                .setWidth(0.7f)
                .setHeight(0.6f)
                .show();

    }

}
