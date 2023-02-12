package br.ufrrj.wireless

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.ufrrj.wireless.views.WelcomeScreen
import br.ufrrj.wireless.views.WifiListScreen
import kotlinx.coroutines.delay

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val locationUtil = LocationUtil(context)
    val isLocationEnabled = remember { mutableStateOf(locationUtil.isLocationEnabled()) }
    val checkLocationLoop = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (checkLocationLoop.value) {
            isLocationEnabled.value = locationUtil.isLocationEnabled()
            delay(1000)
        }
    }

    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    NavHost(
        navController = navController,
        startDestination = WelcomeDestination.route,
        modifier = modifier
    ) {
        composable(route = WifiListDestination.route) {
            WifiListScreen(navController, isLocationEnabled)
            BackHandler(true) {}
        }
        composable(route = WelcomeDestination.route) {
            WelcomeScreen(navController, isLocationEnabled)
            BackHandler(true) {}
        }
    }
}