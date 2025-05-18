package com.example.huckathon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.huckathon.ui.HelloWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

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

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<HelloWorker>().build()
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

        val workRequest = PeriodicWorkRequestBuilder<HelloWorker>(15, TimeUnit.SECONDS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "hello_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}



