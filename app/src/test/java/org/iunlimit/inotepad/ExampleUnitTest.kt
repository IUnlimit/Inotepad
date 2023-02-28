package org.iunlimit.inotepad

import okhttp3.*
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun ok_http() {
        val request = Request.Builder()
            .url("https://v1.hitokoto.cn/?c=j&encode=text")
            .get()
            .build()

//        异步调用
//        OkHttpClient().newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                assertNull(e)
//            }
//            override fun onResponse(call: Call, response: Response) {
//                println(response.message())
//            }
//        })

        val response = OkHttpClient().newCall(request).execute()
        assertNotNull(response.body())
        println("Code: ${response.code()}, Resp: ${response.body()?.string()}")
    }
}