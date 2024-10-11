import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        // 在应用启动时执行初始化操作
        DBManager.init().defaultDriver()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
