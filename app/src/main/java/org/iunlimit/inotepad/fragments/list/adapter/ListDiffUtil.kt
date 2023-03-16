package org.iunlimit.inotepad.fragments.list.adapter;

import androidx.recyclerview.widget.DiffUtil
import org.iunlimit.inotepad.data.models.FileData

class ListDiffUtil(
    private var oldList: List<FileData>,
    private var newList: List<FileData>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

}
