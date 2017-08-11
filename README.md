# Dialog
[![Download](https://api.bintray.com/packages/jiangyychn/maven/dialog/images/download.svg)](https://bintray.com/jiangyychn/maven/dialog) ![API](https://img.shields.io/badge/api-14%2B-brightgreen.svg)

[中文版](https://github.com/jyygithub/dialog/blob/master/README.zh.md)

<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280777.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280780.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280784.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;

<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280788.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280793.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280796.png" width = "280" height = "497" alt="图片名称" align=center />

# Download

```
dependencies {
	// ... other dependencies here
    compile 'com.jiangyy:dialog:1.0.2'
    // implementation 'com.jiangyy:dialog:1.0.2'
}
```

# Common Dialog
Here's a basic example: 

```
new CommonDialog.Builder(this)
		.setTitle("标题")
		.setMessage("这里是提示内容")
		.setPositiveButton("确定", new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
		}).setNegativeButton("取消", null).show();
```
You can setting the text color of title/message/positive button/negative button. Like this:

```
new CommonDialog.Builder(this)
		.setTitle("标题", R.color.colorAccent)
        .setMessage("这里是提示内容", R.color.colorPrimary)
        .setPositiveButton("确定", new View.OnClickListener() {
		        @Override
                public void onClick(View view) {

                }
        }, R.color.colorPrimaryDark)
        .setNegativeButton("取消", null, R.color.colorPrimaryDark).show();
```

# Single Choice Dialog
SingleChoiceDialog is a menu dialog which is showing from bottom, and you can choose an item only. And you also can set text color like common dialog.

```
new SingleChoiceDialog.Builder(this).setTitle("提示")
                .addList(new String[]{"1", "2", "3"})
                .setOnItemClickListener(new SingleChoiceDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(String title, int position) {
                        Toast.makeText(MainActivity.this, title + "," + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
```
This dialog should bundle datas, which can be an array、a list or a string. Then if you choose an item, you can use "setOnItemClickListener" method to get value which include item's value and position. 

```
new SingleChoiceDialog.Builder(this).setTitle("提示")
                .addList(new ArrayList<String>())
                .setOnItemClickListener(new SingleChoiceDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(String title, int position) {
                        Toast.makeText(MainActivity.this, title + "," + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
```
```
new SingleChoiceDialog.Builder(this).setTitle("提示")
                .addList("古典风格")
                .setOnItemClickListener(new SingleChoiceDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(String title, int position) {
                        Toast.makeText(MainActivity.this, title + "," + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
```

# Multiple Choice Dialog
MultipleChoiceDialog's style is like SingleChoiceDialog, but it can choose multiple items. You can get an array after you choose.

```
new MultipleChoiceDialog.Builder(this)
				.setTitle("提示")
                .addList(new String[]{"1", "2", "3"})
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
```

# Input Dialog
InputDialog is a dialog which is can input, and you can get the result from view.getTag().toString()

```
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
```

# Loading Dialog

This is a basic usage:

```
new LoadingDialog.Builder(this).setTitle("正在加载...").show();
```
If you want to show progress in this dialog, you can use "showProgress()" like this:

```
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
```

# Update Dialog
We know most APP check from update, and show dialog when id have a new version. UpdateDialog is a beautiful dialog to show it, and you can show icon、title、message and two button in it. The usage is easy:

```
new UpdateDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("发现新版本，1.0.1来了")
                .setMessage("【新添】1、添加功能一；\n【修复】2、修复了好几个BUG哟\n【其他】3、你猜更新了什么", R.color.colorAccent)
                .setPositiveButton("立即下载", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setNegativeButton("以后更新", null).show();
```
# Other Dialog
if you want to custom dialog using your layout, OtherDialog is a good choice. This is a example:

```
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
```
In OtherDialog, you should create a xml first, and then use setContentView to bundle with dialog, and now you can use all views in your custom layout.
| Method                      | Usage                                |Parameter Example
|:-------------               |:-------------                        |:-------------
| setContentView              | bundle the layout to dialog          |setContentView(R.layout.layout_dialog)
| setText                     | set text                             |setText(R.id.dialog_title, "This is title")
| setOnClickListener          | set click event                      |setOnClickListener(R.id.dialog_button1, "ABC", listener
| setAdapter                  | set adapter to AdapterView           |setAdapter(R.id.listview,adapter)
| setOnItemClickListener      | set item click event to AdapterView  |setOnItemClickListener(R.id.listview,listener)
| bundleInputListener         | get input after view click           |bundleInputListener(R.id.dialog_input, R.id.dialog_button2, listener)
| setWidth                    | set dialog's width（Relative to the width of the screen）                   |setWidth(0.8f) ：80% of screen width
| setHeight                   |set dialog's height（Relative to the height of the screen）      |setHheight(0.8f) ：80% of screen height
| setGravity                  | Relative to the gravity of the screen  |setGravity(Gravity.BOTTOM)
| setCanceledOnTouchOutside   |  |
| setCancelable               |  |