package com.example.huckathon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.huckathon.AuthActivity
import com.example.huckathon.R

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val continueButton = view.findViewById<Button>(R.id.buttonContinue)
        continueButton.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.editTextName).text.toString()
            val surname = view.findViewById<EditText>(R.id.editTextSurname).text.toString()
            val dob = view.findViewById<EditText>(R.id.editTextDOB).text.toString()

            // Bilgileri AuthActivity'deki companion object'e yaz
            AuthActivity.name = name
            AuthActivity.surname = surname
            AuthActivity.dob = dob

            // Quiz ekranına geç
            findNavController().navigate(R.id.action_loginFragment_to_quizFragment)
        }

    }
}
