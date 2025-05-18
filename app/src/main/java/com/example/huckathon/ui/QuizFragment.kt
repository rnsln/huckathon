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
        "1. Yeni bir ortama girdiğinde ne yaparsın?" to listOf(
            "Hemen iletişim kurarım" to 1.0,
            "Gözlem yapar, sonra dahil olurum" to -1.0,
            "Duruma göre değişir" to 0.0
        ),
        "2. Boş zamanlarını nasıl geçirirsin?" to listOf(
            "Grup aktiviteleri" to 1.0,
            "Yalnız dinlenerek" to -1.0,
            "Bazen öyle, bazen böyle" to 0.0
        ),
        "3. Düşüncelerini nasıl paylaşırsın?" to listOf(
            "Yüksek sesle, başkalarıyla konuşarak" to 1.0,
            "Kafamda işler" to -1.0,
            "Ortama göre değişir" to 0.0
        ),
        "4. Yeni bir fikir duyduğunda ne ilgini çeker?" to listOf(
            "Arkasındaki potansiyel ve olasılıklar" to 1.0,
            "Gerçekler ve uygulanabilirliği" to -1.0,
            "Ne olduğu fark etmez, yeter ki ilginç olsun" to 0.0
        ),
        "5. Geçmişten bahsederken:" to listOf(
            "Anlam ve bağlantıları hatırlarım" to 1.0,
            "Gerçek detayları ve sırayı hatırlarım" to -1.0,
            "Sahne sahne hatırlıyorum" to 0.0
        ),
        "6. Projeye nasıl başlarsın?" to listOf(
            "Tüm resmi hayal ederim" to 1.0,
            "Küçük adımlarla başlarım" to -1.0,
            "İçgüdüme göre değişir" to 0.0
        ),
        "7. Tartışmada ne önemlidir?" to listOf(
            "Gerçek ve mantık" to 1.0,
            "Duygular ve ilişkiler" to -1.0,
            "Dengeli olmak" to 0.0
        ),
        "8. Zor bir kararda neye bakarsın?" to listOf(
            "Mantıksal neden-sonuçlara" to 1.0,
            "Kalbinin ne söylediğine" to -1.0,
            "İkisine de" to 0.0
        ),
        "9. Geri bildirim verirken:" to listOf(
            "Doğrudan açık sözlü olurum" to 1.0,
            "Hislerini incitmemeye çalışırım" to -1.0,
            "Yumuşak ama net olurum" to 0.0
        ),
        "10. Günlük planlama stilin:" to listOf(
            "Saat saat belirlerim" to 1.0,
            "Akışa bırakırım" to -1.0,
            "İskelet plan yaparım" to 0.0
        ),
        "11. Acil işler geldiğinde:" to listOf(
            "Plan çıkarır, adım adım ilerlerim" to 1.0,
            "Anlık çözümler üretirim" to -1.0,
            "Öncelik sırasına göre karar veririm" to 0.0
        ),
        "12. Tatil için hazırlık:" to listOf(
            "Önceden rezervasyon, plan, rota yaparım" to 1.0,
            "Sadece bilet alır, gerisini doğaçlarım" to -1.0,
            "Esnek plan yaparım" to 0.0
        )
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
                // Calculate grouped scores
                val scoresMap = mutableMapOf(
                    "EI" to 0.0,
                    "NS" to 0.0,
                    "TF" to 0.0,
                    "JP" to 0.0
                )
                scoresMap["EI"] = scores.subList(0, 3).sum()
                scoresMap["NS"] = scores.subList(3, 6).sum()
                scoresMap["TF"] = scores.subList(6, 9).sum()
                scoresMap["JP"] = scores.subList(9, 12).sum()

                Log.d("QuizFinal", "Scores: $scores")
                Log.d("QuizFinal", "Grouped Scores: $scoresMap")
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