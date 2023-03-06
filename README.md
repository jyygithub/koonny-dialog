<h2 align="center">
  对DialogFragment的封装使用
</h2>
<p align="center">
  <a href="https://github.com/jyygithub/koonny-dialog" target="_blank">
    <img src="https://img.shields.io/badge/min--sdk-21+-%23A97BFF" alt="minSdk">
    <img alt="Maven Central" src="https://img.shields.io/maven-central/v/jyygithub/koonny-dialog">
    <img alt="GitHub" src="https://img.shields.io/github/license/jyygithub/koonny-dialog">
    <img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/jyygithub/koonny-dialog">
    <img alt="GitHub top language" src="https://img.shields.io/github/languages/top/jyygithub/koonny-dialog">
  </a>
</p>

## Gradle

```groovy
implementation 'com.koonny.dialog:dialog:LATEST_VERSION' 
```

## 使用文档

### 内置Dialog

- ConfirmDialog

```kotlin
ConfirmDialog()
    .title("提示")
    .message("确认退出当前账号？")
    .positive("Go") {
        dismiss()
    }
    .negative("YES")
    .show(supportFragmentManager)
```

- BottomListDialog

```kotlin
BottomListDialog<String>()
    .title("提示")
    .negative("Go") {
        setTextColor(Color.RED)
        setTypeface(null, Typeface.BOLD)
    }
    .items("Item1", "Item2", "Item3") { _, _ ->

    }
    .show(supportFragmentManager)
```

```kotlin
BottomListDialog<User>()
    .title("提示")
    .negative("Go")
    .bind { user -> user.name }
    .items(User("张三"), User("李四")) { _, _ ->

    }
    .show(supportFragmentManager)
```

- BottomMenuDialog

```kotlin
BottomMenuDialog()
    .title("提示")
    .negative("Go")
    .items("Item1" to R.mipmap.ic_launcher, "Item2" to R.mipmap.ic_launcher, "Item3" to R.mipmap.ic_launcher) {

    }
    .show(supportFragmentManager)
```

### 自定义Dialog

- 自定义

```kotlin
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
```

- 直接使用

```kotlin
CustomDialog().show(supportFragmentManager)
```

## Licenses

```
Copyright 2023 jyygithub

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```