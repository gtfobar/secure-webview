package com.example.webviewsslpinningviansc

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)
        webView.webViewClient = WebViewClient()

        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://github.com")
    }
}