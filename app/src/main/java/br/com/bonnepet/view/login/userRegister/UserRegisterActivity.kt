package br.com.bonnepet.view.login.userRegister

import android.os.Bundle
import android.view.MenuItem
import br.com.bonnepet.R
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import java.io.File


class UserRegisterActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_register
    override val activityTitle = R.string.title_activity_register

    private val fragmentContent by lazy { R.id.fragment_content }

    var selectedImage: File? = null

    lateinit var userDTO: UserDTO

    override fun onPrepareActivity(state: Bundle?) {
        replaceFragment(fragmentContent, UserRegisterOneFragment())
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
