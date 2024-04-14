package com.example.alquicar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alquicar.ui.theme.AlquicarTheme
import com.example.alquicar.view.LoginScreen
import com.example.alquicar.view.RegisterScreen
import com.example.alquicar.view.VehicleListScreen
import com.example.alquicar.viewmodels.Splash

class MainActivity : ComponentActivity() {
    private val splash : Splash by viewModels<Splash>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen : SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            splashScreen.setKeepOnScreenCondition{ splash.isSplashScreen }
            MyApp()
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
    AlquicarTheme {
        Greeting("Android")
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController) }
        composable("register") {
            RegisterScreen(navController) }
        composable("vehicleListScreen") {
            VehicleListScreen() }
    }
}