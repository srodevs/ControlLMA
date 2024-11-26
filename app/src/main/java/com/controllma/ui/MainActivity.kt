package com.controllma.ui

import android.os.Bundle
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
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.controllma.core.StorageUser
import com.controllma.ui.core.theme.ControlLMATheme
import com.controllma.ui.main.MainLoginView
import com.controllma.ui.main.MainHomeView
import com.controllma.ui.main.MainProfileView
import com.controllma.ui.navigation.NavRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val loginViewModel: MainViewModel by viewModels()
    private val userInf: StorageUser = StorageUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlLMATheme {
                val navigationControl = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute =
                            navigationControl.currentBackStackEntryAsState().value?.destination?.route
                        if (currentRoute == NavRoute.NavMainHome.route
                            || currentRoute == NavRoute.NavMainProfile.route
                        ) {
                            BottomBar(navigationControl)
                        }
                    }) { innerPadding ->

                    val startDestination by produceState(initialValue = NavRoute.NavLogin.route) {
                        userInf.getLogin().collect { isLoggedIn ->
                            value = if (isLoggedIn) {
                                NavRoute.NavMainHome.route
                            } else {
                                NavRoute.NavLogin.route
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
                            composable(NavRoute.NavLogin.route) {
                                MainLoginView(
                                    modifier = Modifier.padding(innerPadding),
                                    loginViewModel = loginViewModel,
                                    navigationControl = navigationControl,
                                    userStorageInf = userInf
                                )
                            }
                            composable(NavRoute.NavMainHome.route) {
                                MainHomeView(
                                    navigationControl = navigationControl,
                                    viewModel = loginViewModel,
                                    storageUser = userInf
                                )
                            }
                            composable(NavRoute.NavMainProfile.route) {
                                MainProfileView(
                                    navigationControl = navigationControl,
                                    viewModel = loginViewModel,
                                    storageUser = userInf,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(navigationControl: NavHostController) {
    var index by remember { mutableIntStateOf(0) }
    NavigationBar(modifier = Modifier.background(Color.Blue)) {
        NavigationBarItem(
            selected = index == 0,
            onClick = {
                index = 0
                navigationControl.navigate(NavRoute.NavMainHome.route)
            },
            icon = { Icon(imageVector = Icons.Rounded.Home, contentDescription = "") },
            label = { Text(text = "Principal") }
        )
        NavigationBarItem(
            selected = index == 1,
            onClick = {
                index = 1
                navigationControl.navigate(NavRoute.NavMainProfile.route)
            },
            icon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = "") },
            label = { Text(text = "Perfil") }
        )
    }
}