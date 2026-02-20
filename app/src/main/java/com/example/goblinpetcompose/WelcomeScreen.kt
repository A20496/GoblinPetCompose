package com.example.goblinpetcompose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController) {

    var petName by remember { mutableStateOf("") }

    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center
//        horizontalAlignment = Alignment.CenterHorizontally

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Goblin Pet",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = petName,
            onValueChange = { petName = it },
            label = { Text("Enter your pet's name") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (petName.isNotBlank()) {
                    navController.navigate("selection/$petName")
                }
            }
        ) {
            Text("Select your pet")
        }
    }
}
