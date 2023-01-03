package br.ufrrj.wireless.views

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import br.ufrrj.wireless.WifiListDestination
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WelcomeScreen(navController: NavHostController) {
    val permissionsState = rememberMultiplePermissionsState(permissions = requiredPerms.toList())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            PermissionsStateList(permissionsState)
            Buttons(permissionsState, navController)
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun ColumnScope.PermissionsStateList(permissionsState: MultiplePermissionsState) {
    Column(
        modifier = Modifier.weight(15f),
        verticalArrangement = Arrangement.Center
    ) {
        permissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        perm.status.isGranted -> {
                            Text(text = "Permissão de localicação precisa aceita")
                        }
                        perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de localicação precisa é necessária")
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de localicação precisa foi rejeitada permanentemente")
                        }
                    }
                }
                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    when {
                        perm.status.isGranted -> {
                            Text(text = "Permissão de localização aproximada aceita")
                        }
                        perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de localização aproximada é necessária")
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de localização aproximada foi rejeitada permanentemente")
                        }
                    }
                }
                Manifest.permission.CHANGE_WIFI_STATE -> {
                    when {
                        perm.status.isGranted -> {
                            Text(text = "Permissão de mudança de Wi-Fi aceita")
                        }
                        perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de mudança de Wi-Fi é necessária")
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Text(text = "Permissão de mudança de Wi-Fi foi rejeitada permanentemente")
                        }
                    }
                }
                Manifest.permission.ACCESS_WIFI_STATE -> {
                    when {
                        perm.status.isGranted -> {
                            Text(text = "Permissão para acessar o estado do Wi-Fi aceita")
                        }
                        perm.status.shouldShowRationale -> {
                            Text(text = "Permissão para acessar o estado do Wi-Fi é necessária")
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Text(text = "Permissão para acessar o estado do Wi-Fi foi rejeitada permanentemente")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun ColumnScope.Buttons(
    permissionsState: MultiplePermissionsState,
    navController: NavHostController
) {
    val activity = LocalContext.current as Activity
    Button(
        onClick = {
            if (!permissionsState.allPermissionsGranted) {
                ActivityCompat.requestPermissions(activity, requiredPerms, 42)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(2.dp, 2.dp)
            .alpha(if (permissionsState.allPermissionsGranted) 0.5f else 1f)
    ) {
        Text(text = "Request Permissions")
    }
    Button(
        onClick = {
            if (permissionsState.allPermissionsGranted) {
                navController.navigate(WifiListDestination.route) {
                    popUpTo(0)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(2.dp, 2.dp)
            .alpha(if (!permissionsState.allPermissionsGranted) 0.5f else 1f)
    ) {
        Text(text = "Continuar")
    }
}
