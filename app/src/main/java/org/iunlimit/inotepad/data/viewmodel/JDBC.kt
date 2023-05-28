package org.iunlimit.inotepad.data.viewmodel

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource

const val HOST = "47.117.136.149"
const val PORT = 2436
const val DB = "i_notepad"
const val MYSQL_URL = "jdbc:mysql://$HOST:$PORT/$DB?useSSL=false&autoReconnect=true&username="
const val USERNAME = "root"
const val PASSWORD = "#IllTamer!"

fun <T> daoSupplier(clazz: Class<T>): Dao<T, *> {
    val source = JdbcConnectionSource(MYSQL_URL)
    source.setUsername(USERNAME)
    source.setPassword(PASSWORD)
    return DaoManager.createDao(source, clazz)
}
