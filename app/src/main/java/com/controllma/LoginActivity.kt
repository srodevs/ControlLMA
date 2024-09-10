package com.controllma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.controllma.ui.theme.ControlLMATheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlLMATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginView(modifier: Modifier) {

}