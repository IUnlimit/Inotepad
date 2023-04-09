package org.iunlimit.inotepad.fragments.preview.handler

import android.webkit.ValueCallback
import org.iunlimit.inotepad.data.models.FileData

interface TypeHandler {

    /**
     * invoke js method
     * @return invoke result
     * */
    fun invoke(
        fileData: FileData,
        eval: (script: String, resultCallback: ValueCallback<String>?) -> Unit
    )

    /**
     * 预览是否需要检查网络
     * */
    fun needNetWork(): Boolean {
        return false
    }

    fun getName(): String

    /**
     * 获取本地网页资源路径
     * */
    fun getUrl(): String {
        return "file:///android_asset/${getName()}/dist/index.html"
    }

}