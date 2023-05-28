package org.iunlimit.inotepad.fragments.preview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.CODE_CLAZZ
import org.iunlimit.inotepad.data.models.IMAGE_CLAZZ
import org.iunlimit.inotepad.data.models.MD_CLAZZ
import org.iunlimit.inotepad.data.models.OFFICE_CLAZZ
import org.iunlimit.inotepad.data.models.TXT_CLAZZ
import org.iunlimit.inotepad.fragments.preview.handler.BlobTypeHandler
import org.iunlimit.inotepad.fragments.preview.handler.CodeTypeHandler
import org.iunlimit.inotepad.fragments.preview.handler.ImageTypeHandler
import org.iunlimit.inotepad.fragments.preview.handler.MDTypeHandler
import org.iunlimit.inotepad.fragments.preview.handler.OfficeTypeHandler
import org.iunlimit.inotepad.fragments.preview.handler.TxtTypeHandler
import org.iunlimit.inotepad.util.checkNetworkAvailable

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

        val handler = when (args.fileData.type.clazz) {
            CODE_CLAZZ -> CodeTypeHandler
            TXT_CLAZZ -> TxtTypeHandler
            MD_CLAZZ -> MDTypeHandler
            OFFICE_CLAZZ -> OfficeTypeHandler(requireContext())
            IMAGE_CLAZZ -> ImageTypeHandler
            else -> BlobTypeHandler
        }

        if (handler.needNetWork()) {
            val online = checkNetworkAvailable(requireActivity())
            if (!online) {
                MaterialDialog(requireContext()).show {
                    cornerRadius(16f)
                    title(R.string.no_network)
                    positiveButton(R.string.ok) {
                        view.findNavController().popBackStack()
                    }
                }
            }
        }

        webView.settings.allowFileAccess = true // allow cros
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true // 是否启用本地存储 (localStorage
//        // 自适应屏幕(无效2
//        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true // 是否应使用其内置的缩放机制
        webView.webViewClient = PreviewWebViewClient(this, handler)
        webView.loadUrl(handler.getUrl())

        return view
    }

}
