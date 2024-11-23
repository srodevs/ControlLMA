package com.controllma.data.service

import androidx.compose.animation.core.snap
import com.controllma.data.model.NewModel
import com.controllma.data.model.NewResponse
import com.controllma.data.model.RollCall
import com.controllma.data.model.UserResponse
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

private const val TAG = "firebaseDbService"

class FirebaseDbService @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        const val TABLE_USER = "user"
        const val TABLE_NEWS = "news"
        const val TABLE_ASSISTANT = "roll_call"
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

    suspend fun getMyRollCall(uuId: String): Flow<List<RollCall>> {
        return firebaseDatabase.getReference(TABLE_ASSISTANT).snapshots.map { snapshot ->
            snapshot.children.flatMap { dateSnapshot ->
                dateSnapshot.child(getCurrentDateWithoutTime()).child(uuId).children.mapNotNull {
                    it.getValue(RollCall::class.java)
                }
            }
        }
    }

    suspend fun createRollCall(rollCall: RollCall): Boolean {
        val mKey = firebaseDatabase.getReference(TABLE_ASSISTANT).child(rollCall.uuId.toString())
            .push().key
        rollCall.registerId = mKey
        rollCall.date = getCurrentDateWithoutTime()


        val existRegister = firebaseDatabase.getReference(TABLE_ASSISTANT).child(rollCall.uuId.toString())
            .child(rollCall.uuId.toString()).get().await().exists()

        return if (existRegister) {
            true
        } else {
            suspendCancellableCoroutine { cancelable ->
                firebaseDatabase.getReference(TABLE_ASSISTANT).child(getCurrentDateWithoutTime())
                    .child(rollCall.uuId.toString()).setValue(rollCall)
                    .addOnCompleteListener {
                        cancelable.resume(it.isSuccessful)
                    }
            }
        }
    }

    private fun getCurrentDateWithoutTime(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
    }
}