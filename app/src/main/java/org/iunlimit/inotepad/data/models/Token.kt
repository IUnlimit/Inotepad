package org.iunlimit.inotepad.data.models

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.Date

@DatabaseTable(
    tableName = "token"
)
class Token(

    @DatabaseField(
        generatedId = true
    )
    var id: Int? = null,

    @DatabaseField(
        columnName = "token"
    )
    var token: String? = null,

    @DatabaseField(
        columnName = "create_time"
    )
    var createTime: Date? = null

)