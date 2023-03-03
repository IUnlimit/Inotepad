package org.iunlimit.inotepad.data

import androidx.room.TypeConverter
import org.iunlimit.inotepad.data.models.FileType

class TypeConverter {

    @TypeConverter
    fun format(fileType: FileType): String {
        return fileType.value
    }

    @TypeConverter
    fun parse(fileType: String): FileType {
        return FileType.parse(fileType)
    }

}