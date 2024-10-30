package com.controllma.ui.navigation

sealed class NavRoute(val route: String) {
    data object DataNavLogin : NavRoute("screenLogin")
    data object DataNavMain : NavRoute("screenMain")
}