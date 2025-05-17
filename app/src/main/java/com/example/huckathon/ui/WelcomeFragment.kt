package com.example.huckathon.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huckathon.R

class WelcomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("MainMenuFragment", "Fragment created!")
        Toast.makeText(context, "Main menu started", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }
}
