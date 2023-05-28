package org.iunlimit.inotepad.fragments.preview.driver

import android.content.Context
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.fileChooser
import com.kaopiz.kprogresshud.KProgressHUD
import com.rratchet.android.compress.CompressHelper
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import java.io.File

class Compress(val fileData: FileData) {

    fun selectFile(context: Context, callback: (file: File) -> Unit) {
        fileData.filePath?.let {
            val dir = File(context.cacheDir, fileData.name)
            if (dir.exists()) dir.deleteRecursively()

            val loading = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.compress_label))
                .setDetailsLabel(context.getString(R.string.compress_detail_label))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
            CompressHelper.extract(it, dir.absolutePath)
            loading.dismiss()

            MaterialDialog(context).show {
                cornerRadius(16f)
                fileChooser(context, initialDirectory = dir) { _, file ->
                    // File selected
                    Log.v("compress", "Select ${file.name} (${file.totalSpace}) ${file.absolutePath}")
                    callback.invoke(file)
                }
            }
        }
    }



}