package org.iunlimit.inotepad.fragments.preview.handler

import android.util.Log
import android.webkit.ValueCallback
import org.iunlimit.inotepad.data.models.FileData
import java.io.File
import java.nio.charset.Charset
import java.util.Base64

object BlobTypeHandler: TypeHandler {

    override fun invoke(
        fileData: FileData,
        eval: (script: String, resultCallback: ValueCallback<String>?) -> Unit
    ) {
        val base64 = Base64.getEncoder().encode(File(fileData.filePath!!).readBytes())
            .toString(Charset.forName("UTF8"))
        eval("javascript:setData(`${base64}`)") {
            Log.v("handler#blob", it)
        }
    }

    override fun getName(): String {
        return "blob"
    }

}