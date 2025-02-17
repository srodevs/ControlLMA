package com.controllma.core

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.controllma.ControlLMAApp
import kotlinx.coroutines.flow.map

class StorageUser {

    companion object {
        val USER_LOGIN = booleanPreferencesKey("userIsLogin")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_UUID = stringPreferencesKey("uuid")
        private val USER_TYPE = stringPreferencesKey("typeUser")
        private val USER_NAME = stringPreferencesKey("username")
        private val USER_IMG = stringPreferencesKey("userImage")
        private val USER_FCM_TOKEN = stringPreferencesKey("deviceToken")
    }

    private val context = ControlLMAApp.mAppContext

    suspend fun saveUserInfo(
        uuid: String,
        email: String,
        username: String,
        userType: String,
        userImage: String,
        tokenFcm: String
    ) {
        context.xDataStorageUser.edit {
            it[USER_UUID] = uuid
            it[USER_EMAIL] = email
            it[USER_NAME] = username
            it[USER_TYPE] = userType
            it[USER_IMG] = userImage
            it[USER_FCM_TOKEN] = tokenFcm
        }
    }

    suspend fun saveLoginBool(isLogin: Boolean) {
        context.xDataStorageUser.edit {
            it[USER_LOGIN] = isLogin
        }
    }

    fun getLogin() = context.xDataStorageUser.data.map {
        it[USER_LOGIN] ?: false
    }

    fun getUserType() = context.xDataStorageUser.data.map {
        it[USER_TYPE].orEmpty()
    }

    fun getUserUuid() = context.xDataStorageUser.data.map {
        it[USER_UUID].orEmpty()
    }

    fun getUsername() = context.xDataStorageUser.data.map {
        it[USER_NAME].orEmpty()
    }

    fun getUserImage() = context.xDataStorageUser.data.map {
        it[USER_IMG].orEmpty()
    }

    fun getUserEmail() = context.xDataStorageUser.data.map {
        it[USER_EMAIL].orEmpty()
    }
}