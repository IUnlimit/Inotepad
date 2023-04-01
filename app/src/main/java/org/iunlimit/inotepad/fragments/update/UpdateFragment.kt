package org.iunlimit.inotepad.fragments.update

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.databinding.FragmentUpdateBinding
import org.iunlimit.inotepad.fragments.FontArrayAdapter
import org.iunlimit.inotepad.fragments.SharedViewModel
import org.iunlimit.inotepad.fragments.setFont

class UpdateFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private var tempData: FileData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        // Spinner item selected spinner
//        binding.currentFileTypeSpinner.adapter = FontArrayAdapter(requireContext(), R.id.spinner_layout, resources.getStringArray(R.array.file_type))

        binding.updateMenuPreview.setOnClickListener {
            if (!tempSaveData()) return@setOnClickListener
            val action = UpdateFragmentDirections.actionUpdateFragmentToWebViewFragment(tempData!!.content, tempData!!.type)
            findNavController().navigate(action)
        }

        binding.updateMenuSave.setOnClickListener {
            if (!updateData()) return@setOnClickListener
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        binding.updateMenuDelete.setOnClickListener {
            needConfirmDeleteData(args.currentItem, viewModel, requireContext()) {
                if (!it) return@needConfirmDeleteData
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setFont(requireView().findViewById(R.id.current_filename_et))
        setFont(requireView().findViewById(R.id.current_content_et))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun tempSaveData(): Boolean {
        val filename = binding.currentFilenameEt.text.toString()
        val fileType = binding.currentFileTypeSpinner.selectedItem.toString()
        val content = binding.currentContentEt.text.toString()

        if (TextUtils.isEmpty(filename) || TextUtils.isEmpty(content)) {
            MaterialDialog(requireContext()).show {
                title(R.string.not_null)
                positiveButton(R.string.ok)
            }
            return false
        }

        tempData = FileData(args.currentItem.id, filename, FileType.parse(fileType), content, args.currentItem.filePath)
        return true
    }

    private fun updateData(): Boolean {
        if (!tempSaveData()) return false
        viewModel.updateData(tempData!!)
        MaterialDialog(requireContext()).show {
            title(R.string.update_success)
            positiveButton(R.string.ok)
        }
        return true
    }

    companion object {

        fun needConfirmDeleteData(fileData: FileData, viewModel: FileViewModel, ctx: Context, callback: ((Boolean) -> Unit)?) {
            MaterialDialog(ctx).show {
                title(R.string.delete_confirm)
                message(text = "即将删除 '${fileData.name}' ！")
                positiveButton(R.string.cancel) {
                    callback?.let { func -> func(false) }
                }
                negativeButton(R.string.confirm) {
                    viewModel.deleteData(fileData)
                    MaterialDialog(ctx).show {
                        title(R.string.delete_success)
                        positiveButton(R.string.ok)
                    }
                    callback?.let { func -> func(true) }
                }
            }
        }

    }

}