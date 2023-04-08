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
import org.iunlimit.inotepad.fragments.setFont

class AddFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var tempData: FileData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

//        binding.fileTypeSpinner.adapter = FontArrayAdapter(requireContext(), R.id.spinner_layout, resources.getStringArray(R.array.file_type))

        binding.addPreview.setOnClickListener {
            if (!tempSaveData()) return@setOnClickListener
            val action = AddFragmentDirections.actionAddFragmentToWebViewFragment(tempData!!)
            findNavController().navigate(action)
        }

        binding.addCheck.setOnClickListener {
            if (!insertData()) return@setOnClickListener
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setFont(requireView().findViewById(R.id.filename_et))
        setFont(requireView().findViewById(R.id.content_et))
    }

    private fun tempSaveData(): Boolean {
        val filename = binding.filenameEt.text.toString()
        val fileType = binding.fileTypeSpinner.selectedItem.toString()
        val content = binding.contentEt.text.toString()

        if (TextUtils.isEmpty(filename) || TextUtils.isEmpty(content)) {
            MaterialDialog(requireContext()).show {
                cornerRadius(16f)
                title(R.string.not_null)
                positiveButton(R.string.ok)
            }
            return false
        }

        tempData = FileData(0, filename, FileType.parse(fileType), content, null)
        return true
    }

    private fun insertData(): Boolean {
        if (!tempSaveData()) return false
        viewModel.insertData(tempData!!)
        MaterialDialog(requireContext()).show {
            cornerRadius(16f)
            title(R.string.insert_success)
            positiveButton(R.string.ok)
        }
        return true
    }

}