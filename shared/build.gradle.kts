plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    // compose multiplatform support
    alias(libs.plugins.composeLibrary)
    // 添加 Kotlin Serialization 插件，注意低版本无效
    // 配套 libs.kotlinx.serialization.json 库使用
    alias(libs.plugins.kotlinSerializationPlugin)
    // 本地资源2
    id("dev.icerock.mobile.multiplatform-resources")
    // 数据库配置1
    // ⚠️最后，iOS还需添加SQLite依赖库，俩种方式：
    // 1. 使用 CocoaPods 来管理依赖项
    // pod 'SQLite.swift', '~> 0.12.2'
    // 然后运行 pod install 来安装依赖
    // 2. 手动链接 SQLite:
    // 如果没有使用 CocoaPods，或者需要手动链接，确保在 Xcode 项目的构建设置中添加 SQLite 库：
    // <1. 打开 Xcode，选择你的项目。
    // <2. 转到“Build Phases”选项卡。
    // <3. 展开“Link Binary With Libraries”。
    // <4. 点击“+”按钮，添加 libsqlite3.tbd。
    // 这里我选了第二种
    id("app.cash.sqldelight") version "2.0.2"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                // compose multiplatform support
                api(compose.material3) // md 设计的组件
                api(compose.foundation) // 基础布局组件
                api(compose.ui) // 测量、布局、绘制、事件、Modifier
                api(compose.runtime) // 树管理能力
                api(compose.animation) // 动画

                // 跨平台导航库
                api(libs.precompose)
                // 跨平台网络库
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                // 跨平台日志库
                // 不好用 日志出不来 已自行实现
//                implementation(libs.logger.kermit)
                // 跨平台序列化库
                implementation(libs.kotlinx.serialization.json) // 添加 Serialization 依赖
                // ViewModel
                implementation(libs.precompose.viewmodel)
                // 图片库 目前还是Alpha版本
                implementation(libs.coil3.core)
                implementation(libs.coil3.ktor)
                implementation(libs.coil3.compose)
                // 本地资源3
                api("dev.icerock.moko:resources:0.24.1")
                api("dev.icerock.moko:resources-compose:0.24.1")
                // 日期&时间
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                // KeyValueStore
                implementation("com.liftric:kvault:1.12.0")
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        val androidMain by getting {
            dependencies {
                // 平台网络库引擎
                implementation(libs.ktor.client.okhttp)
                // 数据库配置1
                implementation("app.cash.sqldelight:android-driver:2.0.2")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                // 平台网络库引擎
                implementation(libs.ktor.client.ios)
                // 数据库配置1
                implementation("app.cash.sqldelight:native-driver:2.0.2")
            }
        }
    }
}

android {
    namespace = "com.dixon.app.kmmsample"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

multiplatformResources {
    resourcesPackage.set("com.dixon.app.kmmsample") // required
//    resourcesClassName.set("SharedRes") // optional, default MR
//    resourcesVisibility.set(MRVisibility.Internal) // optional, default Public
    iosBaseLocalizationRegion.set("en") // optional, default "en"
    iosMinimalDeploymentTarget.set("16.0") // optional, default "9.0"
}

// 数据库配置1
sqldelight {
    databases {
        create("KmmSampleDatabase") {
            packageName.set("com.dixon.app.kmmsample")
        }
    }
}