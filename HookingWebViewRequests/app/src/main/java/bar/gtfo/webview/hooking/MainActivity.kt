package bar.gtfo.webview.hooking
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.net.Uri
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)
        webView.webViewClient = MyWebViewClient(this)


        webView.webChromeClient = object: WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.message()?.let { Log.d("WebView", it) }
                return super.onConsoleMessage(consoleMessage)
            }
        }


        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/www/index.html")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}