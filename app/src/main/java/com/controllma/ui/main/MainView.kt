package com.controllma.ui.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.controllma.R
import com.controllma.core.StorageUser
import com.controllma.ui.core.theme.Purple20
import com.controllma.ui.login.MainViewModel

@Composable
fun MainHomeView(
    navigationControl: NavHostController,
    viewModel: MainViewModel,
    storageUser: StorageUser
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val isAdmin by produceState(initialValue = false) {
            storageUser.getUserType().collect { userType ->
                value = userType == "admin"
            }
        }
        val miUuid by produceState(initialValue = "") {
            storageUser.getUserUuid().collect { uuid ->
                value = uuid
            }
        }
        val newList by viewModel.newsList.collectAsState(emptyList())
        val loading by viewModel.loading.collectAsState(false)
        val title by viewModel.newTitle.collectAsState()
        val description by viewModel.newDescr.collectAsState()
        var showDialogCreate by rememberSaveable { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val (fabCreate, pg, rv, toolbar) = createRefs()

        viewModel.getAllNews()

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .constrainAs(toolbar) {
                    top.linkTo(parent.top)
                }) {
            Image(
                painter = painterResource(R.drawable.logo_lma_transparent),
                contentDescription = "",
                modifier = Modifier.size(36.dp)
            )
            Text(
                stringResource(R.string.app_name),
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        LazyColumn(modifier = Modifier.constrainAs(rv) {
            top.linkTo(toolbar.bottom)
        }, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(newList.asReversed()) {
                ItemNew(it) { new ->
                    Toast.makeText(context, "${new.newId}", Toast.LENGTH_LONG).show()
                    Log.e("rv", "$new")
                }
            }
        }

        if (loading) {
            Box(modifier = Modifier.constrainAs(pg) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                CircularProgressIndicator(color = Purple20)
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = Purple20,
                onClick = {
                    showDialogCreate = !showDialogCreate
                },
                modifier = Modifier
                    .padding(12.dp)
                    .constrainAs(fabCreate) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
            }
        }
        if (showDialogCreate) {
            Dialog(onDismissRequest = { showDialogCreate = false }) {
                Card(shape = RoundedCornerShape(8)/*, elevation = 12.dp*/) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        TextField(
                            value = title,
                            onValueChange = {
                                viewModel.onNewsChange(
                                    title = it,
                                    description = description
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.home_new_title)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardActions = KeyboardActions(onDone = {}),
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = description,
                            onValueChange = {
                                viewModel.onNewsChange(
                                    title = title,
                                    description = it
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.home_new_description)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            keyboardActions = KeyboardActions(onDone = {}),
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )

                        Button(
                            colors = ButtonColors(
                                containerColor = Purple20,
                                contentColor = Color.White,
                                disabledContentColor = Color.DarkGray,
                                disabledContainerColor = Color.LightGray
                            ),
                            onClick = {
                                if (viewModel.newTitle.value.isNotEmpty() && viewModel.newDescr.value.isNotEmpty()) {
                                    viewModel.onPublish(miUuid) {
                                        if (it) {
                                            viewModel.onNewsChange(title = "", description = "")
                                            showDialogCreate = false
                                            Toast.makeText(context, "create", Toast.LENGTH_LONG)
                                                .show()
                                        } else {
                                            Toast.makeText(context, "error", Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context, "Rellena ambos campos", Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, bottom = 20.dp)
                        ) {
                            Text(stringResource(R.string.home_new_btn_send))
                        }
                    }
                }
            }

        }
    }
}


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

@Composable
@Preview(showSystemUi = true)
fun ShowExample() {
    Dialog(onDismissRequest = { }) {
        Card(shape = RoundedCornerShape(8)/*, elevation = 12.dp*/) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                TextField(
                    value = "email",
                    onValueChange = { /*loginViewModel.onLoginChange(it, pass)*/ },
                    label = { Text(text = stringResource(id = R.string.home_new_title)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    maxLines = 1,
                    keyboardActions = KeyboardActions(onDone = {}),
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = "email",
                    onValueChange = { /*loginViewModel.onLoginChange(it, pass)*/ },
                    label = { Text(text = stringResource(id = R.string.home_new_description)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    maxLines = 1,
                    keyboardActions = KeyboardActions(onDone = {}),
                    modifier = Modifier
                        .padding(top = 20.dp)
                )

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp)
                ) {
                    Text(stringResource(R.string.home_new_btn_send))
                }
            }
        }
    }

}
