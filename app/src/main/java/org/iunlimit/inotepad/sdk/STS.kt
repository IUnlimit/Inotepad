package org.iunlimit.inotepad.sdk

import com.aliyun.sts20150401.Client
import com.aliyun.sts20150401.models.AssumeRoleRequest
import com.aliyun.teaopenapi.models.Config
import com.aliyun.teautil.models.RuntimeOptions

val sts = generateSTS()!!

fun generateSTS(): STS? {
    val client = Client(
        Config()
            .setAccessKeyId(STS.accessKeyId)
            .setAccessKeySecret(STS.accessKeySecret)
            .setEndpoint(STS.endpoint)
            .setRegionId(STS.regionId)
    )
    val request = AssumeRoleRequest()
        .setRoleSessionName(STS.sessionName)
        .setRoleArn(STS.arn)
        .setDurationSeconds(STS.durationSeconds)
    val runtime = RuntimeOptions()
    try {
        val response = client.assumeRoleWithOptions(request, runtime)
        val credentials = response.body.credentials
        return STS(
            credentials.accessKeyId,
            credentials.accessKeySecret,
            credentials.securityToken
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
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