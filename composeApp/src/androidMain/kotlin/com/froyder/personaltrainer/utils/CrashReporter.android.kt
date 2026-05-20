package com.froyder.personaltrainer.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

actual object CrashReporter {
    actual fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    actual fun recordException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    actual fun setUserId(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }
}