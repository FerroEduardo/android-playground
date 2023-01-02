package br.ufrrj.wireless

interface Destination {
    val route: String
}

object WifiListDestination : Destination {
    override val route = "wifi-list"
}

val screens = listOf(WifiListDestination)