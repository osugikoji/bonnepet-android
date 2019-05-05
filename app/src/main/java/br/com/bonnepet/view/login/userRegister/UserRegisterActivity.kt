package br.com.bonnepet.view.login.userRegister

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import br.com.bonnepet.R
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File


class UserRegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_register
    override val activityTitle = R.string.title_activity_register

    private val fragmentContent by lazy { R.id.fragment_content }

    private val progressBar by lazy { progress_bar }

    var selectedImage: File? = null

    lateinit var userDTO: UserDTO

    override fun onPrepareActivity(state: Bundle?) {
        replaceFragment(fragmentContent, UserRegisterOneFragment())
    }

    /** Caso o cadastro do usuário for um sucesso, autentica o usuário. */
    fun authenticateUser() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * Mostra o progress bar da view, caso ele exista
     */
    fun showLoading() {
        progressBar.isVisible = true
    }

    /**
     * Esconde o progress bar da view, caso ele exista
     */
    fun hideLoading() {
        progressBar.isVisible = false
    }

    /**
     *  Acao do botao de voltar da actionBar
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
