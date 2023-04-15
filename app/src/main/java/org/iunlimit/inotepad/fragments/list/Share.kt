package org.iunlimit.inotepad.fragments.list

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import org.iunlimit.inotepad.BuildConfig
import org.iunlimit.inotepad.R
import java.io.File

fun shareFile(context: Context, filePath: String) {
    val file = File(filePath)
    if (!file.exists()) {
        MaterialDialog(context).show {
            cornerRadius(16f)
            title(R.string.file_not_found)
            positiveButton(R.string.ok)
        }
    }

    val share = Intent(Intent.ACTION_SEND)
    val contentUri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".fileprovider",
        file
    )
    share.putExtra(Intent.EXTRA_STREAM, contentUri)
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    share.type = "*/*"
    share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    context.startActivity(Intent.createChooser(share, "分享文件"))
}