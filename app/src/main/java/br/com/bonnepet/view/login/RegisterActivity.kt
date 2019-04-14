package br.com.bonnepet.view.login

import android.os.Bundle
import android.view.MenuItem
import br.com.bonnepet.R
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_register

    private val fragmentContent by lazy { R.id.fragment_content }

    private val progressBar by lazy { progress_bar }

    var selectedUriImage: String? = null

    lateinit var userDTO: UserDTO

    override fun onPrepareActivity(state: Bundle?) {
        replaceFragment(fragmentContent, UserInfoRegisterFragment())

        val items = ArrayList(arrayOf("teste1", "teste2", "teste3", "teste4").toList())
//        val customAdapter = CustomSpinner(this, items)
//        inputState.setAdapter(customAdapter)
//        inputState.adapter.getItem(3)
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
