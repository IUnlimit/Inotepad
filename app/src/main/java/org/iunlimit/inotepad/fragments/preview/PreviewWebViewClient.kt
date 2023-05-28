package org.iunlimit.inotepad.fragments.preview

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.iunlimit.inotepad.fragments.preview.handler.TypeHandler

class PreviewWebViewClient(
    val fragment: WebViewFragment,
    val handler: TypeHandler
) : WebViewClient() {

    /**
     * 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
     * */
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request!!.url.toString()
        Log.v("webView", "load url: $url")
        if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp")) {
            view!!.loadUrl(url)
            return true
        }
        if (url.startsWith("scheme://")) {
            // 使用浏览器打开
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            fragment.startActivity(intent)
            return true
        }
        return false
    }

    /**
     * 在vue页面加载后进行调用
     * */
    override fun onPageFinished(view: WebView?, url: String?) {
        // 在 onPageFinished 回调里调用，表示页面加载好就调用
        view!!.post {
            val eval = view::evaluateJavascript
            handler.invoke(fragment.args.fileData, eval)
        }
    }

}