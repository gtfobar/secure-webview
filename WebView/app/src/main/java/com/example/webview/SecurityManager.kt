package com.example.webview

import android.net.Uri

class SecurityManager {
    var baseWhitelist = hashMapOf<String, List<String>>()

    fun urlMatchesWhitelist(url: Uri, whitelist: HashMap<String, List<String>>) : Boolean {
        whitelist[url.scheme]?.also { hostWhitelist ->
            if (url.host in hostWhitelist) {
                return true
            }
        }
        return false
    }

    fun isUrlValid(url: Uri, whitelist: HashMap<String, List<String>>) : Boolean {
        return urlMatchesWhitelist(url, whitelist) || urlMatchesWhitelist(url, baseWhitelist)
    }
}