package com.controllma.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MainView(modifier: Modifier = Modifier, navigationControl: NavHostController) {
    Text(text = "Hola mundo")
}