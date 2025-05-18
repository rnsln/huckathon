package com.example.huckathon.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.huckathon.R
import androidx.lifecycle.ViewModelProvider
import com.db.williamchart.Tooltip
import com.db.williamchart.view.BarChartView
import com.example.huckathon.MainActivity


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val sleepHours = listOf(7f, 6f, 8f, 5f, 7f, 9f, 6.5f) // This should come from API

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stepProgress = view.findViewById<ProgressBar>(R.id.stepProgress)
        val stepText = view.findViewById<TextView>(R.id.stepText)
        val steps = MainActivity.steps

        stepProgress.progress = steps
        stepText.text = "Steps: $steps / 10000"

        val nameText = view.findViewById<TextView>(R.id.textName)
        val surnameText = view.findViewById<TextView>(R.id.textSurname)
        val dobText = view.findViewById<TextView>(R.id.textDOB)

        nameText.text = "Name ${MainActivity.receivedName ?: "N/A"}"
        surnameText.text = "Surname: ${MainActivity.receivedSurname ?: "N/A"}"
        dobText.text = "Date of Birth: ${MainActivity.receivedDob ?: "N/A"}"

        val chart = view.findViewById<BarChartView>(R.id.sleepChart)
        chart.animation.duration = 1000L

        chart.labelsFormatter = { value -> "${value.toInt()}h" }

        chart.animate(
            sleepHours.mapIndexed { i, value ->
                weekLabels[i] to value
            }
        )
    }
}