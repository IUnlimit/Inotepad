package org.iunlimit.inotepad.sdk

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kaopiz.kprogresshud.KProgressHUD
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

class API2D(
    val host: String?,
    val port: Int?
) {

    private val jsonType = "application/json;charset=utf-8".toMediaTypeOrNull()
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .proxy(let {
        var proxy: Proxy? = null
        if (host != null && port !== null)
            proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
        proxy
    }).build()

    fun send(prompt: String, content: String, loading: KProgressHUD?, callback: (String) -> Unit) {
        val json = let {
            val json = JsonObject()
            json.addProperty("model", "gpt-3.5-turbo")
            json.addProperty("max_tokens", 3000)
            json.addProperty("temperature", 0.9)
            json.add("messages", let {
                val array = JsonArray()
                array.add(let {
                    val entity1 = JsonObject()
                    entity1.addProperty("role", "system")
                    entity1.addProperty("content", prompt)
                    entity1
                })
                array.add(let {
                    val entity2 = JsonObject()
                    entity2.addProperty("role", "user")
                    entity2.addProperty("content", content)
                    entity2
                })
                array
            })
            json
        }

        val jsonStr = Gson().toJson(json)
        val body = RequestBody.create(jsonType, jsonStr)
        val request = Request.Builder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $FORWARD_KEY")
            .url("https://openai.api2d.net/v1/chat/completions")
            .post(body).build();
        val call = okHttpClient.newCall(request);
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                loading?.dismiss()
                callback("API访问异常：\n${e.stackTraceToString()}")
            }

            override fun onResponse(call: Call, response: Response) {
                loading?.dismiss()
                if (!response.isSuccessful) {
//                    Log.v("api2d", "POST-ASYNC failed ${response.code}");
                    println(response.code)
                    return
                }
                val resp = response.body?.string().toString()
//                Log.v("api2d", "POST-ASYNC $resp");
                val result = Gson().fromJson(resp, JsonObject::class.java)
                callback.invoke(result["choices"].asJsonArray[0].asJsonObject["message"].asJsonObject["content"].asString)
            }
        })
    }

    companion object {
        private const val FORWARD_KEY = "fk203108-W7SOlEOgoTMwU4W3h35ktZpS3uHiRNzJ"
    }

}