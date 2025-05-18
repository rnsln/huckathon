package com.example.huckathon.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.huckathon.AuthActivity
import com.example.huckathon.R

class QuizFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.buttonContinueQuiz).setOnClickListener {
            // Helper to get score from RadioGroup
            fun getScore(rgId: Int): Double {
                val rg = view.findViewById<RadioGroup>(rgId)
                val checkedId = rg.checkedRadioButtonId
                if (checkedId == -1) return 0.0
                val rb = rg.findViewById<RadioButton>(checkedId)
                return rb.tag?.toString()?.toDoubleOrNull() ?: 0.0
            }

            // Map questions to categories
            val ei = getScore(R.id.question1) + getScore(R.id.question2) + getScore(R.id.question3)
            val ns = getScore(R.id.question4) + getScore(R.id.question5) + getScore(R.id.question6)
            val tf = getScore(R.id.question7) + getScore(R.id.question8) + getScore(R.id.question9)
            val jp = getScore(R.id.question10) + getScore(R.id.question11) + getScore(R.id.question12)

            // Use or show results as needed
            Log.d("QuizResult", "EI: $ei, NS: $ns, TF: $tf, JP: $jp")

            (activity as? AuthActivity)?.launchMainActivity()
        }
    }
}