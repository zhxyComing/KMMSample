plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
}

buildscript {
    dependencies {
        // https://github.com/icerockdev/moko-resources
        // 本地资源配置1
        classpath("dev.icerock.moko:resources-generator:0.24.1")
    }
}

