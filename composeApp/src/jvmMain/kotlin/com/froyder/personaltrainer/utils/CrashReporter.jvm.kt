package com.froyder.personaltrainer.utils

actual object CrashReporter {
    actual fun log(message: String) = println("LOG: $message")
    actual fun recordException(throwable: Throwable) = println("ERROR: ${throwable.message}")
    actual fun setUserId(userId: String) = println("USER: $userId")
}