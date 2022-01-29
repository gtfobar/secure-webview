package bar.gtfo.webview.hooking

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader

class MyWebViewClient(
    private val activity: MainActivity,
) : WebViewClient() {

    private val assetLoader = WebViewAssetLoader.Builder()
        .addPathHandler("/assets/",
            WebViewAssetLoader.AssetsPathHandler(activity)
        )
        .build()

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest
    ): WebResourceResponse? {
        Log.i("shouldInterceptRequest", request.url.toString())
        return assetLoader.shouldInterceptRequest(request.url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        Log.i("shouldOverrideUrlLoadin", request.url.toString())
        return super.shouldOverrideUrlLoading(view, request)
    }
}