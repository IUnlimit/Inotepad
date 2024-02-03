package org.iunlimit.inotepad.fragments.preview

import android.content.res.AssetManager
import fi.iki.elonen.NanoHTTPD
import java.io.File

class HttpDServer(
    val port: Int
): NanoHTTPD(port) {

    override fun serve(session: IHTTPSession?): Response {
        // 根据请求路径加载相应的静态资源文件
        // 根据请求路径加载相应的静态资源文件
        val uri = session!!.uri
        val mimeType = "text/html"
        var responseString = ""

        println(uri)
        val file = File("file:///android_asset/blob/dist/index.html")
        responseString = file.readBytes().toString()

        return newFixedLengthResponse(Response.Status.OK, mimeType, responseString)
    }

}