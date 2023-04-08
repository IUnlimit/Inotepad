package org.iunlimit.inotepad.fragments.preview.handler

import android.webkit.ValueCallback
import org.iunlimit.inotepad.data.models.FileData

object BlobTypeHandler: TypeHandler {

    override fun invoke(
        fileData: FileData,
        eval: (script: String, resultCallback: ValueCallback<String>?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "blob"
    }

}