package bar.gtfo.webview.backslashattack

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)
        webView.webViewClient = WebViewClient()

        /*
        webView.webChromeClient = object: WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.message()?.let { Log.d("WebView", it) }
                return super.onConsoleMessage(consoleMessage)
            }
        }
        */

        val url = "https://attacker.com\\\\@legitimate.com"
        Uri.parse(url).host?.let { Log.d("evil", it) }
        if (authorityIsValidUrl(url)) {
            Log.i("isUrlValid", "url is valid")
            webView.loadUrl(url)
        } else {
            Log.i("isUrlValid", "url is not valid")
        }
    }

    private fun hostIsValidUrl(url: String): Boolean {
        val uri = Uri.parse(url)
        return "https" == uri.scheme && "legitimate.com" == uri.host
    }

    private fun authorityIsValidUrl(url: String): Boolean {
        val uri = Uri.parse(url)
        return "https" == uri.scheme && "legitimate.com" == uri.authority
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}