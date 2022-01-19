package com.example.webview

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.webkit.WebViewAssetLoader
import java.io.ByteArrayInputStream


class MyWebViewClient(private val activity: MainActivity,
) : WebViewClient() {

    private val specialResponse = WebResourceResponse(
        "text/html",
        "utf-8",
        ByteArrayInputStream("Not allowed".toByteArray())
    )

    private val responseSafeBrowsingNotInitialized: WebResourceResponse = WebResourceResponse(
        "text/html",
        "utf-8",
        ByteArrayInputStream("Safe Browsing not initialized yet".toByteArray())
    )
    private val sm = activity.sm
    private val pageWhitelist = hashMapOf(
        "file" to listOf(""),
        "https" to listOf("gtfo.bar", "appassets.gtfo.bar")
    )
    private val contentWhitelist = pageWhitelist
    private val assetLoader = WebViewAssetLoader.Builder()
        .setDomain(activity.getString(R.string.assets_dummy_domain))
        .addPathHandler("/assets/",
            WebViewAssetLoader.AssetsPathHandler(activity)
        )
        .addPathHandler("/arbitrary/",
            WebViewAssetLoader.AssetsPathHandler(activity)
        )
        .build()

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest
    ): WebResourceResponse? {
        if (sm.isUrlValid(request.url, contentWhitelist)) {
            Log.d("ZALUPA shouldInterceptRequest", request.url.toString() + " - good URL")
            // return assetLoader.shouldInterceptRequest(request.url)
        } else {
            Log.d("ZALUPA shouldInterceptRequest", request.url.toString() + " - bad URL")
        }
        return assetLoader.shouldInterceptRequest(request.url)
        // return specialResponse
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        if (!activity.safeBrowsingInitialized)
            return true
        if (sm.isUrlValid(request.url, contentWhitelist)) {
            val url = Uri.parse(activity.webView.url)
            activity.jsInterface.url = url
            Log.i("ZALUPA shouldOverrideUrlLoading", request.url.toString() + " - good URL")
            // return super.shouldOverrideUrlLoading(view, request)
        } else {
            Log.i("ZALUPA shouldOverrideUrlLoading", request.url.toString() + " - bad URL")
        }
        return super.shouldOverrideUrlLoading(view, request)
        // return true
    }
}