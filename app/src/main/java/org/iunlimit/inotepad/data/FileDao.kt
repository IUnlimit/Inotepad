package org.iunlimit.inotepad.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.iunlimit.inotepad.data.models.FileData

@Dao
interface FileDao {

    @Query(
        "SELECT * FROM file_data_table ORDER BY id ASC"
    )
    fun getAllData(): LiveData<List<FileData>>

    @Insert(
        // 插入数据重复时，忽略
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertData(fileData: FileData)

    @Update
    suspend fun updateData(fileData: FileData)

    @Delete
    suspend fun deleteData(fileData: FileData)

}