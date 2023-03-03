package org.iunlimit.inotepad.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.fragments.SharedViewModel

class AddFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        view.findViewById<Spinner>(R.id.file_type_spinner).onItemSelectedListener = sharedViewModel.listener

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.add_check).setOnClickListener {
            if (!insertData(view)) return@setOnClickListener
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        return view
    }

    private fun insertData(view: View): Boolean {
        val filename = view.findViewById<EditText>(R.id.filename_et).text.toString()
        val fileType = view.findViewById<Spinner>(R.id.file_type_spinner).selectedItem.toString()
        val content = view.findViewById<EditText>(R.id.content_et).text.toString()

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