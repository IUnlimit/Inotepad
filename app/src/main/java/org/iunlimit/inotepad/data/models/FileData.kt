package org.iunlimit.inotepad.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "file_data_table"
)
@Parcelize
data class FileData(
    @PrimaryKey(
        autoGenerate = true
    )
    var id: Int,
    var name: String,
    var type: FileType,
    var content: String,
    var filePath: String?
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileData

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (content != other.content) return false
        if (filePath != other.filePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (filePath?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "FileData(id=$id, name='$name', type=$type, content='$content', filePath=$filePath)"
    }


}