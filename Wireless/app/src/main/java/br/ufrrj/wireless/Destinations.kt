package br.ufrrj.wireless

interface Destination {
    val route: String
}

object WifiListDestination : Destination {
    override val route = "wifi-list"
}

object WelcomeDestination : Destination {
    override val route = "welcome"
}

val screens = listOf(WifiListDestination, WelcomeDestination)