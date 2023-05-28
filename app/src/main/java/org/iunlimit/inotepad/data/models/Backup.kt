package org.iunlimit.inotepad.data.models

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(
    tableName = "backup"
)
class Backup(

    @DatabaseField(
        generatedId = true
    )
    var id: Int? = null,

    @DatabaseField(
        columnName = "user_id"
    )
    var userId: Int? = null,

    @DatabaseField(
        columnName = "file_name"
    )
    var fileName: String? = null,

    @DatabaseField(
        columnName = "file_type"
    )
    var fileType: String? = null,

    @DatabaseField(
        columnName = "content",
        dataType = DataType.BYTE_ARRAY
    )
    var content: ByteArray? = null,

    @DatabaseField(
        columnName = "file"
    )
    var file: Boolean? = null

)