package com.controllma.ui.navigation

sealed class NavRoute(val route: String) {
    data object NavLogin : NavRoute("screenLogin")
    data object NavMainHome : NavRoute("screenMainHome")
    data object NavMainProfile : NavRoute("screenMainProfile")
}