package com.controllma.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.controllma.core.StorageUser
import com.controllma.core.xDataStorageUser
import com.controllma.ui.core.theme.ControlLMATheme
import com.controllma.ui.login.LoginViewModel
import com.controllma.ui.login.MainLoginView
import com.controllma.ui.main.MainView
import com.controllma.ui.navigation.NavRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val userInf: StorageUser = StorageUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlLMATheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {

                    }) { innerPadding ->
                    val navigationControl = rememberNavController()

                    val startDestination by produceState(initialValue = NavRoute.DataNavLogin.route) {
                        getLogin().collect { isLoggedIn ->
                            value = if (isLoggedIn) {
                                NavRoute.DataNavMain.route
                            } else {
                                NavRoute.DataNavLogin.route
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NavHost(
                            navController = navigationControl,
                            startDestination = startDestination
                        ) {
                            composable(NavRoute.DataNavMain.route) {
                                MainView(
                                    modifier = Modifier.padding(innerPadding),
                                    navigationControl = navigationControl
                                )
                            }
                            composable(NavRoute.DataNavLogin.route) {
                                MainLoginView(
                                    modifier = Modifier.padding(innerPadding),
                                    loginViewModel = loginViewModel,
                                    navigationControl = navigationControl,
                                    userInf
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLogin() = xDataStorageUser.data.map {
        Log.e("main", it[StorageUser.USER_LOGIN].toString())
        it[StorageUser.USER_LOGIN] ?: false
    }
}

@Composable
fun BottomBar() {
    var index by remember { mutableIntStateOf(0) }
    NavigationBar(modifier = Modifier.background(Color.Blue)) {
        NavigationBarItem(
            selected = index == 0,
            onClick = { index = 0 },
            icon = { Icon(imageVector = Icons.Rounded.Home, contentDescription = "") },
            label = { Text(text = "Principal") }
        )
        NavigationBarItem(
            selected = index == 1,
            onClick = { index = 1 },
            icon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = "") },
            label = { Text(text = "Perfil") }
        )
    }
}