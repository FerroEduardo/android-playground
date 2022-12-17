package br.ufrrj.wireless

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

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