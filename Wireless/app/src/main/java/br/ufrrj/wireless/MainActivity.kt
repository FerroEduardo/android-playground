package br.ufrrj.wireless

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.wifi.ScanResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import br.ufrrj.wireless.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Container()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainTheme {
        MainTheme {
            Container()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Container() {
    val perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    )
    val activity = LocalContext.current as Activity

    val mutableStateScanResultList = mutableStateListOf<ScanResult>()
    val list = remember { mutableStateScanResultList }

    val scanner = WifiScanReceiver(LocalContext.current, mutableStateScanResultList)


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
            Button(
                onClick = {
                    ActivityCompat.requestPermissions(activity, perms, 42)
                    scanner.startScan()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp)
            ) {
                Text(text = "Scan")
            }
        }
    }
}

@Composable
fun ColumnScope.WifiList(list: List<ScanResult>) {
    LazyColumn(Modifier.weight(1f)) {
        items(list) { item ->
            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp, 5.dp)
            ) {
                Text(text = String.format("Nome: %s", item.SSID))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = String.format("Frequência: %s", item.frequency.toString()))
                    Text(text = String.format("Força: %s", getWifiStrength(item.level)))
                }
                Divider()
            }
        }
    }
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