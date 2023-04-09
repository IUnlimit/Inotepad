package org.iunlimit.inotepad.sdk

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest
import com.alibaba.sdk.android.oss.model.DeleteObjectResult
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

class OSSHolder(
    val context: Context,
    val sts: STS
) {

    private var oss: OSS = create()

    fun upload(fileName: String, filePath: String): String {
        val put = PutObjectRequest(
            bucketName,
            baseUrl + fileName,
            filePath
        )

        val result = oss.putObject(put)
        Log.v("oss#upload", result.toString())
        return publicUrl + fileName
    }

    fun asyncUpload(fileName: String, filePath: String, success:(String) -> Unit) {
        val put = PutObjectRequest(
            bucketName,
            baseUrl + fileName,
            filePath
        )

        oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                Log.v("oss#upload", result.toString())
                success.invoke(publicUrl + fileName)
            }

            override fun onFailure(
                request: PutObjectRequest?,
                clientException: ClientException?,
                serviceException: ServiceException?
            ) {
                clientException?.printStackTrace()
                serviceException?.printStackTrace()
            }

        })
    }

    fun delete(fileName: String): OSSAsyncTask<DeleteObjectResult>? {
        val delete = DeleteObjectRequest(
            bucketName,
            baseUrl + fileName
        )
        return oss.asyncDeleteObject(
            delete,
            object : OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> {
                override fun onSuccess(request: DeleteObjectRequest?, result: DeleteObjectResult?) {
                    Log.v("oss#delete", "Delete status: ${result?.statusCode}")
                }

                override fun onFailure(
                    request: DeleteObjectRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    clientException?.printStackTrace()
                    serviceException?.printStackTrace()
                }

            })
    }

    private fun create(): OSS {
        val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
            sts.accessKeyId,
            sts.accessKeySecret,
            sts.securityToken
        )
        return OSSClient(context, endpoint, credentialProvider)
    }

    companion object {
        const val endpoint = "oss-cn-hangzhou.aliyuncs.com"
        const val bucketName = "illtamer-web-picture"
        const val baseUrl = "public/i-notepad/"
        const val publicUrl = "https://illtamer-web-picture.oss-cn-hangzhou.aliyuncs.com/$baseUrl"
    }

}