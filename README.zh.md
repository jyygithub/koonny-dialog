# Dialog
[![Download](https://api.bintray.com/packages/jiangyychn/maven/dialog/images/download.svg)](https://bintray.com/jiangyychn/maven/dialog) ![API](https://img.shields.io/badge/api-14%2B-brightgreen.svg)

[English Document](https://github.com/jyygithub/dialog/blob/master/README.md)

<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280777.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280780.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280784.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;

<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280788.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280793.png" width = "280" height = "497" alt="图片名称" align=center />&ensp;<img src="https://github.com/jyygithub/dialog/blob/master/image/screenshot_1502280796.png" width = "280" height = "497" alt="图片名称" align=center />

# 添加依赖

```
dependencies {
	// ... other dependencies here
    compile 'com.jiangyy:dialog:1.0.3'
    // implementation 'com.jiangyy:dialog:1.0.3'
}
```

# 通用Dailog
下面是基础用法：

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
你可以设置标题、内容、确定按钮和取消按钮的文字颜色。例如：

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

# 单选对话框
单选对话框是从底部出现的一个列表对话框，你仅可以选择一项。当然，你依旧可以设置文字颜色。

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
单选对话框需要设置数据源，格式可以使数组、List或者就是一个字符串。你可以通过setOnItemClickListener方法获取到你点击的值，返回值包括值和下标。

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

# 多选对话框
多选对话框的显示风格和单选对话框相似，不过它可以选择多个，你选择后会返回一个List。

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

# 输入对话框
输入对话框可以输入，获取输入的结果则是通过view.getTag().toString()获得。

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

# 加载对话框

这是基础用法：

```
new LoadingDialog.Builder(this).setTitle("正在加载...").show();
```
如果你还想显示进度也可以，你可以使用showProgress()，例如：

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

# 检查更新对话框
我们知道，现在绝大多数APP都有检查更新的功能，当检查到有新版本时，就回显示一个对话框用于显示新版本的信息。检查更新对话框就是一个好看的定制对话框，可以显示图标、标题、内容和两个按钮。使用也很简单：
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

# 自定义对话框
如果上面的对话框不能满足您的需求，你可以使用自定义对话框。下面是个例子：

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

| 方法| 用法|传参例子
|:-------------               |:-------------                        |:-------------|
| setContentView              | 给对话框绑定布局文件          |setContentView(R.layout.layout_dialog)|
| setText                     | 设置文本                           |setText(R.id.dialog_title, "This is title")|
| setOnClickListener          | 设置点击事件                      |setOnClickListener(R.id.dialog_button1, "ABC", listener|
| setAdapter                  | 设置适配器           |setAdapter(R.id.listview,adapter)|
| setOnItemClickListener      | 设置Item点击  |setOnItemClickListener(R.id.listview,listener)|
| bundleInputListener         | 当View点击后获取某个输入框的值           |bundleInputListener(R.id.dialog_input, R.id.dialog_button2, listener)|
| setWidth                    | 设置对话框的宽度（屏幕宽度的百分之几）                |setWidth(0.8f) ：80% of screen width|
| setHeight                   |设置对话框高度（屏幕高度的百分之几）      |setHheight(0.8f) ：80% of screen height|
| setGravity                  | 设置对话框的相对位置  |setGravity(Gravity.BOTTOM)|
| setCanceledOnTouchOutside   |  |  |
| setCancelable               |  |  |