package org.iunlimit.inotepad.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.molihuan.pathselector.PathSelector
import com.molihuan.pathselector.entity.FileBean
import com.molihuan.pathselector.fragment.BasePathSelectFragment
import com.molihuan.pathselector.listener.FileItemListener
import com.molihuan.pathselector.utils.MConstants
import com.molihuan.pathselector.utils.Mtools
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.databinding.FragmentListBinding
import org.iunlimit.inotepad.fragments.SharedViewModel
import org.iunlimit.inotepad.fragments.list.adapter.ListAdapter
import org.iunlimit.inotepad.fragments.setFont
import org.iunlimit.inotepad.fragments.update.UpdateFragment
import java.io.File


class ListFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

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

        binding.menuCreate.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
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
        viewModel.allData.observe(viewLifecycleOwner) { data ->
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

        // BUG 不响应 notifyItemChanged
//        recyclerView.itemAnimator = SlideInUpAnimator().apply {
//            addDuration = 300
//        }

        // Swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun importFile(filePath: String) {
        val file = File(filePath)
        val divIndex = filePath.lastIndexOf('/')
        val typeSplitIndex = filePath.lastIndexOf('.')
        val fileType = if (typeSplitIndex != -1 && typeSplitIndex > divIndex) {
            FileType.parse(filePath.substring(typeSplitIndex))
        } else FileType.UNKNOWN

        if (fileType == FileType.UNKNOWN) {
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.unsupported_type)
                message(R.string.unsupported_type_content)
                positiveButton(R.string.confirm) {
                    viewModel.insertData(file, FileType.UNKNOWN)
                }
                negativeButton(R.string.cancel)
            }
            return
        }

        viewModel.insertData(file, fileType)
        Log.v("list", "Import with type $fileType")
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val callback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                UpdateFragment.needConfirmDeleteData(itemToDelete, viewModel, requireContext()) { delete ->
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
        viewModel.searchData(query).observe(viewLifecycleOwner) {
            it.let {
                adapter.setData(it)
            }
        }
    }



}