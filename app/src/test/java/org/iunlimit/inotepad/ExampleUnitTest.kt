package org.iunlimit.inotepad

import com.google.gson.Gson
import com.google.gson.JsonObject
import junit.framework.Assert.assertNotNull
import org.iunlimit.inotepad.sdk.STS
import org.iunlimit.inotepad.sdk.generateSTS
import org.junit.Test
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Socket


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun sts() {
//        generateSTS()
//        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 7890))
//        val socket = Socket(proxy)
//        socket.connect(InetSocketAddress("127.0.0.1", 25565))
//        println(socket.isConnected)
    }

    @Test
    fun ftp() {
//        val conn = org.iunlimit.inotepad.util.ftp("47.117.136.149", 22, "illtamer", "cxzzpz99")
//        println(conn?.ftpClient?.listFiles())
    }

    @Test
    fun chatGPT() {
//        org.iunlimit.inotepad.sdk.API2D(null, null).send("你是一只猫，无论我发送什么你都需要在语句结尾处加上“喵”", "你好呀", null) {
//            println(it)
//        }
//        Thread.sleep(100000)
    }

}