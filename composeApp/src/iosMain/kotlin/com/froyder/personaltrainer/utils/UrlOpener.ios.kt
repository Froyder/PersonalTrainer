package com.froyder.personaltrainer.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(url: String) {
    val youtubeAppUrl = url.replace(
        "https://www.youtube.com/results?search_query=",
        "youtube://www.youtube.com/results?search_query="
    )

    val youtubeNsUrl = NSURL.URLWithString(youtubeAppUrl)
    val safariNsUrl = NSURL.URLWithString(url)
    val app = UIApplication.sharedApplication

    if (youtubeNsUrl != null && app.canOpenURL(youtubeNsUrl)) {
        app.openURL(youtubeNsUrl, options = emptyMap<Any?, Any?>(), completionHandler = null)
    } else if (safariNsUrl != null) {
        app.openURL(safariNsUrl, options = emptyMap<Any?, Any?>(), completionHandler = null)
    }
}