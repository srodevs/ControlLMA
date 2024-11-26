package com.controllma.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.controllma.R
import com.controllma.core.StorageUser
import com.controllma.ui.MainViewModel
import com.controllma.ui.core.theme.Purple20
import com.controllma.ui.navigation.NavRoute
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileView(
    navigationControl: NavHostController,
    viewModel: MainViewModel,
    storageUser: StorageUser,
) {
    val myImgUrl by produceState(initialValue = "https://images.pexels.com/photos/29095597/pexels-photo-29095597/free-photo-of-misteriosa-figura-encapuchada-en-el-humo.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1") {
        storageUser.getUserImage().collect { miUrl ->
            value = miUrl
        }
    }
    val myName by produceState(initialValue = "") {
        storageUser.getUsername().collect { miName ->
            value = miName
        }
    }
    val myEmail by produceState(initialValue = "") {
        storageUser.getUserEmail().collect { email ->
            value = email
        }
    }
    val myUuid by produceState(initialValue = "") {
        storageUser.getUserUuid().collect { uuid ->
            value = uuid
        }
    }
    val mPassword by viewModel.profilePassword.collectAsState()
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var showPass by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Log.e("main", "timestamp->" + getTimeZoneFromTimestamp(System.currentTimeMillis()))
    Log.e("main", "calendar->" + getTimeZoneFromTimestampWithCalendar(System.currentTimeMillis()))
    Log.e("main", "utc" + getTimeZoneInfo())

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        val (ivIconMenu, ivUser, ivFinger, ivPassword, tvUsername, topAccess, rvContainer, boxContainerAssistant, menuContainer) = createRefs()

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
                    start.linkTo(parent.start)
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.constrainAs(menuContainer) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        ) {
            DropdownMenuItem(
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
            text = "Hola $myName",
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
            .constrainAs(boxContainerAssistant) {
                top.linkTo(tvUsername.bottom)
            }) {
            Column {
                Text(
                    stringResource(R.string.profile_register_with),
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
                            .clickable {
                                showPass = true
                            }
                            .padding(12.dp)
                            .size(40.dp)
                            .weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_round_fingerprint),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                showFingerPrint(context, viewModel, myUuid)
                            }
                            .padding(12.dp)
                            .size(40.dp)
                            .weight(1f)
                    )
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.constrainAs(rvContainer) {
                top.linkTo(boxContainerAssistant.bottom)
                bottom.linkTo(parent.bottom)
            }) {
//            items() {
//                ItemNew(it) { new ->
//                    Toast.makeText(context, "${new.newId}", Toast.LENGTH_LONG).show()
//                    Log.e("rv", "$new")
//                }
//            }
        }
        viewModel.getAllRollCall(myUuid)

        if (showPass) {
            ModalBottomSheet(
                onDismissRequest = { showPass = false },
                modifier = Modifier.fillMaxWidth(),
                sheetMaxWidth = 400.dp,
            ) {
                Text(
                    stringResource(R.string.profile_finger_title),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    stringResource(R.string.profile_pass_description),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                TextField(
                    value = mPassword,
                    onValueChange = {
                        viewModel.onProfileChangePass(pass = it)
                    },
                    label = { Text(text = stringResource(id = R.string.profile_pass_put_pass_hint)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    keyboardActions = KeyboardActions(onDone = {}),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
                Button(
                    onClick = {
                        if (viewModel.profilePassword.value.isNotEmpty()) {
                            viewModel.loginFromProfile(myEmail) {
                                if (it) {
                                    viewModel.createRollCall(myUuid) { rollCall ->
                                        if (rollCall) {
                                            viewModel.onProfileChangePass(pass = "")
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.profile_rollcall_success),
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                            showPass = false
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Ingresa tu contraseña", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    colors = ButtonColors(
                        containerColor = Purple20,
                        contentColor = Color.White,
                        disabledContentColor = Color.DarkGray,
                        disabledContainerColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp, start = 12.dp, end = 12.dp)
                ) {
                    Text(stringResource(R.string.profile_pass_btn_send))
                }
            }
        }
    }
}

@SuppressLint("NewApi")
fun showFingerPrint(
    context: Context,
    viewModel: MainViewModel,
    uuId: String
) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        (context as FragmentActivity),
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    context, "Authentication error: $errString", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    context, "Authentication succeeded!", Toast.LENGTH_SHORT
                ).show()
                viewModel.createRollCall(uuId) { rollCall ->
                    if (rollCall) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.profile_rollcall_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context, "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getText(R.string.profile_finger_title))
        .setSubtitle(context.getText(R.string.profile_finger_description))
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val biometricManager = androidx.biometric.BiometricManager.from(context)
    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS ->
            Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            Log.e("MY_APP_TAG", "No biometric features available on this device.")

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // Prompts the user to create credentials that your app accepts.
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }
            //startActivityForResult(enrollIntent, REQUEST_CODE)
        }
    }
    biometricPrompt.authenticate(promptInfo)
}


fun getTimeZoneFromTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val zoneId = ZoneId.systemDefault()  // Zona horaria del sistema
    val zonedDateTime = instant.atZone(zoneId)
    return "Zona horaria: ${zoneId.id}, Hora: ${zonedDateTime.hour}:${zonedDateTime.minute}"
}
//val timestamp = System.currentTimeMillis()
//println(getTimeZoneFromTimestamp(timestamp))

fun getTimeZoneFromTimestampWithCalendar(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    val timeZone = calendar.timeZone
    return "Zona horaria: ${timeZone.id}, Hora: ${calendar.get(Calendar.HOUR_OF_DAY)}:${
        calendar.get(Calendar.MINUTE)
    }"
}
//val timestamp = System.currentTimeMillis()
//println(getTimeZoneFromTimestampWithCalendar(timestamp))

fun getTimeDifferenceBetweenZones(zone1: String, zone2: String): Long {
    val now = ZonedDateTime.now(ZoneId.of(zone1))
    val otherTime = ZonedDateTime.now(ZoneId.of(zone2))
    return ChronoUnit.HOURS.between(now, otherTime)
}
//println("Diferencia horaria entre México y Londres: ${getTimeDifferenceBetweenZones("America/Mexico_City", "Europe/London")} horas")

fun getTimeZoneInfo(): String {
    val timeZone = TimeZone.getDefault()
    val offsetMillis = timeZone.getOffset(System.currentTimeMillis())

    // Convertir el desplazamiento a horas y minutos
    val offsetHours = offsetMillis / (1000 * 60 * 60)
    val offsetMinutes = (offsetMillis / (1000 * 60)) % 60

    // Obtener el nombre de la zona horaria
    val zoneName = timeZone.id

    // Formatear el resultado
    val utcOffset = String.format("UTC %+d:%02d", offsetHours, offsetMinutes)

    return "$utcOffset ($zoneName)"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
private fun ShowExample2() {

}