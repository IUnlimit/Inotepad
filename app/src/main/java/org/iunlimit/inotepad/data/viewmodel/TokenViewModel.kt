package org.iunlimit.inotepad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.iunlimit.inotepad.data.models.Token
import org.iunlimit.inotepad.data.repository.TokenRepository
import java.util.Date
import java.util.UUID

class TokenViewModel(application: Application): AndroidViewModel(application) {

    private val tokenRepository = TokenRepository()

    /**
     * 验证 Token，返回 Token Id
     * @return null报错，0未找到
     * */
    fun verifyToken(token: String, callback: (Int?) -> Unit) {
        viewModelScope.launch (Dispatchers.IO) {
            val result = tokenRepository.queryToken(token)
            if (result == null) {
                callback.invoke(null)
                return@launch
            }
            if (Date().time - 15 * 60 * 1000 >= result.createTime!!.time) {
                // TODO 级联删除
                callback.invoke(0)
                return@launch
            }
            callback.invoke(result.id)
        }
    }

    /**
     * 生成 Token
     * */
    fun generateToken(callback: (Int, String) -> Unit) {
        viewModelScope.launch (Dispatchers.IO) {
            val uuid = UUID.randomUUID().toString()
            val token = Token(
                id = 0,
                token = uuid,
                createTime = Date()
            )
            tokenRepository.insertToken(token)
            callback.invoke(token.id!!, uuid)
        }
    }

}