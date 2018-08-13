package com.nooblabs.messaging.ui.login

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

class LoginViewModel : ViewModel() {

    fun tryLogin(email: String, password: String): Single<FirebaseUser> {
        val loginStatus = Single.create<FirebaseUser> { emitter ->
            try {
                SystemClock.sleep(1500)
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            emitter.onSuccess(FirebaseAuth.getInstance().currentUser!!)
                        }
                        .addOnFailureListener {
                            emitter.onError(it)
                        }
            }catch (e: Exception) {
                emitter.onError(e)
            }

        }
        return loginStatus
    }

}
