package com.nooblabs.messaging.ui.signup

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable

class SignUpViewModel : ViewModel() {

    fun tryCreateAccount(email: String, password: String, username: String): Completable {
        val signUpStatus = Completable.create { emitter ->
            try {
                SystemClock.sleep(1500)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val profileUpdate = UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build()
                            FirebaseAuth.getInstance().currentUser!!.updateProfile(profileUpdate)
                                    .addOnCompleteListener {
                                        FirebaseAuth.getInstance().signOut()
                                        emitter.onComplete()
                                    }
                        }
                        .addOnFailureListener {
                            emitter.onError(it)
                        }
            }catch (e: Exception) {
                emitter.onError(e)
            }
        }
        return signUpStatus
    }


}
