package com.example.huckathon.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.huckathon.MainActivity
import com.example.huckathon.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sqrt

class MainMenuFragment : Fragment(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelSensor: Sensor? = null
    private lateinit var stepText: TextView
    private lateinit var energyText: TextView
    private lateinit var energyBar: ProgressBar


    private var lastMagnitude = 0.0
    private var threshold = 4.4 // titreşim eşiği

    private lateinit var playerDot: ImageView
    private lateinit var mapContainer: FrameLayout
    private var posX = 0f
    private var posY = 0f
    private val velocity = 5f // pixels per second
    private var directionAngle = 45f // degrees

    private val handler = Handler(Looper.getMainLooper())
    private val moveRunnable = object : Runnable {
        override fun run() {
            movePlayer()
            handler.postDelayed(this, 100) // move every 1 second
        }
    }

    private fun fetchAndDisplayEnergyZones() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.101.12.18:3000/") // update if needed
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ChatBotApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val zones = api.getEnergyZones()
                withContext(Dispatchers.Main) {
                    zones.forEach { zone ->
                        drawZone(zone)
                    }
                }
            } catch (e: Exception) {
                Log.e("API", "Failed to fetch zones: ${e.message}")
            }
        }
    }

    private fun drawZone(zone: EnergyZone) {
        val context = requireContext()

        // Draw the zone dot
        val dot = ImageView(context).apply {
            setImageResource(android.R.drawable.presence_online)
            layoutParams = FrameLayout.LayoutParams(20, 20)
            translationX = zone.X
            translationY = zone.Y
        }

        // Optional: Draw a translucent circle showing impact
        val radiusView = View(context).apply {
            layoutParams = FrameLayout.LayoutParams((zone.Radius * 2).toInt(), (zone.Radius * 2).toInt())
            translationX = zone.X - zone.Radius
            translationY = zone.Y - zone.Radius
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#33FF4081")) // semi-transparent pink
            }
        }

        mapContainer.addView(radiusView)
        mapContainer.addView(dot)
    }

    private fun movePlayer() {
        // Slight direction change
        directionAngle += (-10..10).random()

        // Move based on direction
        val radians = Math.toRadians(directionAngle.toDouble())
        val dx = (Math.cos(radians) * velocity).toFloat()
        val dy = (Math.sin(radians) * velocity).toFloat()

        posX += dx
        posY += dy

        // Bounce from edges
        val mapWidth = mapContainer.width - playerDot.width
        val mapHeight = mapContainer.height - playerDot.height
        if (posX < 0 || posX > mapWidth) {
            directionAngle = 180 - directionAngle
            posX = posX.coerceIn(0f, mapWidth.toFloat())
        }
        if (posY < 0 || posY > mapHeight) {
            directionAngle = -directionAngle
            posY = posY.coerceIn(0f, mapHeight.toFloat())
        }

        playerDot.translationX = posX
        playerDot.translationY = posY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_mainmenu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stepProgress = view.findViewById<ProgressBar>(R.id.stepProgress)
        val statusText = view.findViewById<TextView>(R.id.statusText)
        energyBar = view.findViewById(R.id.energyBar)
        energyText = view.findViewById(R.id.energyText)
        stepText = view.findViewById(R.id.stepCounterText)
        val steps = MainActivity.steps
        playerDot = view.findViewById(R.id.playerMarker)
        mapContainer = view.findViewById(R.id.mapContainer)
        posX = playerDot.translationX
        posY = playerDot.translationY
        stepProgress.progress = steps

        handler.post(moveRunnable)

        val energy = MainActivity.globalEnergy
        energyBar.progress = energy
        energyText.text = "Energy: $energy"


        val energyStatus = when {
            energy > 60 -> "YÜKSEK ENERJİ!"
            energy < 30 -> "DÜŞÜK ENERJİ!"
            else -> "Normal"
        }
        statusText.text = energyStatus


        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelSensor == null) {
            stepText.text = "Accelerometer bulunamadı"
        }
        stepText.text = "Tahmini adım: ${MainActivity.steps}"

        fetchAndDisplayEnergyZones()

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
