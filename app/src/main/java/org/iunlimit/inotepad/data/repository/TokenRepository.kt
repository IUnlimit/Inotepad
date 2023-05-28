package org.iunlimit.inotepad.data.repository

import org.iunlimit.inotepad.data.models.Token
import org.iunlimit.inotepad.data.viewmodel.daoSupplier

class TokenRepository {

    fun insertToken(token: Token): Int {
        return daoSupplier(Token::class.java).create(token)
    }

    fun queryToken(token: String): Token? {
        val result = daoSupplier(Token::class.java).queryForEq("token", token)
        if (result.size == 0) return null
        return result[0]
    }

}