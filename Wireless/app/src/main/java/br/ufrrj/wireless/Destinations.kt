package br.ufrrj.wireless

interface Destination {
    val route: String
}

object WifiListDestination : Destination {
    override val route = "wifi-list"
}

object WifiDetailsDestination : Destination {
    override val route = "wifi-details"
}

val screens = listOf(WifiListDestination, WifiDetailsDestination)