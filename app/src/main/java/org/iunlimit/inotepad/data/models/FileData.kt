package org.iunlimit.inotepad.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "file_data_table"
)
@Parcelize
data class FileData (
    @PrimaryKey(
        autoGenerate = true
    )
    var id: Int,
    var name: String,
    var type: FileType,
    var content: String
): Parcelable