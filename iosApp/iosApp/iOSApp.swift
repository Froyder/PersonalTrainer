import SwiftUI
import FirebaseCore
import FirebaseCrashlytics

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        
        Crashlytics.crashlytics().log("iOS Crashlytics test")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
