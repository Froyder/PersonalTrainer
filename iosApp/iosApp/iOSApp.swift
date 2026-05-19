import SwiftUI

@main
struct iOSApp: App {
    init() {
        if let firebaseClass = NSClassFromString("FIRApp") as? NSObject.Type {
            firebaseClass.perform(NSSelectorFromString("configure"))
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}