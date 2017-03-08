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
	       compile 'com.github.playAndroid:intentutils:v1.1.1'
	}
```

```
使用如下:
IntentUtils.callPhone(activity,"13845612345");


```

