package com.controllma.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.controllma.R
import com.controllma.core.StorageUser
import com.controllma.data.model.TypeLoginResponse
import com.controllma.ui.navigation.NavRoute
import kotlinx.coroutines.launch

@Composable
fun MainLoginView(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    navigationControl: NavHostController,
    userInf: StorageUser
) {


    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val email by loginViewModel.email.collectAsState()
    val pass by loginViewModel.pass.collectAsState()
    val btnEnabled by loginViewModel.btnEnable.collectAsState()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        val (img, fieldEmail, fieldPass, fieldButton, pg) = createRefs()
        val centerGuide = createGuidelineFromTop(0.44f)
        val v1Guide = createGuidelineFromStart(0.1f)
        val v2Guide = createGuidelineFromEnd(0.1f)

        if (loginViewModel.loading.value == true) {
            Box(modifier = Modifier.constrainAs(pg) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                CircularProgressIndicator()
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.logo_lma_transparent),
                contentDescription = "",
                modifier = Modifier
                    .size(240.dp)
                    .constrainAs(img) {
                        bottom.linkTo(centerGuide)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
            )
            TextField(
                value = email,
                onValueChange = { loginViewModel.onLoginChange(it, pass) },
                label = { Text(text = stringResource(id = R.string.login_email)) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 80.dp)
                    .constrainAs(fieldEmail) {
                        top.linkTo(centerGuide)
                        start.linkTo(v1Guide)
                        end.linkTo(v2Guide)
                    }
            )
            TextField(
                value = pass,
                onValueChange = { loginViewModel.onLoginChange(email, it) },
                label = { Text(text = stringResource(id = R.string.login_pass)) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(fieldPass) {
                        top.linkTo(fieldEmail.bottom)
                        end.linkTo(fieldEmail.end)
                        start.linkTo(fieldEmail.start)
                    }
            )
            Button(
                onClick = {
                    loginViewModel.onLoginSelected {
                        when (it.loginStatus) {
                            TypeLoginResponse.Success -> {
                                Log.d("main", "debe de cambiar de pantalla")
                                scope.launch {
                                    userInf.saveLoginBool(true)
                                }
                                navigationControl.navigate(NavRoute.DataNavMain.route)
                            }

                            TypeLoginResponse.Incorrect, TypeLoginResponse.Fail -> {
                                Log.d("main", "a salido un error ")
                                scope.launch {
                                    snackBarHostState.showSnackbar("Error: ${it.loginStatus}")
                                }
                            }
                        }
                    }
                },
                enabled = btnEnabled,
                modifier = Modifier
                    .padding(top = 48.dp)
                    .constrainAs(fieldButton) {
                        top.linkTo(fieldPass.bottom)
                        end.linkTo(v2Guide)
                        start.linkTo(v1Guide)
                    }
            ) {
                Text(text = stringResource(id = R.string.login_btn_login))
            }
        }
    }
}