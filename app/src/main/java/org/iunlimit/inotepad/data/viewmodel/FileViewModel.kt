package org.iunlimit.inotepad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iunlimit.inotepad.data.FileDatabase
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.repository.FileRepository

class FileViewModel(application: Application): AndroidViewModel(application) {

    private val fileDao = FileDatabase.getDatabase(application).fileDao()
    private val repository: FileRepository = FileRepository(fileDao)
    val allData: LiveData<List<FileData>> = repository.allData

    fun insertData(fileData: FileData) {
        // 后台携程执行
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertData(fileData)
        }
    }

    fun updateData(fileData: FileData) {
        // 后台携程执行
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateData(fileData)
        }
    }

    fun deleteData(fileData: FileData) {
        // 后台携程执行
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteData(fileData)
        }
    }

}