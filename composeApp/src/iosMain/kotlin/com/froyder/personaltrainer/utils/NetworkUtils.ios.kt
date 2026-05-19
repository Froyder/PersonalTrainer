package com.froyder.personaltrainer.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.darwin.UInt32Var
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value

actual class NetworkMonitor actual constructor() {
    @OptIn(ExperimentalForeignApi::class)
    actual fun isConnected(): Boolean {
        val reachability = SCNetworkReachabilityCreateWithName(null, "google.com") ?: return false
        return memScoped {
            val flags = alloc<UInt32Var>()
            SCNetworkReachabilityGetFlags(reachability, flags.ptr)
            flags.value and kSCNetworkReachabilityFlagsReachable != 0u
        }
    }
}