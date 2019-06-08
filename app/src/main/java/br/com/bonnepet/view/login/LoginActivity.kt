package br.com.bonnepet.view.login

import RequestCode
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.login.userRegister.UserRegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_login
    override val activityTitle = R.string.get_in
    private lateinit var viewModel: LoginViewModel

    private val editTextEmail by lazy { input_email }

    private val editTextPassword by lazy { input_password }

    private val btnLogin by lazy { btn_login }

    private val registerLink by lazy { register_link }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        btnLogin.setSafeOnClickListener { doLogin() }
        registerLink.setSafeOnClickListener { goToRegister() }

        viewModel.errorMessage().observe(this, Observer { message ->
            progressBarVisibility(false)
            showToast(message)
        })

        viewModel.onLoginSuccess.observe(this, Observer { authenticationResult ->
            if (authenticationResult) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    private fun doLogin() {
        progressBarVisibility(true)
        val credential = Credential(editTextEmail.text.toString(), editTextPassword.text.toString())
        viewModel.authenticateUser(credential)
    }

    private fun goToRegister() {
        startActivityForResult(Intent(this, UserRegisterActivity::class.java), RequestCode.SIGN_UP)
    }

    private fun progressBarVisibility(visibility: Boolean) {
        editTextEmail.isEnabled = !visibility
        editTextPassword.isEnabled = !visibility
        btnLogin.text = if (visibility) null else getString(R.string.get_in)
        progress_bar_login.isVisible = visibility
        registerLink.isVisible = !visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.SIGN_UP -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    /**
     *  Ação do botão de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
