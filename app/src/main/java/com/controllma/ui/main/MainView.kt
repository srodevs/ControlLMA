package com.controllma.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.controllma.R
import com.controllma.core.StorageUser
import com.controllma.ui.login.LoginViewModel
import com.controllma.ui.navigation.NavRoute
import kotlinx.coroutines.launch

@Composable
fun MainHomeView(navigationControl: NavHostController) {
    Column(modifier = Modifier) {
        Text(text = "Hola mundo")
    }
}

@Composable
fun MainProfileView(
    navigationControl: NavHostController,
    viewModel: LoginViewModel,
    storageUser: StorageUser
) {
    val myImgUrl by rememberSaveable {
        mutableStateOf("https://images.pexels.com/photos/29095597/pexels-photo-29095597/free-photo-of-misteriosa-figura-encapuchada-en-el-humo.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val contextForToast = LocalContext.current.applicationContext

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        val (ivIconMenu, ivUser, ivFinger, ivPassword, tvUsername, topAccess, bottomAccess, boxContainer, menuContainer) = createRefs()

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "",
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    expanded = true
                }
                .constrainAs(ivIconMenu) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        DropdownMenu( // (3)
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.constrainAs(menuContainer) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        ) {
            DropdownMenuItem( // (4)
                onClick = {
                    viewModel.logOut {
                        if (it) {
                            scope.launch {
                                storageUser.saveLoginBool(false)
                                navigationControl.navigate(NavRoute.NavLogin.route)
                            }
                        }
                    }
                    expanded = false
                },
                text = {
                    Text(text = "Cerrar Sesion")
                },
                modifier = Modifier.padding(),
                leadingIcon = {},
                trailingIcon = {},
                enabled = true,
                colors = MenuDefaults.itemColors(),
                contentPadding = PaddingValues()
            )
        }


        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(myImgUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.all_description_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(120.dp)
                .background(Color.Black)
                .constrainAs(ivUser) {
                    top.linkTo(ivIconMenu.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
        )

        Text(
            text = "Hola mundo desde perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 20.dp)
                .constrainAs(tvUsername) {
                    top.linkTo(ivUser.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp)
            .constrainAs(boxContainer) {
                top.linkTo(tvUsername.bottom)
            }) {
            Column {
                Text(
                    "Asistencia con",
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_password),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(12.dp)
                            .size(40.dp)
                            .weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_round_fingerprint),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                //showFingerPrint(context)
                            }
                            .padding(12.dp)
                            .size(40.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}


/*
@SuppressLint("VisibleForTests")
fun showFingerPrint(context: Context) {

    val biometricManager = BiometricManager.canAuthenticate(BIOMETRIC_STRONG)
    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            // Authenticate using biometrics
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            // Biometric features hardware is missing
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            // Biometric features are currently unavailable.
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // The user didn't enroll in biometrics that your app accepts, prompt them to enroll in it
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val enrollIntent =
                    Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                        //startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }
    }

}
*/

@Composable
fun TaskMenu(
    expanded: Boolean, // (1)
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val options = listOf( // (2)
        "Cambiar nombre",
        "Enviar por email",
        "Copiar enlace",
        "Ocultar subtareas completas",
        "Eliminar"
    )

    DropdownMenu( // (3)
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        options.forEach { option ->
            DropdownMenuItem( // (4)
                onClick = {
                    onItemClick(option)
                    onDismiss()
                },
                text = {
                    Text(text = option)
                },
                modifier = Modifier.padding(),
                leadingIcon = {},
                trailingIcon = {},
                enabled = true,
                colors = MenuDefaults.itemColors(),
                contentPadding = PaddingValues()
            )
        }
    }
}
