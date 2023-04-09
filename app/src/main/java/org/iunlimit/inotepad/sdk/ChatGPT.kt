package org.iunlimit.inotepad.sdk

import com.plexpt.chatgpt.ChatGPT
import com.plexpt.chatgpt.entity.chat.ChatCompletion
import com.plexpt.chatgpt.entity.chat.Message
import com.plexpt.chatgpt.util.Proxys

class ChatGPT(
    val ip: String = "127.0.0.1",
    val port: Int = 7890
) {

    val client: ChatGPT = ChatGPT.builder()
        .apiKey("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")
        .proxy(Proxys.http(ip, port))
        .apiHost("https://api.openai.com/") //反向代理地址
        .build()
        .init()

    /**
     * @param content code
     * */
    fun analise(content: String, callback: (String) -> Unit) {
        val system = Message.ofSystem(ANALISE_PROMPT)
        val message = Message.of(content)

        val chatCompletion = ChatCompletion.builder()
            .model(ChatCompletion.Model.GPT_3_5_TURBO.name)
            .messages(listOf(system, message))
            .maxTokens(3000)
            .temperature(0.9)
            .build()
        val response = client.chatCompletion(chatCompletion)
        val res = response.choices[0].message
        callback.invoke(res.content)
    }

}

const val ANALISE_PROMPT = "You are a code analysis engineer. I need you to carefully analyze the specific functions of the code I gave and explain the functions of the lines, methods, parameters, classes, etc. that you think are worth noting. Reply in Chinese"