package org.iunlimit.inotepad.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.databinding.FragmentAddBinding
import org.iunlimit.inotepad.fragments.SharedViewModel

class AddFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.fileTypeSpinner.onItemSelectedListener = sharedViewModel.listener

        // FloatingActionButton click listener
        binding.addCheck.setOnClickListener {
            if (!insertData()) return@setOnClickListener
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        return binding.root
    }

    private fun insertData(): Boolean {
        val filename = binding.filenameEt.text.toString()
        val fileType = binding.fileTypeSpinner.selectedItem.toString()
        val content = binding.contentEt.text.toString()

        if (TextUtils.isEmpty(filename) || TextUtils.isEmpty(content)) {
            MaterialDialog(requireContext()).show {
                title(R.string.not_null)
                positiveButton(R.string.ok)
            }
            return false
        }

        val fileData = FileData(0, filename, FileType.parse(fileType), content)
        viewModel.insertData(fileData)
        MaterialDialog(requireContext()).show {
            title(R.string.insert_success)
            positiveButton(R.string.ok)
        }
        return true
    }

}