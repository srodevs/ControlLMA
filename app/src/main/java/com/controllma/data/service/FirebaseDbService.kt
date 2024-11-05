package com.controllma.data.service

import com.controllma.data.model.UserResponse
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "firebaseDbService"

class FirebaseDbService @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        const val TABLE_USER = "user"
    }

    suspend fun getUserInf(uuid: String): UserResponse? {
        return firebaseDatabase.getReference(TABLE_USER).child(uuid)
            .get().await().getValue(UserResponse::class.java)
    }
}