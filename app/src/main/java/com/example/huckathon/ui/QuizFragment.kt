package com.example.huckathon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huckathon.AuthActivity
import com.example.huckathon.R

class QuizFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.buttonContinueQuiz).setOnClickListener {
            (activity as? AuthActivity)?.launchMainActivity()
        }
    }
}
