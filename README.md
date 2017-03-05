#####intentutils
Android use Intent start system App's Utils.

本项目用于Intent打开系统应用的工具类.

 * Android Studio，可以选择添加:

```
   allprojects {
     
         repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

   dependencies {
	        compile 'com.github.playAndroid:intentutils:v1.0'
	}
```

```
为防止内存泄漏,工具类中不保存Activity引用,所以直接使用 IntentUtils utils = new IntentUtils(activity) 即可


```

