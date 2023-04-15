package org.iunlimit.inotepad.sdk

import com.kaopiz.kprogresshud.KProgressHUD
import com.theokanning.openai.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.service.OpenAiService.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Proxy
import java.nio.charset.Charset
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit


class ChatGPT(
    val ip: String?,
    val port: Int?
) {

    /**
     * @param content code
     * */
    @OptIn(DelicateCoroutinesApi::class)
    fun send(prompt: String, content: String, loading: KProgressHUD, callback: (String) -> Unit) {
        GlobalScope.launch {
            try {
                val service = createService(Base64.getDecoder()
                    .decode("c2stUWNOSUlpNVRRSzgyRDFoNlJ6c3hUM0JsYmtGSjV4M1Z6OU9WWURoSm5LZ1BSV3NE")
                    .toString(Charset.forName("UTF8")))

                val promptMessage = ChatMessage()
                promptMessage.let {
                    it.role = "system"
                    it.content = prompt
                }
                val text = if (content.length > 3000) content.substring(0, 3000) else content
                val contentMessage = ChatMessage()
                contentMessage.let {
                    it.role = "user"
                    it.content = text
                }

                val resp = service.createChatCompletion(
                    ChatCompletionRequest.builder().messages(listOf(promptMessage, contentMessage))
                    .model("gpt-3.5-turbo")
                    .maxTokens(3000)
                    .temperature(0.9)
                    .build()).choices[0].message.content

                callback.invoke(resp)
            } catch (e: Exception) {
                callback.invoke("API访问异常：\n${e.stackTraceToString()}")
            }
            loading.dismiss()
        }
    }

    private fun createService(token: String): OpenAiService {
        val mapper = defaultObjectMapper()
        val proxy = if (ip != null && port != null) Proxy(Proxy.Type.HTTP, InetSocketAddress(ip, port)) else null
        val client = defaultClient(token, Duration.of(10, ChronoUnit.SECONDS))
            .newBuilder()
            .proxy(proxy)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = defaultRetrofit(client, mapper)
        val api = retrofit.create(OpenAiApi::class.java)
        return OpenAiService(api)
    }

}