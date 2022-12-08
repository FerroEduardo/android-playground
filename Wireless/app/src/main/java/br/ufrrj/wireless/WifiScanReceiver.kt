package br.ufrrj.wireless

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.compose.runtime.snapshots.SnapshotStateList

class WifiScanReceiver(
    private val context: Context,
    private var list: SnapshotStateList<ScanResult>
): BroadcastReceiver() {
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun startScan() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(this, intentFilter)

        wifiManager.startScan()
    }

    override fun onReceive(context: Context, intent: Intent) {
        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        if (success) {
            scanSuccess(wifiManager)
        } else {
            scanFailure(wifiManager)
        }
    }

    private fun scanSuccess(wifiManager: WifiManager) {
        val results = wifiManager.scanResults
        list.clear()
        list.addAll(results.sortedByDescending { it.level })
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