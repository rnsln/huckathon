package com.example.huckathon.ui

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

        // Buton tanımlamaları
        val startMeditation = view.findViewById<Button>(R.id.startMeditation)
        val stopMeditation = view.findViewById<Button>(R.id.stopMeditation)
        val startConcert = view.findViewById<Button>(R.id.startConcert)
        val stopConcert = view.findViewById<Button>(R.id.stopConcert)

        // Tıklama olayları
        startMeditation.setOnClickListener { startActivity("meditation") }
        stopMeditation.setOnClickListener { stopActivity("meditation") }

        startConcert.setOnClickListener { startActivity("concert") }
        stopConcert.setOnClickListener { stopActivity("concert") }

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
            "meditation" -> duration / 1       // Her 5 saniyede +1 enerji
            "concert" -> -duration / 1         // Her 3 saniyede -1 enerji
            else -> 0
        }

        energy = (energy + delta).coerceIn(0, 100)
        MainActivity.globalEnergy = energy
        updateEnergyUI()
    }

    private fun updateEnergyUI() {
        energyBar.progress = energy
        energyText.text = "Energy: $energy"
    }
}
