# MVVMKit
使用Kotlin搭建Android MVVM快速开发框架。

## 引入
### 将JitPack存储库添加到您的项目中(项目根目录下build.gradle文件)
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### 添加依赖(项目目录下build.gradle文件)
[![](https://jitpack.io/v/shenbengit/MVVMKit.svg)](https://jitpack.io/#shenbengit/MVVMKit)
```gradle
plugins {
    ...
    id 'kotlin-kapt'
}

android{
    ...
    buildFeatures {
        //启用databinding
        dataBinding = true
    }
}

dependencies {
    implementation 'com.github.shenbengit:MVVMKit:Tag'
}
```

## 演示用例
详见[SrsRtcAndroidClient](https://github.com/shenbengit/SrsRtcAndroidClient)

## 项目中集成的三方框架


## 快速使用



