package br.ufrrj.wireless.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.wifi.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NetworkWifi
import androidx.compose.material.icons.rounded.SignalWifi0Bar
import androidx.compose.material.icons.rounded.SignalWifi4Bar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import br.ufrrj.wireless.WifiDetailsDestination
import br.ufrrj.wireless.WifiManager
import br.ufrrj.wireless.saveList
import br.ufrrj.wireless.ui.theme.MainTheme

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val result = ScanResult()
    result.SSID = "salve"
    result.frequency = 2400
    val list = listOf(result, result, result, result, result, result, result, result)
    MainTheme {
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
                WifiList(list)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun WifiListScreen(navController: NavHostController) {
    val perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    )

    val mutableIsScanning = mutableStateOf(false)

    val mutableStateScanResultList = mutableStateListOf<ScanResult>()
    val list = remember { mutableStateScanResultList }

    val scanner = WifiManager(LocalContext.current, mutableStateScanResultList, mutableIsScanning)

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
            WifiList(list)
            Buttons(perms, scanner)
            Button(onClick = {
                navController.navigate(WifiDetailsDestination.route)
            }) {
                Text(text = "Detalhes")
            }
        }
    }
}

@Composable
fun Buttons(
    perms: Array<String>,
    scanner: WifiManager,
) {
    val activity = LocalContext.current as Activity
    ScanButton(activity, perms, scanner)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScanButton(
    activity: Activity,
    perms: Array<String>,
    scanner: WifiManager
) {
    val isScanning by scanner.isScanning
    val wifiList = scanner.list
    val context = LocalContext.current
    Row(modifier = Modifier.padding(10.dp, 0.dp)) {
        Button(
            onClick = {
                ActivityCompat.requestPermissions(activity, perms, 42)
                if (!isScanning) scanner.startScan()
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(2.dp, 0.dp)
                .alpha(if (isScanning) 0.5f else 1f)
        ) {
            Text(text = "Scan")
        }
        Button(
            onClick = {
                if (!isScanning) saveList(context, wifiList)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(2.dp, 0.dp)
                .alpha(if (isScanning) 0.5f else 1f),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ColumnScope.WifiList(list: List<ScanResult>) {
    val padding = PaddingValues(10.dp, 0.dp)
    Text(
        text = "Redes disponíveis",
        modifier = Modifier
            .align(Alignment.Start)
            .padding(padding)
    )
    LazyColumn(Modifier.weight(1f)) {
        itemsIndexed(list) { index, item ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                WifiCard(index, list.size, item.level, item.SSID)
            }
        }
    }
}

@Composable
fun WifiCard(index: Int, count: Int, level: Int, SSID: String) {
    val isFirst = index == 0
    val isLast = (count - 1) == index
    val round = 10.dp
    val shape: RoundedCornerShape
    if (count == 1) {
        shape = RoundedCornerShape(round)
    } else if (isFirst) {
        shape = RoundedCornerShape(round, round)
    } else if (isLast) {
        shape = RoundedCornerShape(0.dp, 0.dp, round, round)
    } else {
        shape = RoundedCornerShape(0.dp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.LightGray)
            .padding(5.dp, 5.dp)
    ) {
        GetWifiIcon(level)
//        Text(text = String.format("Força: %s", getWifiStrength(level)), modifier = Modifier.padding(10.dp, 0.dp))
        Text(text = String.format("Nome: %s", SSID), modifier = Modifier.padding(10.dp, 0.dp))
    }
}

@Composable
fun GetWifiIcon(level: Int) {
    val icon: ImageVector
    if (level >= -30) {
        icon = Icons.Rounded.SignalWifi4Bar // 5
    } else if (level >= -67) {
        icon = Icons.Rounded.NetworkWifi // 4
    } else if (level >= -70) {
        icon = Icons.Rounded.NetworkWifi // 3
    } else if (level >= -80) {
        icon = Icons.Rounded.NetworkWifi // 2
    } else if (level >= -90) {
        icon = Icons.Rounded.NetworkWifi // 1
    } else {
        icon = Icons.Rounded.SignalWifi0Bar // 0
    }
    Icon(
        icon,
        contentDescription = icon.name
    )
}

fun getWifiStrength(level: Int): String {
    when {
        level >= -30 -> {
            return "5"
        }
        level >= -67 -> {
            return "4"
        }
        level >= -70 -> {
            return "3"
        }
        level >= -80 -> {
            return "2"
        }
        level >= -90 -> {
            return "1"
        }
        else -> {
            return "0";
        }
    }
}