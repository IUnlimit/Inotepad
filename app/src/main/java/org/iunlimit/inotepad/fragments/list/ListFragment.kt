package org.iunlimit.inotepad.fragments.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.github.clans.fab.FloatingActionButton
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.fragments.SharedViewModel

class ListFragment : Fragment() {

    private val viewModel: FileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.allData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkDatabaseEmpty(data)
            adapter.setData(data)
        }
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner) {
            showEmptyDatabaseView(it)
        }

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.menu_search).setOnClickListener {
            searchData()
        }

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.menu_create).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return view
    }

    private fun showEmptyDatabaseView(emptyDatabase: Boolean) {
        val visibility = if (emptyDatabase) { View.VISIBLE } else { View.INVISIBLE }
        view?.findViewById<ImageView>(R.id.no_data_imageView)?.visibility = visibility
        view?.findViewById<TextView>(R.id.no_data_textView)?.visibility = visibility
    }

    private fun searchData() {
        MaterialDialog(requireContext()).show {
            title(R.string.search_title)
            val dialog = input(hintRes = R.string.search_tip)
            negativeButton(R.string.cancel)
            positiveButton(R.string.confirm) {
                val text = dialog.getInputField().text
                Log.i("jump", "=========================${text}")
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }
    }

}