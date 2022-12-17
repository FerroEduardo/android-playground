package br.ufrrj.wireless.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import br.ufrrj.wireless.WifiListDestination

@Composable
fun WifiDetailsScreen(navController: NavHostController) {
    Column() {
        Text(text = "SSID")
    }
    Button(onClick = {
        navController.navigate(WifiListDestination.route)
    }) {
        Text(text = "Lista")
    }
}