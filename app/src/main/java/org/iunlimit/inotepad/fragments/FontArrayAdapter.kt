package org.iunlimit.inotepad.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FontArrayAdapter(context: Context, resource: Int, private val array: Array<String>) : ArrayAdapter<String>(context, resource, array) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return doView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return doView(position, convertView, parent)
    }

    private fun doView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)

        view.findViewById<TextView>(android.R.id.text1).let {
            it.text = array[position]
            setFont(it)
        }
        return view
    }

}