package org.iunlimit.inotepad.fragments.gpt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.FileDatabase

class RequestViewModel(app: Application): AndroidViewModel(app) {

    val fileDao = FileDatabase.getDatabase(app).fileDao()

    val resp: MutableLiveData<String> = MutableLiveData("[EMPTY]")
    val papiSelectFile: MutableLiveData<String> = MutableLiveData(app.getString(R.string.select_file))
    val papiSelectPrompt: MutableLiveData<String> = MutableLiveData(app.getString(R.string.select_prompt))

}