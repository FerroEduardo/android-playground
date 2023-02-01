package br.ufrrj.wireless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.ufrrj.wireless.ui.theme.Background
import br.ufrrj.wireless.ui.theme.MainTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirelessApp()
        }
    }
}

@Composable
fun WirelessApp() {
    MainTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Background
        )
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            screens.find { it.route == currentDestination?.route } ?: WifiListDestination
        NavigationHost(navController = navController)
    }
}