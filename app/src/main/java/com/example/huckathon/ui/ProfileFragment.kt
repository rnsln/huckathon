package com.example.huckathon.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.huckathon.R
import androidx.lifecycle.ViewModelProvider
import com.db.williamchart.Tooltip
import com.db.williamchart.view.BarChartView
import com.example.huckathon.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private var sleepHours = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) // This should come from API
    private fun fetchSleepHours() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.101.12.18:3000/") // update if needed
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ChatBotApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val zones = api.getSleepData()
                withContext(Dispatchers.Main) {
                    sleepHours = zones.map { it.hours }
                }
            } catch (e: Exception) {
                Log.e("API", "Failed to fetch zones: ${e.message}")
            }
        }
    }

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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.101.12.18:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val chart = view.findViewById<BarChartView>(R.id.sleepChart)

        val api = retrofit.create(ChatBotApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getSleepData() // this returns List<SleepData>
                withContext(Dispatchers.Main) {
                    val sleepHours = response.map { it.hours }
                    val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                    chart.animate(
                        weekLabels.zip(sleepHours)
                    )
                }
            } catch (e: Exception) {
                Log.e("API", "Sleep data fetch failed: ${e.message}")
            }
        }
    }
}