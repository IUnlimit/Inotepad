package org.iunlimit.inotepad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iunlimit.inotepad.data.models.Backup
import org.iunlimit.inotepad.data.repository.BackupRepository

class BackupViewModel(application: Application): AndroidViewModel(application) {

    private val backupRepository = BackupRepository()

    fun batchInsertData(backupList: List<Backup>, loading: KProgressHUD) {
        viewModelScope.launch (Dispatchers.IO) {
            backupRepository.batchInsertBackup(backupList)
            loading.dismiss()
        }
    }

    fun batchDeleteData(userId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            backupRepository.batchDeleteBackup(userId)
        }
    }

    fun batchQueryData(userId: Int, callback: (List<Backup>) -> Unit) {
        viewModelScope.launch (Dispatchers.IO) {
            callback.invoke(backupRepository.batchQueryBackup(userId))
        }
    }

}