import SwiftUI
import shared

// 创建一个 Swift 结构体来包装 UIViewController
struct ComposeUIViewControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        ComposeViewControllerKt.AppViewController()  // 调用 Kotlin 中创建的 Compose UIViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
	let greet = Greeting().greet()

	var body: some View {
// 		Text(greet)
        ComposeUIViewControllerRepresentable()
                 .edgesIgnoringSafeArea(.all) // 如果只想忽略水平方向的 Safe Area
//                 .edgesIgnoringSafeArea(.horizontal) // 如果只想忽略水平方向的 Safe Area
//                 .edgesIgnoringSafeArea([]) // 默认不忽略 Safe Area
        // .all 状态栏顶部高度、横向 safe area 都会忽略
	}
}


struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
