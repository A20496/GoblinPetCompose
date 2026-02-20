package com.example.goblinpetcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PetSelectionScreen(navController: NavController, petName: String) {

//    val pets = listOf("Cat", "Rabbit", "Fox", "Bear")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp)
//    ) {
//
//        Text("Choose your pet")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        pets.forEach { pet ->
//            Button(
//                onClick = {
//                    navController.navigate("pet/$petName/$pet")
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(pet)
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//    }

    val pets = listOf(
        "Cat" to R.drawable.cat,
        "Rabbit" to R.drawable.rabbit,
        "Fox" to R.drawable.fox,
        "Bear" to R.drawable.bear
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Choose your pet",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        pets.forEach { (petNameType, imageRes) ->

            Card(
                onClick = {
                    navController.navigate("pet/$petName/$petNameType")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = petNameType,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = petNameType,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }





}
