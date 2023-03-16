package org.iunlimit.inotepad.fragments.update

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.databinding.FragmentUpdateBinding
import org.iunlimit.inotepad.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        // Spinner item selected spinner
        binding.currentFileTypeSpinner.onItemSelectedListener = sharedViewModel.listener

        // FloatingActionButton click listener
        binding.updateMenuSave.setOnClickListener {
            if (!updateData()) return@setOnClickListener
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        // FloatingActionButton click listener
        binding.updateMenuDelete.setOnClickListener {
            needConfirmDeleteData(args.currentItem, viewModel,requireContext()) {
                if (!it) return@needConfirmDeleteData
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateData(): Boolean {
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

        val fileData = FileData(args.currentItem.id, filename, FileType.parse(fileType), content)
        viewModel.updateData(fileData)
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