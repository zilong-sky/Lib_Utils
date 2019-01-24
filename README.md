Lib_Utils

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