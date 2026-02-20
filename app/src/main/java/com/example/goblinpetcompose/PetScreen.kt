package com.example.goblinpetcompose

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.abs
import kotlin.math.sqrt

@Composable
fun PetScreen(
    navController: NavController,
    petName: String,
    petType: String
) {

    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val viewModel = remember { PetViewModel(database.unsafeSessionDao()) }

    val mood by viewModel.mood.collectAsState()

    val currentImage = when (mood) {
        PetMood.NEUTRAL -> getNeutralImage(petType)
        PetMood.CONCERNED -> getConcernedImage(petType)
        PetMood.ANGRY -> getAngryImage(petType)
    }

    // -------- Walking Detection --------

    val sensorManager = remember {
        context.getSystemService(SensorManager::class.java)
    }

    val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var lastMagnitude by remember { mutableStateOf(0f) }
    var peakCount by remember { mutableStateOf(0) }
    var lastPeakTime by remember { mutableStateOf(0L) }
    var lastMovementTime by remember { mutableStateOf(0L) }

    val threshold = 2.5f
    val requiredPeaks = 6
    val windowTime = 3000L
    val stopTimeout = 4000L

    DisposableEffect(Unit) {

        val listener = object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val magnitude = sqrt(x * x + y * y + z * z)
                val delta = abs(magnitude - lastMagnitude)
                lastMagnitude = magnitude

                val currentTime = System.currentTimeMillis()

                if (delta > threshold) {
                    if (currentTime - lastPeakTime > 250) {
                        peakCount++
                        lastPeakTime = currentTime
                    }
                    lastMovementTime = currentTime
                }

                if (peakCount >= requiredPeaks &&
                    currentTime - lastPeakTime < windowTime
                ) {
                    viewModel.startUnsafeSession()
                }

                if (currentTime - lastMovementTime > stopTimeout) {
                    viewModel.stopUnsafeSession()
                    peakCount = 0
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager?.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // -------- UI --------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedContent(
                    targetState = currentImage,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) { image ->
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = petType,
                        modifier = Modifier.size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = petName,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pet Type: $petType",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (mood == PetMood.CONCERNED) {
                    Text(
                        text = "⚠ Please stop using your phone while walking.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "You're using your phone responsibly.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("stats") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Stats")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}

// -------- Image Helpers --------

fun getNeutralImage(petType: String): Int =
    when (petType.lowercase()) {
        "cat" -> R.drawable.cat
        "rabbit" -> R.drawable.rabbit
        "fox" -> R.drawable.fox
        "bear" -> R.drawable.bear
        else -> R.drawable.cat
    }

fun getConcernedImage(petType: String): Int =
    when (petType.lowercase()) {
        "cat" -> R.drawable.cats
        "rabbit" -> R.drawable.rabbits
        "fox" -> R.drawable.foxs
        "bear" -> R.drawable.bears
        else -> R.drawable.cat
    }

fun getAngryImage(petType: String): Int =
    when (petType.lowercase()) {
        "cat" -> R.drawable.cata
        "rabbit" -> R.drawable.rabbita
        "fox" -> R.drawable.foxa
        "bear" -> R.drawable.beara
        else -> R.drawable.cat
    }