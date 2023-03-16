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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.viewmodel.FileViewModel
import org.iunlimit.inotepad.databinding.FragmentListBinding
import org.iunlimit.inotepad.fragments.SharedViewModel
import org.iunlimit.inotepad.fragments.list.adapter.ListAdapter
import org.iunlimit.inotepad.fragments.update.UpdateFragment

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

        // observe LiveData
        viewModel.allData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkDatabaseEmpty(data)
            adapter.setData(data)
        }

        return binding.root
    }

    // avoid memory release
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // BUG 不响应 notifyItemChanged
//        recyclerView.itemAnimator = SlideInUpAnimator().apply {
//            addDuration = 300
//        }

        // Swipe to delete
        swipeToDelete(recyclerView)
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
            title(R.string.search_title)
            val dialog = input(hintRes = R.string.search_tip)
            negativeButton(R.string.cancel)
            positiveButton(R.string.confirm) {
                val text = dialog.getInputField().text
            Log.d("Inotepad", "=========================${text}")
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }
    }

}