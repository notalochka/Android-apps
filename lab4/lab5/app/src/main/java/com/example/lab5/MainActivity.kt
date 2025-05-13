package com.example.lab5

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var magneticSensor: Sensor
    private lateinit var compassImage: ImageView
    private lateinit var degreesText: TextView
    private lateinit var directionText: TextView

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compassImage = findViewById(R.id.compass_image)
        degreesText = findViewById(R.id.degrees_text)
        directionText = findViewById(R.id.direction_text)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

        if (magneticSensor == null) {
            degreesText.text = "Magnetic sensor not available"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            magneticSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> System.arraycopy(
                event.values, 0,
                accelerometerReading, 0, accelerometerReading.size
            )
            Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(
                event.values, 0,
                magnetometerReading, 0, magnetometerReading.size
            )
        }

        updateOrientation()
    }

    private fun updateOrientation() {
        SensorManager.getRotationMatrix(
            rotationMatrix, null,
            accelerometerReading, magnetometerReading
        )

        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        val azimuthInRadians = orientationAngles[0]
        val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

        compassImage.rotation = -azimuthInDegrees


        val roundedDegrees = azimuthInDegrees.roundToInt()
        degreesText.text = "$roundedDegrees°"

        directionText.text = when {
            roundedDegrees >= -22 && roundedDegrees <= 22 -> "N"
            roundedDegrees >= 23 && roundedDegrees <= 67 -> "NE"
            roundedDegrees >= 68 && roundedDegrees <= 112 -> "E"
            roundedDegrees >= 113 && roundedDegrees <= 157 -> "SE"
            roundedDegrees >= 158 || roundedDegrees <= -158 -> "S"
            roundedDegrees <= -113 && roundedDegrees >= -157 -> "SW"
            roundedDegrees <= -68 && roundedDegrees >= -112 -> "W"
            roundedDegrees <= -23 && roundedDegrees >= -67 -> "NW"
            else -> ""
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
}