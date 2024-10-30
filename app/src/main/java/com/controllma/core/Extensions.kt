package com.controllma.core

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.xDataStorageUser by preferencesDataStore(name = "USER_PREFS_NAME")