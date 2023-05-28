package org.iunlimit.inotepad.fragments.list

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.kaopiz.kprogresshud.KProgressHUD
import com.molihuan.pathselector.PathSelector
import com.molihuan.pathselector.entity.FileBean
import com.molihuan.pathselector.fragment.BasePathSelectFragment
import com.molihuan.pathselector.listener.FileItemListener
import com.molihuan.pathselector.utils.MConstants
import com.molihuan.pathselector.utils.Mtools
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.Backup
import org.iunlimit.inotepad.data.models.COMPRESS_CLAZZ
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.models.IMAGE_CLAZZ
import org.iunlimit.inotepad.data.viewmodel.BackupViewModel
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.data.viewmodel.TokenViewModel
import org.iunlimit.inotepad.databinding.FragmentListBinding
import org.iunlimit.inotepad.fragments.SharedViewModel
import org.iunlimit.inotepad.fragments.list.adapter.ListAdapter
import org.iunlimit.inotepad.fragments.setFont
import org.iunlimit.inotepad.fragments.update.UpdateFragment
import org.iunlimit.inotepad.util.checkNetworkAvailable
import java.io.File
import java.nio.charset.Charset
import java.util.stream.Collectors


open class ListFragment : Fragment() {

    private val fileViewModel: FileViewModel by viewModels()
    private val backupViewModel: BackupViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter(this) }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        setupRecyclerView()

        binding.cloudMenuUpload.setOnClickListener {
            checkNetWork {
                uploadDataToCloud()
            }
        }

        binding.cloudMenuDownload.setOnClickListener {
            checkNetWork {
                downloadDataFromCloud()
            }
        }

        binding.menuCreate.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.menuChatgpt.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_GPTFragment)
        }

        binding.menuSearch.setOnClickListener {
            searchData()
        }

        binding.menuImport.setOnClickListener {
            //如果没有权限会自动申请权限
            PathSelector.build(this, MConstants.BUILD_DIALOG)
                .setShowTitlebarFragment(false)
                .setFileItemListener(//设置文件item点击回调(点击是文件才会回调,如果点击是文件夹则不会)
                    object : FileItemListener() {
                        override fun onClick(
                            v: View?,
                            file: FileBean?,
                            currentPath: String?,
                            fragment: BasePathSelectFragment?
                        ): Boolean {
                            fragment?.dismiss()
                            val filePath = file?.path ?: return false
                            Mtools.toast("select file:\n$filePath")
                            importFile(filePath)
                            return false
                        }
                    }
                )
                .show()
        }

        // observe LiveData
        fileViewModel.allData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkDatabaseEmpty(data)
            adapter.setData(data)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setFont(requireView().findViewById(R.id.no_data_textView))
    }

    // avoid memory release
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun checkNetWork(callback: () -> Unit) {
        if (!checkNetworkAvailable(requireActivity())) {
            // network err
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.upload_failed)
                message(R.string.uoload_failed_label)
                positiveButton(R.string.ok)
            }
            return
        }
        callback.invoke()
    }

    private fun uploadDataToCloud() {
        MaterialDialog(requireContext()).show {
            cornerRadius(16f)
            title(R.string.upload)
            val dialog = input(hintRes = R.string.upload_label)

            negativeButton(R.string.upload_new) {
                tokenViewModel.generateToken { userId, token ->
                    // 拷贝uuid到剪贴板
                    val clip = getSystemService(requireContext(), ClipboardManager::class.java)
                    clip!!.setPrimaryClip(ClipData.newPlainText(getString(R.string.clip_token_label), token))
                    Log.v("clip", clip.primaryClip?.getItemAt(0)?.text.toString())
                    Looper.prepare()
                    Mtools.toast(getString(R.string.token_generate))
                    doBatchUpload(userId)
                    Looper.loop()
                }
            }

            positiveButton(R.string.confirm) {
                val token = dialog.getInputField().text
                tokenViewModel.verifyToken(token.toString()) {
                    if (!checkUserStates(it)) return@verifyToken
                    backupViewModel.batchDeleteData(it!!)
                    Looper.prepare()
                    doBatchUpload(it)
                    Looper.loop()
                }
            }
        }
    }

    private fun downloadDataFromCloud() {
        MaterialDialog(requireContext()).show {
            cornerRadius(16f)
            title(R.string.download)
            val dialog = input(hintRes = R.string.download_label)

            negativeButton(R.string.cancel)
            positiveButton(R.string.confirm) {
                val token = dialog.getInputField().text
                tokenViewModel.verifyToken(token.toString()) {
                    if (!checkUserStates(it)) return@verifyToken
                    // loading
                    Looper.prepare()
                    val loading = KProgressHUD.create(requireContext())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel(getString(R.string.loading_label))
                        .setDetailsLabel(getString(R.string.loading_download_detail_label))
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show()
                    backupViewModel.batchQueryData(it!!) { backupList ->
                        doBatchDownload(backupList)
                        loading.dismiss()
                    }
                    Looper.loop()
                }

            }
        }
    }

    private fun checkUserStates(userId: Int?): Boolean {
        // not found
        if (userId == null || userId == 0) {
            Looper.prepare()
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.token_mistake)
                positiveButton(R.string.ok)
            }
            Looper.loop()
            return false
        }
        return true
    }

    private fun doBatchUpload(userId: Int) {
        Log.v("upload", "检测到${adapter.dataList.size}个数据项")
        val collect = adapter.dataList.stream().map {
            val isFile = it.filePath != null
            if (isFile && File(it.filePath!!).length() / 1024 / 1024 >= 10) {
                Looper.prepare()
                MaterialDialog(requireContext()).show {
                    cornerRadius(16f)
                    title(text = "文件${it.name}过大")
                    message(R.string.too_large_upload_content)
                    positiveButton(R.string.confirm)
                }
                Looper.loop()
            }
            val backup = Backup(
                id = 0,
                userId = userId,
                fileName = it.name,
                fileType = it.type.value,
                file = isFile,
                content = if (isFile) {
                    File(it.filePath!!).readBytes()
                } else {
                    it.content.toByteArray()
                }
            )
            return@map backup
        }.collect(Collectors.toList())
        // loading
        val loading = KProgressHUD.create(requireContext())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(R.string.loading_label))
            .setDetailsLabel(getString(R.string.loading_upload_detail_label))
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
        backupViewModel.batchInsertData(collect, loading)
    }

    private fun doBatchDownload(backupList: List<Backup>) {
        Log.v("download", "检测到${adapter.dataList.size}个数据项")
        backupList.forEach {
            val fileType = FileType.parse(it.fileType)
            if (it.file!!) {
                val file = File(requireContext().filesDir, it.fileName!! + it.fileType!!)
                if (file.exists()) file.deleteRecursively()
                file.createNewFile()
                file.writeBytes(it.content!!)
                fileViewModel.insertData(file, fileType)
                return@forEach
            }
            val fileData = FileData(
                id = 0,
                name = it.fileName!!,
                type = fileType,
                content = it.content!!.toString(CHARSET),
                filePath = null
            )
            fileViewModel.insertData(fileData)
        }
    }

    fun importFile(filePath: String) {
        val file = File(filePath)
        val divIndex = filePath.lastIndexOf('/')
        val typeSplitIndex = filePath.lastIndexOf('.')
        val fileType = if (typeSplitIndex != -1 && typeSplitIndex > divIndex) {
            FileType.parse(filePath.substring(typeSplitIndex))
        } else FileType.UNKNOWN

        val sizeKB = file.length() / 1024
        // pdf image排外 其他限制
        if (fileType != FileType.PDF && !arrayOf(IMAGE_CLAZZ, COMPRESS_CLAZZ).contains(fileType.clazz) && sizeKB > 1024) {
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.too_large)
                message(R.string.too_large_content)
                positiveButton(R.string.confirm)
            }
            return
        }

        if (fileType == FileType.UNKNOWN) {
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.unsupported_type)
                message(R.string.unsupported_type_content)
                positiveButton(R.string.confirm) {
                    fileViewModel.insertData(file, FileType.UNKNOWN)
                }
                negativeButton(R.string.cancel)
            }
            return
        }

        fileViewModel.insertData(file, fileType)
        Log.v("list", "Import with type $fileType")
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val callback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                UpdateFragment.needConfirmDeleteData(itemToDelete, fileViewModel, requireContext()) { delete ->
                    if (delete)
                        adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    else
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                }
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun searchData() {
        MaterialDialog(requireContext()).show {
            cornerRadius(16f)
            title(R.string.search_title)
            val dialog = input(hintRes = R.string.search_tip)
            negativeButton(R.string.cancel)
            positiveButton(R.string.confirm) {
                val text = dialog.getInputField().text
                searchThroughDatabase(text.toString())
            }
        }
    }

    private fun searchThroughDatabase(content: String) {
        val query = "%$content%"
        fileViewModel.searchData(query).observe(viewLifecycleOwner) {
            it.let {
                adapter.setData(it)
            }
        }
    }

    companion object {

        val CHARSET: Charset = Charset.forName("UTF-8");

    }

}