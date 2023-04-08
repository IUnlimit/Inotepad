package org.iunlimit.inotepad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iunlimit.inotepad.data.FileDatabase
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.repository.FileRepository
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

class FileViewModel(application: Application): AndroidViewModel(application) {

    private val charset = Charset.forName("UTF-8")
    private val fileDao = FileDatabase.getDatabase(application).fileDao()
    private val repository: FileRepository = FileRepository(fileDao)
    val allData: LiveData<List<FileData>> = repository.allData

    fun insertData(file: File, fileType: FileType) {
        assert(file.exists())

        val content = if (fileType.isEditable()) {
            FileInputStream(file).use {
                it.readBytes().toString(charset)
            }
        } else "[${fileType.value.substring(1).uppercase()}]"

        val takeIf = file.name.lastIndexOf('.').takeIf { it != -1 }
        val name = takeIf?.let { file.name.substring(0, it) } ?: file.name
        val type = takeIf?.let { file.name.substring(it) }.let { FileType.parse(it) }
        val data = FileData(
            id = 0,
            name = name,
            type = type,
            content = content,
            filePath = file.path
        )
        insertData(data)
    }

    fun insertData(fileData: FileData) {
        // 后台携程执行
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertData(fileData)
        }
    }

    fun updateData(fileData: FileData) {
        fileData.filePath?.let {
            File(it).writeText(fileData.content, charset)
        }
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

    fun searchData(query: String): LiveData<List<FileData>> {
        return repository.searchData(query)
    }

}