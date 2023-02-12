package br.ufrrj.wireless.views

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import br.ufrrj.wireless.R
import br.ufrrj.wireless.WifiListDestination
import br.ufrrj.wireless.ui.theme.Background
import br.ufrrj.wireless.ui.theme.Green600
import br.ufrrj.wireless.ui.theme.Red
import br.ufrrj.wireless.ui.theme.Red600
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WelcomeScreen(
    navController: NavHostController,
    isLocationEnabled: MutableState<Boolean>
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = requiredPerms.toList())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            PermissionsStateList(permissionsState, isLocationEnabled)
            Buttons(permissionsState, navController, isLocationEnabled)
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun ColumnScope.PermissionsStateList(
    permissionsState: MultiplePermissionsState,
    isLocationEnabled: MutableState<Boolean>
) {
    val iconModifier = Modifier.size(36.dp)
    Column(
        modifier = Modifier.weight(15f),
        verticalArrangement = Arrangement.Center
    ) {
        when (isLocationEnabled.value) {
            true -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.check_circle_fill),
                        contentDescription = null,
                        modifier = iconModifier,
                        tint = Green600
                    )
                    Text(
                        text = "GPS Ativado",
                        color = Color.White
                    )
                }
            }
            false -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel_fill),
                        contentDescription = null,
                        modifier = iconModifier,
                        tint = Red600
                    )
                    Text(
                        text = "GPS desativado",
                        color = Color.White
                    )
                }
            }
        }
        permissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        perm.status.isGranted -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.check_circle_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Green600
                                )
                                Text(
                                    text = "Permissão de localicação precisa aceita",
                                    color = Color.White
                                )
                            }
                        }
                        perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de localicação precisa é necessária",
                                    color = Color.White
                                )
                            }
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de localicação precisa foi rejeitada permanentemente",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    when {
                        perm.status.isGranted -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.check_circle_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Green600
                                )
                                Text(
                                    text = "Permissão de localização aproximada aceita",
                                    color = Color.White
                                )
                            }
                        }
                        perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de localização aproximada é necessária",
                                    color = Color.White
                                )
                            }
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de localização aproximada foi rejeitada permanentemente",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Manifest.permission.CHANGE_WIFI_STATE -> {
                    when {
                        perm.status.isGranted -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.check_circle_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Green600
                                )
                                Text(
                                    text = "Permissão de mudança de Wi-Fi aceita",
                                    color = Color.White
                                )
                            }
                        }
                        perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de mudança de Wi-Fi é necessária",
                                    color = Color.White
                                )
                            }
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão de mudança de Wi-Fi foi rejeitada permanentemente",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Manifest.permission.ACCESS_WIFI_STATE -> {
                    when {
                        perm.status.isGranted -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.check_circle_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Green600
                                )
                                Text(
                                    text = "Permissão para acessar o estado do Wi-Fi aceita",
                                    color = Color.White
                                )
                            }
                        }
                        perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão para acessar o estado do Wi-Fi é necessária",
                                    color = Color.White
                                )
                            }
                        }
                        !perm.status.isGranted && !perm.status.shouldShowRationale -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_fill),
                                    contentDescription = null,
                                    modifier = iconModifier,
                                    tint = Red600
                                )
                                Text(
                                    text = "Permissão para acessar o estado do Wi-Fi foi rejeitada permanentemente",
                                    color = Color.White
                                )
                            }
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
    navController: NavHostController,
    isLocationEnabled: MutableState<Boolean>
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
            .alpha(if (permissionsState.allPermissionsGranted) 0.6f else 1f),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (!permissionsState.allPermissionsGranted) Green600 else Red)
    ) {
        Text(text = "Request Permissions", color = Color(0xfffafafa))
    }
    Button(
        onClick = {
            if (permissionsState.allPermissionsGranted && isLocationEnabled.value) {
                navController.navigate(WifiListDestination.route) {
                    popUpTo(0)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(2.dp, 2.dp)
            .alpha(if (!permissionsState.allPermissionsGranted || !isLocationEnabled.value) 0.6f else 1f),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (permissionsState.allPermissionsGranted && isLocationEnabled.value) Green600 else Red)
    ) {
        Text(text = "Continue", color = Color(0xfffafafa))
    }
}
