package com.example.huckathon.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.huckathon.MainActivity
import com.example.huckathon.R
import kotlin.math.sqrt

class MainMenuFragment : Fragment(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelSensor: Sensor? = null
    private lateinit var stepText: TextView
    private lateinit var energyText: TextView
    private lateinit var energyBar: ProgressBar

    private var lastMagnitude = 0.0
    private var threshold = 4.4 // titreşim eşiği

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_mainmenu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        energyBar = view.findViewById(R.id.energyBar)
        energyText = view.findViewById(R.id.energyText)
        stepText = view.findViewById(R.id.stepCounterText)

        val energy = MainActivity.globalEnergy
        energyBar.progress = energy
        energyText.text = "Energy: $energy"

        val playerDot = view.findViewById<ImageView>(R.id.playerDot)
        val concertDot = view.findViewById<ImageView>(R.id.concertDot)
        val meditationDot = view.findViewById<ImageView>(R.id.meditationDot)

        if (MainActivity.globalEnergy < 40) {
            concertDot.visibility = View.VISIBLE
            meditationDot.visibility = View.GONE
            playerDot.visibility = View.VISIBLE
        } else {
            concertDot.visibility = View.GONE
            meditationDot.visibility = View.VISIBLE
        }

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelSensor == null) {
            stepText.text = "Accelerometer bulunamadı"
        }
        stepText.text = "Tahmini adım: ${MainActivity.steps}"
    }

    override fun onResume() {
        super.onResume()
        accelSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = sqrt((x * x + y * y + z * z).toDouble())
            val delta = kotlin.math.abs(magnitude - lastMagnitude)
            lastMagnitude = magnitude

            if (delta > threshold) {
                MainActivity.steps++
                if (MainActivity.steps % 10 == 0) {
                    MainActivity.globalEnergy = (MainActivity.globalEnergy - 1).coerceAtLeast(0)
                }
                stepText.text = "Tahmini adım: ${MainActivity.steps}"
                energyText.text = "Energy: ${MainActivity.globalEnergy}"
                energyBar.progress = MainActivity.globalEnergy
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
