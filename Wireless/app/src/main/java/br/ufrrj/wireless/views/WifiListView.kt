package br.ufrrj.wireless.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.wifi.ScanResult
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import br.ufrrj.wireless.R
import br.ufrrj.wireless.WifiManager
import br.ufrrj.wireless.saveList
import br.ufrrj.wireless.ui.theme.MainTheme

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

val requiredPerms = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.CHANGE_WIFI_STATE,
    Manifest.permission.ACCESS_WIFI_STATE
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun WifiListScreen(navController: NavHostController) {
    val wifiManager = WifiManager(LocalContext.current)

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
            WifiList(WifiManager.scanResultList)
            Buttons(wifiManager)
        }
    }
}

@Composable
fun Buttons(
    wifiManager: WifiManager
) {
    val activity = LocalContext.current as Activity
    val isScanning by WifiManager.isScanning
    val wifiList = WifiManager.scanResultList
    val context = LocalContext.current
    Row(modifier = Modifier.padding(10.dp, 0.dp)) {
        Button(
            onClick = {
                ActivityCompat.requestPermissions(activity, requiredPerms, 42)
                if (!isScanning) wifiManager.startScan()
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
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(padding)
    ) {
        itemsIndexed(list) { index, item ->
            WifiCard(index, list.size, item.level, item.SSID)
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
            .background(MaterialTheme.colors.onBackground.copy(if (isSystemInDarkTheme()) 0.8f else 0.5f))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetWifiIcon(level)
        Text(
            text = String.format("%s", SSID),
            modifier = Modifier.padding(10.dp, 0.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.background,
            fontSize = 18.sp
        )
    }
}

@Composable
fun GetWifiIcon(level: Int) {
    val modifier = Modifier.size(36.dp)
    val id: Int = if (level >= -30) {
        R.drawable.signal_wifi_5_bar
    } else if (level >= -67) {
        R.drawable.signal_wifi_4_bar
    } else if (level >= -70) {
        R.drawable.signal_wifi_3_bar
    } else if (level >= -80) {
        R.drawable.signal_wifi_2_bar
    } else if (level >= -90) {
        R.drawable.signal_wifi_1_bar
    } else {
        R.drawable.signal_wifi_0_bar
    }
    Icon(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = modifier,
        tint = MaterialTheme.colors.background
    )
}
