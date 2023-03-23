package org.iunlimit.inotepad.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.iunlimit.inotepad.data.models.FileData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkDatabaseEmpty(fileData: List<FileData>) {
        emptyDatabase.value = fileData.isEmpty()
    }

}