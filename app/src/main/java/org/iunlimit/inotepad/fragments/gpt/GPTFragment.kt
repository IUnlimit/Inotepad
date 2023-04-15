package org.iunlimit.inotepad.fragments.gpt

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.kaopiz.kprogresshud.KProgressHUD
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.models.MD_CLAZZ
import org.iunlimit.inotepad.databinding.FragmentGptBinding
import org.iunlimit.inotepad.fragments.setFont
import org.iunlimit.inotepad.sdk.ChatGPT
import java.io.File
import java.nio.charset.Charset

class GPTFragment: Fragment() {

    private val viewModel: RequestViewModel by viewModels()

    private var _binding: FragmentGptBinding? = null
    private val binding get() = _binding!!

    private var content: String = ""
    private var prompt: String = ""
    private var proxyPair: Pair<String, Int>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGptBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.requestModel = viewModel

        binding.buttonSelectFile.setOnClickListener {
            viewModel.fileDao.getAllData().observe(viewLifecycleOwner) { data ->
                selectFileData(data)
            }
        }

        binding.buttonSelectPrompt.setOnClickListener {
            selectHandle(R.string.select_prompt, PROMPT_MAP.keys.toList()) { _, key ->
                prompt = PROMPT_MAP[key]!!
                viewModel.papiSelectPrompt.value = key
            }
        }

        binding.buttonProxy.setOnClickListener {
            setProxy {
                val split = it.split(":")
                proxyPair = Pair(split[0], split[1].toInt())
            }
        }

        binding.buttonRequest.setOnClickListener {
            val gpt = ChatGPT(proxyPair?.first, proxyPair?.second)
            val loading = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.loading_label))
                .setDetailsLabel(getString(R.string.loading_detail_label))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()

            gpt.send(prompt, content, loading) {
                binding.root.post {
                    viewModel.resp.value = it
                }
            }
        }

        binding.buttonPreview.setOnClickListener {
            val action =
                GPTFragmentDirections.actionGPTFragmentToWebViewFragment(
                    FileData(
                        id = 0,
                        name = "",
                        type = FileType.MD,
                        content = viewModel.resp.value!!,
                        filePath = null
                    )
                )
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setFont(requireView().findViewById(R.id.resp_textView))
    }

    @SuppressLint("CheckResult")
    private fun setProxy(callback: (ipHost: String) -> Unit) {
        MaterialDialog(requireContext()).show {
            title(R.string.proxy_title)
            input(hintRes = R.string.proxy_default, waitForPositiveButton = false) { dialog, text ->
                val inputField = dialog.getInputField()

                val isValid = run {
                    val contains = text.contains(":") && text.indexOf(":")+1 != text.length
                    if (!contains) inputField.error = "地址格式错误，应为 'IP:HOST'"
                    contains
                }/* && run {
                    try {
                        val split = text.split(":")
                        Socket(split[0], split[1].toInt()).use {
                            if (it.isClosed) inputField.error = "无法连接到代理服务器"
                            !it.isClosed
                        }
                    } catch (e: java.lang.Exception) {
                        inputField.error = e.message
                        false
                    }
                }*/
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
            }
            positiveButton(R.string.ok) {
                callback.invoke(it.getInputField().text.toString())
            }
        }
    }

    private fun selectFileData(fileDataList: List<FileData>) {
        val editableList = fileDataList.filter { it.type.clazz <= MD_CLAZZ }
        val filenameList = editableList.map { it.name + it.type.value }
        selectHandle(R.string.select_file, filenameList) { index, key ->
            val fileData = editableList[index]
            content =
                if (fileData.filePath != null) {
                    File(fileData.filePath!!).readText(Charset.forName("UTF8"))
                } else {
                    fileData.content
                }
            viewModel.papiSelectFile.value = key
        }
    }

    @SuppressLint("CheckResult")
    private fun selectHandle(title: Int, array: List<String>, callback: (index: Int, key: String) -> Unit) {
        MaterialDialog(requireContext()).show {
            title(title)
            listItems(items = array) { _, index, text ->
                Log.v("gpt#file", "$index : $text")
                callback.invoke(index, text as String)
            }
        }
    }

    companion object {

        private const val I18N = "Reply in Chinese."
        private const val MARKDOWN_FORMAT = "And if you need to display content such as codes or tables, please use markdown syntax.";

        // https://github.com/camsong/chatgpt-engineer-prompts
        val PROMPT_MAP: Map<String, String> = mapOf(
            Pair("explain or summarize text", "Please explain the following text, if the content is a code, then explain the specific meaning of this code, otherwise, summarize the general idea of the text. $I18N $MARKDOWN_FORMAT"),
            Pair("optimize code", "Please help me to optimize the following code and explain the idea and reason of optimization. The optimized code and the explanation are separated by the <hr> tag. $I18N $MARKDOWN_FORMAT")
        )
    }

}