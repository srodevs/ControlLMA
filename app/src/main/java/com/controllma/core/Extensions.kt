package com.controllma.core

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore

val Context.xDataStorageUser by preferencesDataStore(name = "USER_PREFS_NAME")

fun Context.showToast(msg: String, long: Boolean = false) {
    Toast.makeText(
        this,
        msg,
        if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}