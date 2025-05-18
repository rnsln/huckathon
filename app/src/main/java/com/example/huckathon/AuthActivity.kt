package com.example.huckathon
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class AuthActivity : AppCompatActivity() {
    companion object {
        var name: String? = null
        var surname: String? = null
        var dob: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth) // bu layout'u az sonra ekleyeceksin
    }

    fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("name", name)
            putExtra("surname", surname)
            putExtra("dob", dob)
        }
        startActivity(intent)
        finish()

    }
}
