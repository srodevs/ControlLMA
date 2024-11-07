package com.controllma.data.service

import com.controllma.data.model.NewModel
import com.controllma.data.model.NewResponse
import com.controllma.data.model.UserResponse
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "firebaseDbService"

class FirebaseDbService @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        const val TABLE_USER = "user"
        const val TABLE_NEWS = "news"
    }

    suspend fun getUserInf(uuid: String): UserResponse? {
        return firebaseDatabase.getReference(TABLE_USER).child(uuid)
            .get().await().getValue(UserResponse::class.java)
    }

    fun getAllNews(): Flow<List<NewResponse>> {
        return firebaseDatabase.getReference(TABLE_NEWS).snapshots.map { snap ->
            snap.children.mapNotNull {
                it.getValue(NewResponse::class.java)
            }
        }
    }

    fun creteNew(newResponse: NewModel): Boolean {
        val mId = firebaseDatabase.getReference(TABLE_NEWS).push().key.toString()
        newResponse.newId = mId
        return firebaseDatabase.getReference(TABLE_NEWS).child(mId)
            .setValue(newResponse).isSuccessful
    }
}