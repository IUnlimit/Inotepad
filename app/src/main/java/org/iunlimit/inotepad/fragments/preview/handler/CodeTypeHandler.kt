package org.iunlimit.inotepad.fragments.preview.handler

import android.util.Log
import android.webkit.ValueCallback
import org.iunlimit.inotepad.data.models.FileData

object CodeTypeHandler: TypeHandler {

    override fun invoke(
        fileData: FileData,
        eval: (script: String, resultCallback: ValueCallback<String>?) -> Unit
    ) {
        eval("javascript:setData(`${fileData.content.replace("`", "\\`")}`, '${fileData.type.value.substring(1)}')") {
            Log.v("handler#code", it)
        }
    }

    override fun getName(): String {
        return "code"
    }

}