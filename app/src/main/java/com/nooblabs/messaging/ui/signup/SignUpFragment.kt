package com.nooblabs.messaging.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.nooblabs.messaging.R
import com.nooblabs.messaging.ui.login.LoginFragment
import com.nooblabs.messaging.ui.replaceFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sign_up_fragment.view.*

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel

        view?.let { view ->
            view.sign_up_btn.setOnClickListener {
                //Reset
                view.error_message.visibility = View.GONE
                view.progress_bar.visibility = View.VISIBLE
                val username = view.username.text.toString()
                val email = view.email.text.toString()
                val password = view.password.text.toString()
                viewModel.tryCreateAccount(email,password,username)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(object : DisposableCompletableObserver() {
                            override fun onComplete() {
                                //If account creation is successful, user need to login
                                Snackbar.make(view,"Account successfully created. Now Log in.", Snackbar.LENGTH_SHORT).show()
                                fragmentManager?.replaceFragment(R.id.container, LoginFragment.newInstance())
                            }

                            override fun onError(e: Throwable) {
                                view.error_message.text = e.localizedMessage
                                view.error_message.visibility = View.VISIBLE
                                view.progress_bar.visibility = View.GONE
                            }
                        })
            }
            view.go_to_login_btn.setOnClickListener {
                fragmentManager?.replaceFragment(R.id.container, LoginFragment.newInstance())
            }
        }
    }

}
