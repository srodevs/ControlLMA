package com.controllma.ui.main

import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.controllma.R
import com.controllma.core.StorageUser
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
        val newList by viewModel.newsList.collectAsState(emptyList())
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val (fabCreate) = createRefs()

        viewModel.getAllNews()


        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(newList.asReversed()) {
                ItemNew(it) { new ->
                    Toast.makeText(context, "${new.newId}", Toast.LENGTH_LONG).show()
                    Log.e("rv", "$new")
                }
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = {

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


fun getSuperHeroList(): List<SuperHeroModel> {
    return listOf(
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "Marvlel",
            R.drawable.logo_lma_transparent
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "Teletubi",
            R.drawable.logo_lma
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "Teletubi",
            R.drawable.ic_launcher_background
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "Teletubi",
            R.drawable.ic_launcher_foreground
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "Marvlel",
            R.drawable.ic_round_fingerprint
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "DC",
            R.drawable.ic_launcher_background
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "DC",
            R.drawable.ic_round_password
        ),
        SuperHeroModel(
            superHeroName = "wolverine",
            realHeroName = "Logan",
            "DC",
            R.drawable.ic_round_password
        ),
    )
}

data class SuperHeroModel(
    var superHeroName: String,
    var realHeroName: String,
    var publsher: String,
    @DrawableRes var photo: Int,
)
