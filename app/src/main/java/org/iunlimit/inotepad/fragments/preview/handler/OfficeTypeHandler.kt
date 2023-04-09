package org.iunlimit.inotepad.fragments.preview.handler

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.ValueCallback
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.sdk.OSSHolder
import org.iunlimit.inotepad.sdk.sts
import java.util.*

class OfficeTypeHandler(val context: Context): TypeHandler {

    override fun invoke(
        fileData: FileData,
        eval: (script: String, resultCallback: ValueCallback<String>?) -> Unit
    ) {
        assert(fileData.filePath != null)

        val oss = OSSHolder(context, sts)
        val uuid = UUID.randomUUID().toString()

        oss.asyncUpload(uuid + fileData.type.value, fileData.filePath!!) {
            Log.v("handler#office", "Upload to $it")
            // main thread
            Handler(Looper.getMainLooper()).post{
                eval("javascript:setData(`${it}`, '${fileData.type.value.substring(1)}')") { result ->
                    Log.v("handler#office", result)
                    // TODO delete file after preview
//                    val delete = oss.delete(uuid + fileData.type.value)
//                    Log.v("handler#office", "Delete $delete")
                }
            }
        }
    }

    override fun getName(): String {
        return "office"
    }

}