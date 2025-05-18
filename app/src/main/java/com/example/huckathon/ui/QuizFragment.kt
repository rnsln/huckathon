package com.example.huckathon.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.huckathon.AuthActivity
import com.example.huckathon.R
class QuizFragment : Fragment() {
    private val questions = listOf(
        "1. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0),
        "2. Boş zamanlarını nasıl geçirirsin?" to listOf("Grup aktiviteleri" to 1.0, "Yalnız dinlenerek" to -1.0, "Bazen öyle, bazen böyle" to 0.0),
        "3. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0),
        "4. Tatil için hazırlık:" to listOf("Plan yaparım" to 1.0, "Doğaçlarım" to -1.0, "Esnek plan yaparım" to 0.0),
        "5. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0),
        "6. Boş zamanlarını nasıl geçirirsin?" to listOf("Grup aktiviteleri" to 1.0, "Yalnız dinlenerek" to -1.0, "Bazen öyle, bazen böyle" to 0.0),
        "7. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0),
        "8. Tatil için hazırlık:" to listOf("Plan yaparım" to 1.0, "Doğaçlarım" to -1.0, "Esnek plan yaparım" to 0.0) ,
        "9. Boş zamanlarını nasıl geçirirsin?" to listOf("Grup aktiviteleri" to 1.0, "Yalnız dinlenerek" to -1.0, "Bazen öyle, bazen böyle" to 0.0),
        "10. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0),
        "11. Tatil için hazırlık:" to listOf("Plan yaparım" to 1.0, "Doğaçlarım" to -1.0, "Esnek plan yaparım" to 0.0),
        "12. Yeni bir ortama girdiğinde ne yaparsın?" to listOf("Hemen iletişim kurarım" to 1.0, "Gözlem yaparım" to -1.0, "Duruma göre değişir" to 0.0)
    )

    private val scores = MutableList(questions.size) { 0.0 }
    private var currentQuestion = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val questionText = view.findViewById<TextView>(R.id.questionText)
        val optionsGroup = view.findViewById<RadioGroup>(R.id.optionsGroup)
        val nextBtn = view.findViewById<Button>(R.id.buttonNext)
        val prevBtn = view.findViewById<Button>(R.id.buttonPrev)

        fun showQuestion(index: Int) {
            questionText.text = questions[index].first
            optionsGroup.removeAllViews()
            optionsGroup.clearCheck()
            val options = questions[index].second

            for ((i, pair) in options.withIndex()) {
                val rb = RadioButton(context)
                rb.text = pair.first
                rb.setTextColor(Color.WHITE)
                rb.tag = pair.second
                optionsGroup.addView(rb)

            }

            prevBtn.visibility = if (index == 0) View.GONE else View.VISIBLE
            nextBtn.text = if (index == questions.lastIndex) "Finish" else "Next"
        }

        optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<RadioButton>(checkedId)
            val score = rb?.tag?.toString()?.toDoubleOrNull() ?: 0.0
            scores[currentQuestion] = score
        }

        nextBtn.setOnClickListener {
            if (currentQuestion < questions.lastIndex) {
                currentQuestion++
                showQuestion(currentQuestion)
            } else {
                Log.d("QuizFinal", "Scores: $scores")
                (activity as? AuthActivity)?.launchMainActivity()
            }
        }

        prevBtn.setOnClickListener {
            if (currentQuestion > 0) {
                currentQuestion--
                showQuestion(currentQuestion)
            }
        }

        showQuestion(currentQuestion)
    }
}
