package br.com.bonnepet.view.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment() {
    override val layoutResource = R.layout.login_fragment

    private val registerLink by lazy { register_link }

    private lateinit var viewModel: LoginViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        registerLink.setSafeOnClickListener { goToRegister() }
    }

    private fun goToRegister() {
        startActivity(Intent(context, RegisterActivity::class.java))
    }

}