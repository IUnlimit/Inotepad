package org.iunlimit.inotepad.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.iunlimit.inotepad.data.models.COMPRESS_CLAZZ
import org.iunlimit.inotepad.data.models.FileData
import org.iunlimit.inotepad.data.models.FileType
import org.iunlimit.inotepad.databinding.RowLayoutBinding
import org.iunlimit.inotepad.fragments.list.ListFragment
import org.iunlimit.inotepad.fragments.list.ListFragmentDirections
import org.iunlimit.inotepad.fragments.setFont
import org.iunlimit.inotepad.fragments.preview.driver.Compress

class ListAdapter(
    val listFragment: ListFragment
): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var dataList = emptyList<FileData>()

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        setFont(holder.binding.nameText)
        setFont(holder.binding.contentText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val fileData = dataList[position]
        holder.bind(fileData)

        // Send data and action ListFragment to UpdateFragment
        holder.binding.rowBackground.setOnClickListener {
            // TODO
//            if (fileData.filePath != null) {
//                shareFile(holder.itemView.context, fileData.filePath!!)
//                return@setOnClickListener
//            }

            if (fileData.type.isEditable()) {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(fileData)
                holder.itemView.findNavController().navigate(action)
                return@setOnClickListener
            }

            if (fileData.type == FileType.PDF) {
                val action = ListFragmentDirections.actionListFragmentToPDFFragment(fileData)
                holder.itemView.findNavController().navigate(action)
                return@setOnClickListener
            }

            if (fileData.type.clazz == COMPRESS_CLAZZ) {
                val compress = Compress(fileData)
                compress.selectFile(holder.binding.root.context) {
                    listFragment.importFile(it.absolutePath)
                }

                return@setOnClickListener
            }

            // uneditable type should be directly redirected
            val action = ListFragmentDirections.actionListFragmentToWebViewFragment(fileData)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(fileData: List<FileData>) {
        val listDiffUtil = ListDiffUtil(dataList, fileData)
        val listDiffResult = DiffUtil.calculateDiff(listDiffUtil)
        this.dataList = fileData
        listDiffResult.dispatchUpdatesTo(this)
    }

    class ListViewHolder(val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(fileData: FileData) {
            binding.fileData = fileData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding)
            }
        }

    }

}