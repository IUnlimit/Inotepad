package org.iunlimit.inotepad.fragments.preview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.iunlimit.inotepad.R

// 图片 -> vue#Image
// code -> vue#Code
// office -> vue#Office
class WebViewFragment : Fragment() {

    val args by navArgs<WebViewFragmentArgs>()

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.webview, container, false)
        val webView = view.findViewById<WebView>(R.id.webView)

        webView.settings.allowFileAccess = true // allow cros
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true // 是否启用本地存储 (localStorage
//        webView.settings.builtInZoomControls = true // 是否应使用其内置的缩放机制

        webView.webViewClient = PreviewWebViewClient(this)

        webView.loadUrl("file:///android_asset/code/dist/index.html")
        return view
    }

}
