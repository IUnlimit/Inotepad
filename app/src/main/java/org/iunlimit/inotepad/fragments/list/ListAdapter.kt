package org.iunlimit.inotepad.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.iunlimit.inotepad.R
import org.iunlimit.inotepad.data.models.FileData

class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var dataList = emptyList<FileData>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        itemView.findViewById<TextView>(R.id.name_text).text = dataList[position].name
        itemView.findViewById<TextView>(R.id.content_txt).text = dataList[position].content
        itemView.findViewById<ConstraintLayout>(R.id.row_background).setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            itemView.findNavController().navigate(action)
        }

        val type = dataList[position].type
        itemView.findViewById<CardView>(R.id.type_indicator).setCardBackgroundColor(
            ContextCompat.getColor(itemView.context, type.color)
        )
    }

    fun setData(fileData: List<FileData>) {
        this.dataList = fileData
        notifyDataSetChanged()
    }

}