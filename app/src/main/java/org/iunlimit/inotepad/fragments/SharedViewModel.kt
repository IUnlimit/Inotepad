package org.iunlimit.inotepad.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.iunlimit.inotepad.data.models.FileData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            (parent?.getChildAt(0) as TextView).text = TYPE_CHARACTER_MAP[position]
        }
    }

    fun checkDatabaseEmpty(fileData: List<FileData>) {
        emptyDatabase.value = fileData.isEmpty()
    }

    companion object {

        val TYPE_CHARACTER_MAP: Array<String> = arrayOf(
            "\uD835\uDC47\uD835\uDC4B\uD835\uDC47", // .txt
            "\uD835\uDC40\uD835\uDC37", // .md
            "\uD835\uDC3D\uD835\uDC46\uD835\uDC42\uD835\uDC41", // .json
        )

    }

}