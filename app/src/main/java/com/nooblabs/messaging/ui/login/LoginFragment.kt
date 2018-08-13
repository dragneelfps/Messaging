package com.nooblabs.messaging.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.nooblabs.messaging.R
import com.nooblabs.messaging.ui.home.HomeFragment
import com.nooblabs.messaging.ui.replaceFragment
import com.nooblabs.messaging.ui.signup.SignUpFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel

        view?.let { view ->
            view.login_btn.setOnClickListener {
                //Reset
                view.error_message.visibility = View.GONE
                view.progress_bar.visibility = View.VISIBLE
                val email = view.email.text.toString()
                val password = view.password.text.toString()
                compositeDisposable.add(viewModel.tryLogin(email,password)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(object : DisposableSingleObserver<FirebaseUser>() {
                    override fun onSuccess(t: FirebaseUser) {
                        Snackbar.make(view,"Welcome ${FirebaseAuth.getInstance().currentUser!!.email}", Snackbar.LENGTH_SHORT).show()
                        fragmentManager?.replaceFragment(R.id.container, HomeFragment.newInstance())
                    }

                    override fun onError(e: Throwable) {
                        view.error_message.text = e.localizedMessage
                        view.error_message.visibility = View.VISIBLE
                        view.progress_bar.visibility = View.GONE
                    }
                }))
            }

            view.go_to_signup_btn.setOnClickListener {
                fragmentManager?.replaceFragment(R.id.container, SignUpFragment.newInstance())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}
