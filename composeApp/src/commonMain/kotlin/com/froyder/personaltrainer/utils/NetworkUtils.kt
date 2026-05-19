package com.froyder.personaltrainer.utils

expect class NetworkMonitor() {
    fun isConnected(): Boolean
}