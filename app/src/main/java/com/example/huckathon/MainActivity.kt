package com.example.huckathon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        var receivedName: String? = null
        var receivedSurname: String? = null
        var receivedDob: String? = null
        var globalEnergy: Int = 50
        var steps: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Intent üzerinden gelen verileri al
        receivedName = intent.getStringExtra("name")
        receivedSurname = intent.getStringExtra("surname")
        receivedDob = intent.getStringExtra("dob")

        // Navigation setup
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }
}



