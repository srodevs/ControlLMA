package com.controllma.ui.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.controllma.R
import com.controllma.core.StorageUser
import com.controllma.data.model.TypeLoginResponse
import com.controllma.ui.MainViewModel
import com.controllma.ui.core.theme.Purple20
import com.controllma.ui.navigation.NavRoute
import kotlinx.coroutines.launch

const val TAG_MAIN_VM = "mainVm"

@Composable
fun MainLoginView(
    modifier: Modifier = Modifier,
    loginViewModel: MainViewModel,
    navigationControl: NavHostController,
    userStorageInf: StorageUser
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val email by loginViewModel.email.collectAsState()
    val pass by loginViewModel.pass.collectAsState()
    val btnEnabled by loginViewModel.btnEnable.collectAsState()
    val loading by loginViewModel.loading.collectAsState(false)

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        val (img, fieldEmail, fieldPass, fieldButton, pg) = createRefs()
        val centerGuide = createGuidelineFromTop(0.44f)
        val v1Guide = createGuidelineFromStart(0.1f)
        val v2Guide = createGuidelineFromEnd(0.1f)

        if (loading) {
            Box(modifier = Modifier.constrainAs(pg) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                CircularProgressIndicator(color = Purple20)
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
                singleLine = true,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = {}),
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
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = {}),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(fieldPass) {
                        top.linkTo(fieldEmail.bottom)
                        end.linkTo(fieldEmail.end)
                        start.linkTo(fieldEmail.start)
                    },
            )
            Button(
                onClick = {
                    loginViewModel.onLoginSelected {
                        when (it.loginStatus) {
                            TypeLoginResponse.Success -> {
                                Log.d(TAG_MAIN_VM, "debe de cambiar de pantalla")
                                loginViewModel.getUserInf { user ->
                                    Log.e(TAG_MAIN_VM, "mi response user es -> $user")
                                    if (user != null) {
                                        scope.launch {
                                            userStorageInf.saveLoginBool(true)
                                            userStorageInf.saveUserInfo(
                                                uuid = user.uuid.toString(),
                                                email = user.email.toString(),
                                                username = user.username.toString(),
                                                userType = user.typeUser.toString(),
                                                userImage = user.userImage.toString(),
                                                tokenFcm = user.deviceToken.toString()
                                            )
                                        }
                                        loginViewModel.onLoginChange(email = "", pass = "")
                                        navigationControl.navigate(NavRoute.NavMainHome.route)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.login_without_info_user),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                            TypeLoginResponse.Incorrect, TypeLoginResponse.Fail -> {
                                Log.d(TAG_MAIN_VM, "a salido un error ")
                                scope.launch {
                                    snackBarHostState.showSnackbar("Error: ${it.loginStatus}")
                                }
                            }
                        }
                    }
                },
                colors = ButtonColors(
                    containerColor = Purple20,
                    contentColor = Color.White,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = Color.LightGray
                ),
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