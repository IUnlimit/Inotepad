package org.iunlimit.inotepad.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.github.clans.fab.FloatingActionButton
import org.iunlimit.inotepad.R

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.menu_create).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // List click listener
        view.findViewById<ConstraintLayout>(R.id.list_layout).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        return view
    }

}