package org.iunlimit.inotepad.data.repository

import androidx.lifecycle.LiveData
import org.iunlimit.inotepad.data.FileDao
import org.iunlimit.inotepad.data.models.FileData

class FileRepository(
    private val fileDao: FileDao
) {

    val allData: LiveData<List<FileData>> = fileDao.getAllData()

    suspend fun insertData(fileData: FileData) {
        fileDao.insertData(fileData)
    }

    suspend fun updateData(fileData: FileData) {
        fileDao.updateData(fileData)
    }

    suspend fun deleteData(fileData: FileData) {
        fileDao.deleteData(fileData)
    }

    fun searchData(query: String): LiveData<List<FileData>> {
        return fileDao.searchData(query)
    }

}