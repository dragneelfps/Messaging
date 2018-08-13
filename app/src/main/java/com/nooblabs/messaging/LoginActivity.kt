package com.nooblabs.messaging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nooblabs.messaging.ui.home.HomeFragment
import com.nooblabs.messaging.ui.login.LoginFragment
import com.nooblabs.messaging.ui.replaceFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {

            val user = FirebaseAuth.getInstance().currentUser
            if(user == null){
                supportFragmentManager?.replaceFragment(R.id.container, LoginFragment.newInstance())
            } else {
                supportFragmentManager?.replaceFragment(R.id.container, HomeFragment.newInstance())
            }


            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow()
        }
    }

}
