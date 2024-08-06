package com.dixon.app.kmmsample

import androidx.compose.ui.window.ComposeUIViewController
import com.dixon.app.kmmsample.ui.App

/**
 * 将 Compose Multiplatform 的 UI 组件包装成 UIViewController
 * 确保这些组件能够在 iOS 的 UIKit 和 SwiftUI 中使用。
 * 通过这种方式，可以实现跨平台的一致性，并充分利用 iOS 平台的视图管理机制。
 */
fun AppViewController() = ComposeUIViewController { App() }