package com.example.huckathon.ui

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.huckathon.MainActivity
import com.example.huckathon.R

class ActivityPlannerFragment : Fragment() {
    private lateinit var cardMeditation: LinearLayout
    private lateinit var cardConcert: LinearLayout
    private lateinit var cardYoga: LinearLayout
    private lateinit var cardRunning: LinearLayout
    private var energy = MainActivity.globalEnergy
    private var activityStartTime: Long = 0
    private var currentActivity: String? = null

    private lateinit var energyBar: ProgressBar
    private lateinit var energyText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_activity_planner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        energyBar = view.findViewById(R.id.energyBar)
        energyText = view.findViewById(R.id.energyText)

        // Assign to class-level vars (✔ correct way)
        cardMeditation = view.findViewById(R.id.cardMeditation)
        cardConcert = view.findViewById(R.id.cardConcert)
        cardYoga = view.findViewById(R.id.cardYoga)
        cardRunning = view.findViewById(R.id.cardRunning)

        val startMeditation = view.findViewById<Button>(R.id.startMeditation)
        val stopMeditation = view.findViewById<Button>(R.id.stopMeditation)
        val startConcert = view.findViewById<Button>(R.id.startConcert)
        val stopConcert = view.findViewById<Button>(R.id.stopConcert)
        val startYoga = view.findViewById<Button>(R.id.startYoga)
        val stopYoga = view.findViewById<Button>(R.id.stopYoga)
        val startRunning = view.findViewById<Button>(R.id.startRunning)
        val stopRunning = view.findViewById<Button>(R.id.stopRunning)

        // Button actions
        startMeditation.setOnClickListener { startActivity("meditation") }
        stopMeditation.setOnClickListener { stopActivity("meditation") }

        startConcert.setOnClickListener { startActivity("concert") }
        stopConcert.setOnClickListener { stopActivity("concert") }

        startYoga.setOnClickListener { startActivity("yoga") }
        stopYoga.setOnClickListener { stopActivity("yoga") }

        startRunning.setOnClickListener { startActivity("running") }
        stopRunning.setOnClickListener { stopActivity("running") }

        // Energy-based suggestion
        if (energy < 30) {
            startConcert.isEnabled = false
            startMeditation.isEnabled = true
            startYoga.isEnabled = true
            Toast.makeText(requireContext(), "Enerjin düşük, yoga veya meditasyon önerilir.", Toast.LENGTH_LONG).show()
        } else if (energy > 60) {
            startConcert.isEnabled = true
            startMeditation.isEnabled = false
            startYoga.isEnabled = false
            Toast.makeText(requireContext(), "Enerjin yüksek, konser önerilir!", Toast.LENGTH_LONG).show()
        } else {
            startConcert.isEnabled = true
            startMeditation.isEnabled = true
            startYoga.isEnabled = true
        }

        updateEnergyUI()
    }

    private fun startActivity(activity: String) {
        if (currentActivity != null) {
            Toast.makeText(requireContext(), "Another activity is already active!", Toast.LENGTH_SHORT).show()
            return
        }
        currentActivity = activity
        activityStartTime = SystemClock.elapsedRealtime()
        Toast.makeText(requireContext(), "$activity started", Toast.LENGTH_SHORT).show()
    }

    private fun stopActivity(activity: String) {
        if (currentActivity != activity) {
            Toast.makeText(requireContext(), "This activity wasn't running", Toast.LENGTH_SHORT).show()
            return
        }
        val durationMillis = SystemClock.elapsedRealtime() - activityStartTime
        val durationSec = (durationMillis / 1000).toInt()

        applyEnergyChange(activity, durationSec)
        currentActivity = null
        Toast.makeText(requireContext(), "$activity stopped after $durationSec sec", Toast.LENGTH_SHORT).show()
    }

    private fun applyEnergyChange(activity: String, duration: Int) {
        val delta = when (activity) {
            "meditation" -> duration / 1
            "concert" -> -duration / 1
            "yoga" -> duration / 1
            "running" -> -duration / 2
            else -> 0
        }

        energy = (energy + delta).coerceIn(0, 100)
        MainActivity.globalEnergy = energy
        updateEnergyUI()
    }

    private fun updateEnergyUI() {
        energyBar.progress = energy
        energyText.text = "Energy: $energy"

        val recommendedColor = Color.parseColor("#802196F3")
        val defaultColor = Color.parseColor("#10FFFFFF")

        cardMeditation.setBackgroundColor(defaultColor)
        cardYoga.setBackgroundColor(defaultColor)
        cardConcert.setBackgroundColor(defaultColor)
        cardRunning.setBackgroundColor(defaultColor)

        if (energy < 30) {
            cardMeditation.setBackgroundColor(recommendedColor)
            cardYoga.setBackgroundColor(recommendedColor)
        } else if (energy > 60) {
            cardConcert.setBackgroundColor(recommendedColor)
            cardRunning.setBackgroundColor(recommendedColor)
        }
    }
}
