package org.iunlimit.inotepad.sdk

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

var sts: STS? = null

fun generateSTS() {
    val url = "http://illtamer.com:28888/?id=${STS.accessKeyId}&secret=${STS.accessKeySecret}";
    val request = Request.Builder().url(url).get().build();
    val client = OkHttpClient();
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val string = response.body!!.string()
            val json = Gson().fromJson(string, JsonObject::class.java)
            sts = STS(
                json["AccessKeyId"].asString,
                json["AccessKeySecret"].asString,
                json["SecurityToken"].asString
            )
            Log.i("sts", "sts generate: $sts")
        }

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }
    })
}

data class STS(
    val accessKeyId: String,
    val accessKeySecret: String,
    val securityToken: String,
) {
    companion object {
        // RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou
        const val regionId = "cn-hangzhou"
        const val endpoint = "sts.cn-hangzhou.aliyuncs.com"
        const val accessKeyId = "LTAI5tNM6Z5AXR7faQKLCkCY"
        const val accessKeySecret = "3Pp7wOnwztkBqG9yUqxFfDiHPAcjZs"
        const val sessionName = "INotepad"
        const val arn = "acs:ram::1839416139006702:role/sts-oss"
        const val durationSeconds = 60 * 60L // 1h
    }

    override fun toString(): String {
        return "STS(accessKeyId='$accessKeyId', accessKeySecret='$accessKeySecret', securityToken='$securityToken')"
    }

}