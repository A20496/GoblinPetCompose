package com.example.goblinpetcompose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StatsScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val dao = database.unsafeSessionDao()

    val totalUnsafeTimeNullable by dao.getTotalUnsafeTime()
        .collectAsState(initial = 0L)

    val totalUnsafeTime = totalUnsafeTimeNullable ?: 0L

    val sessionCount by dao.getSessionCount()
        .collectAsState(initial = 0)

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

                Text(
                    text = "Usage Statistics",
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Total Unsafe Time",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = formatDuration(totalUnsafeTime),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Unsafe Sessions",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = sessionCount.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

fun formatDuration(ms: Long): String {
    val seconds = ms / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes}m ${remainingSeconds}s"
}