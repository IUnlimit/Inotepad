package org.iunlimit.inotepad.fragments.update

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
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.github.clans.fab.FloatingActionButton
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        view.findViewById<EditText>(R.id.current_filename_et).setText(args.currentItem.name)
        view.findViewById<EditText>(R.id.current_content_et).setText(args.currentItem.content)
        val spinnerView = view.findViewById<Spinner>(R.id.current_file_type_spinner)
        spinnerView.setSelection(args.currentItem.type.ordinal)
        spinnerView.onItemSelectedListener = sharedViewModel.listener

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.update_menu_save).setOnClickListener {
            if (!updateData(view)) return@setOnClickListener
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.update_menu_delete).setOnClickListener {
            deleteData(args.currentItem)
        }

        return view
    }

    private fun updateData(view: View): Boolean {
        val filename = view.findViewById<EditText>(R.id.current_filename_et).text.toString()
        val fileType = view.findViewById<Spinner>(R.id.current_file_type_spinner).selectedItem.toString()
        val content = view.findViewById<EditText>(R.id.current_content_et).text.toString()

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

    private fun deleteData(fileData: FileData) {
        MaterialDialog(requireContext()).show {
            title(R.string.delete_confirm)
            message(text = "即将删除 '${fileData.name}' ！")
            positiveButton(R.string.cancel)
            negativeButton(R.string.confirm) {
                viewModel.deleteData(fileData)
                MaterialDialog(requireContext()).show {
                    title(R.string.delete_success)
                    positiveButton(R.string.ok)
                }
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
        }
    }

}