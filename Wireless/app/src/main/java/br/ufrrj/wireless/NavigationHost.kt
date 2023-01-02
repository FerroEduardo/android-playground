package br.ufrrj.wireless

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.ufrrj.wireless.views.WifiListScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = WifiListDestination.route,
        modifier = modifier
    ) {
        composable(route = WifiListDestination.route) {
            WifiListScreen(navController)
        }
    }
}