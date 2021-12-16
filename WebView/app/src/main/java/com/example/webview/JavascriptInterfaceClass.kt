package com.example.webview

import android.content.Context
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebResourceResponse
import android.widget.Toast
import java.net.URL

class JavascriptInterfaceClass(val activity: MainActivity) {

    lateinit var url : Uri
    val sm = activity.sm

    private val jsInterfaceWhitelist = hashMapOf(
        "https" to listOf("gtfo.bar", "appassets.gtfo.bar")
    )

    @JavascriptInterface
    fun returnString(): String {
        if (sm.isUrlValid(url, jsInterfaceWhitelist))
            return "Secret string"
        return ""
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String?) {
        if (sm.isUrlValid(url, jsInterfaceWhitelist))
            Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }
}