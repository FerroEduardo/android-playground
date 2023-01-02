package br.ufrrj.wireless

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class WifiManager(
    private val context: Context,
): BroadcastReceiver() {
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    companion object {
        var scanResultList: MutableList<ScanResult> = mutableStateListOf()
        val isScanning: MutableState<Boolean> = mutableStateOf(false)
    }

    fun startScan() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(this, intentFilter)

        if (wifiManager.isWifiEnabled) {
            isScanning.value = true
            scanResultList.clear()
            wifiManager.startScan()
        } else {
            turnWifiOn()
        }
    }

    private fun turnWifiOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            context.startActivity(panelIntent)
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        isScanning.value = false
        if (success) {
            scanSuccess(wifiManager)
        } else {
            scanFailure(wifiManager)
        }
    }

    private fun scanSuccess(wifiManager: WifiManager) {
        val results = wifiManager.scanResults
        scanResultList.addAll(results.sortedByDescending { it.level })
    }

    private fun scanFailure(wifiManager: WifiManager) {
        val results = wifiManager.scanResults
//        results.forEach {
//            println(it.SSID)
//        }
//        list = results.map {
//            it.SSID
//        }
    }

}