package com.example.goblinpetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.goblinpetcompose.ui.theme.GoblinPetComposeTheme

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("stats") {
            StatsScreen(navController)
        }

        composable(
            route = "selection/{petName}",
            arguments = listOf(
                navArgument("petName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName") ?: ""
            PetSelectionScreen(navController, petName)
        }

        composable(
            route = "pet/{petName}/{petType}",
            arguments = listOf(
                navArgument("petName") { type = NavType.StringType },
                navArgument("petType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName") ?: ""
            val petType = backStackEntry.arguments?.getString("petType") ?: ""
            PetScreen(navController, petName, petType)
        }
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoblinPetComposeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoblinPetComposeTheme {
        Greeting("Android")
    }
}