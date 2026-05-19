package com.froyder.personaltrainer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.froyder.personaltrainer.data.local.appContext

actual class NetworkMonitor actual constructor() {
    actual fun isConnected(): Boolean {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}