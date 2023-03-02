package org.iunlimit.inotepad.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.clans.fab.FloatingActionButton
import org.iunlimit.inotepad.R

class UpdateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.update_menu_save).setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.update_menu_delete).setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        return view
    }

}