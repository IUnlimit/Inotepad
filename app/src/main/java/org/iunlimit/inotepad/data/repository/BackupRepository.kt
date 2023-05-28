package org.iunlimit.inotepad.data.repository

import org.iunlimit.inotepad.data.models.Backup
import org.iunlimit.inotepad.data.viewmodel.daoSupplier


class BackupRepository {

    fun batchInsertBackup(backupList: List<Backup>) {
        daoSupplier(Backup::class.java).create(backupList)
    }

    fun batchDeleteBackup(userId: Int) {
        val builder = daoSupplier(Backup::class.java).deleteBuilder()
        builder.where().eq("user_id", userId)
        builder.delete()
    }

    fun batchQueryBackup(userId: Int): List<Backup> {
        return daoSupplier(Backup::class.java).queryForEq("user_id", userId)
    }

}
