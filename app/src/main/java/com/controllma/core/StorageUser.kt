package com.controllma.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controllma.ControlLMAApp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageUser {

    companion object {
        val USER_LOGIN = booleanPreferencesKey("userIsLogin")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_NAME = stringPreferencesKey("username")
        private val USER_IMG = stringPreferencesKey("userImage")
        private val USER_FCM_TOKEN = stringPreferencesKey("deviceToken")
    }

    private val context = ControlLMAApp.mAppContext

    suspend fun saveUserInfo(
        isLogin: Boolean,
        loginId: String,
        email: String,
        username: String,
        userImage: String
    ) {
        context.xDataStorageUser.edit {
            it[USER_LOGIN] = isLogin
            it[USER_EMAIL] = email
            it[USER_NAME] = username
            it[USER_IMG] = userImage
        }

    }

    suspend fun saveLoginBool(
        isLogin: Boolean,
    ) {
        context.xDataStorageUser.edit {
            it[USER_LOGIN] = isLogin
        }
    }


    fun getLogin() = context.xDataStorageUser.data.map {
        it[StorageUser.USER_LOGIN] ?: false
    }

}