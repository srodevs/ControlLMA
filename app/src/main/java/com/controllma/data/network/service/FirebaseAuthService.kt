package com.controllma.data.network.service

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "firebaseAuth"
    }

    suspend fun loginUser(
        email: String,
        password: String,
    ): Boolean {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d(
                        TAG, """
                        firebaseAuthService
                        it.result.user?.uid
                        ${it.isSuccessful} 
                    """.trimIndent()
                    )
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    fun getCurrentUser() = firebaseAuth.currentUser?.uid

    fun logOut() {
        firebaseAuth.signOut()
    }
}