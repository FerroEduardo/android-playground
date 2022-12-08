package br.ufrrj.wireless

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import br.ufrrj.wireless.ui.theme.MainTheme
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.util.*


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

fun saveList(context: Context, wifiList: SnapshotStateList<ScanResult>) {
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val fileName = "networks.txt"
    val dir = "wireless"
    val newDir = File("$path${File.pathSeparator}$dir")
    try {
        if (!newDir.exists()) {
            newDir.mkdir()
        }
        val file = File(path, fileName)
        val writer = Files.newBufferedWriter(
            file.toPath(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        )
        writer.write("${LocalDateTime.now()}\n")
        if (wifiList.size > 0) {
            wifiList.forEach {
                writer.write("${it.SSID}\n")
            }
        } else {
            writer.write("Lista vazia\n")
        }
        writer.write("--------------------------------\n")
        writer.close()

        Toast.makeText(context, "Salvo em: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
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
                    Text(text = String.format("Frequência: %s Mhz", item.frequency.toString()))
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