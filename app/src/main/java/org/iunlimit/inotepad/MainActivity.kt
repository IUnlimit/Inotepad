package org.iunlimit.inotepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!.findNavController())

    }

    override fun onNavigateUp(): Boolean {
        Log.d("Inotepad", "onNavigateUp")
//        val navController = findNavController(R.id.nav_host_fragment)
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!.findNavController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}