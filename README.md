Lib_Utils

注意事项
==========
使用时，如果用到工具类 applicationContext ，
需要使用  LibUtils.init(Context context) 在app的必要的地方初始化
# 例如：
```java

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LibUtils.init(this);
    }
}

```

如何导入
===========

```groovy
//项目 build.gradle 引入 jitpack仓库
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }

}

//使用到注解的module ，build.gradle 引入依赖

dependencies {
   //引入依赖
   implementation 'com.github.zilong-sky:Lib_Utils:2.0'

}
```