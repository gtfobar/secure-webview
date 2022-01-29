package com.example.webview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import android.content.Intent
import android.content.Intent.getIntent

import android.app.Activity
import android.webkit.WebSettings
import android.webkit.WebSettings.*

class MainActivity : AppCompatActivity() {

    var safeBrowsingInitialized = false

    lateinit var webView: WebView
    lateinit var jsInterface : JavascriptInterfaceClass
    lateinit var sm: SecurityManager

    private lateinit var loadUrlButton: Button
    private lateinit var urlEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.edit_url)

        loadUrlButton = findViewById(R.id.load_url_button)
        webView = findViewById(R.id.webView)

        val baseWhitelist = hashMapOf(
            "file" to listOf(""),
            "http" to listOf("gtfo.bar"),
            "content" to listOf("sms")
        )
        sm = SecurityManager()
        sm.baseWhitelist = baseWhitelist

        webView.webViewClient = MyWebViewClient(this)
        webView.webChromeClient = object: WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.message()?.let { Log.d("WebView", it) }
                return super.onConsoleMessage(consoleMessage)
            }
        }

        webView.settings.javaScriptEnabled = true

        webView.settings.allowFileAccess = true

        webView.settings.allowFileAccessFromFileURLs = true

        webView.settings.allowUniversalAccessFromFileURLs = false

        webView.settings.allowContentAccess = true

        webView.settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW;

        // SEEMS LIKE GOOGLE SAFE BROWSING NOT WORKING AS EXPECTED. STEPS TO REPRODUCE:
        // 1. Take any link from here https://testsafebrowsing.appspot.com/
        // 2. Try to load it in webView
        // 3. Link is loaded without warning

        jsInterface = JavascriptInterfaceClass(this)
        webView.addJavascriptInterface(jsInterface, "Android")

        // For optimal protection against known threats, wait until you've initialized Safe
        // Browsing before you invoke a WebView object's loadUrl() method.
        if (WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            WebViewCompat.startSafeBrowsing(this) { success ->
                safeBrowsingInitialized = true
                if (!success) {
                    Log.e("WebView", "Unable to initialize Safe Browsing!")
                }
                Log.i("WebView", "Safe Browsing Initialized")
            }
        }

        // webView.loadUrl("file:///etc/hosts")
        // webView.loadUrl("file:///data/local/tmp/outer.html")
        // webView.loadUrl("content://sms/inbox")
        // webView.loadUrl("file:///data/data/com.example.webview/shared_prefs/WebViewChromiumPrefs.xml")
        webView.loadUrl("https://" + getString(R.string.assets_dummy_domain) +"/assets/www/index.html")

        /*
        // TODO: check how it works
        val url = "https://attacker.com\\\\@legitimate.com"
        Uri.parse(url).host?.let { Log.d("evil", it) }
        webView.loadUrl(url)

        // TODO: check how it works
        // vulnerable code
        val uri = intent.data
        val isValidUrl = "https" == uri?.scheme && "legitimate.com" == uri.host
        if (isValidUrl) {
            webView.loadUrl(uri.toString());
        }
        // mitigation
        // val uri = Uri.parse(intent.data.toString())

        // TODO: check how it works
        webView.loadDataWithBaseURL("https://google.com/",
            "<script>document.write(document.domain)</script>",
            null, null, null);

    */

        loadUrlButton.setOnClickListener {
            webView.loadUrl(urlEditText.text.toString())
        }
    }

    private fun isValidUrl(url: String): Boolean {
        val uri = Uri.parse(url)
        return "https" == uri.scheme && "legitimate.com" == uri.host
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
