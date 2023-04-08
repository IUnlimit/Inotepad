package org.iunlimit.inotepad.fragments.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.wdeo3601.pdfview.PDFView
import org.iunlimit.inotepad.R

class PDFFragment: Fragment() {

    val args by navArgs<PDFFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        val pdfView = view.findViewById<PDFView>(R.id.pdfView)
        // 设置当前显示页的前后缓存个数
        pdfView.setOffscreenPageLimit(2)
        // 是否支持缩放
        pdfView.isCanZoom(true)
        // 设置最大缩放倍数,最大支持20倍
        pdfView.setMaxScale(10f)
        pdfView.showPdfFromPath(args.fileData.filePath!!)

        return view
    }

}