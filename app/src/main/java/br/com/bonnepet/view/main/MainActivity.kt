package br.com.bonnepet.view.main

import android.os.Bundle
import android.widget.TextView
import br.com.bonnepet.R
import br.com.bonnepet.data.session.SessionManager
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.login.LoginFragment
import br.com.bonnepet.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_main

    private val fragmentContent by lazy { fragment_content.id }

    override fun onPrepareActivity(state: Bundle?) {
        replaceFragment(SearchFragment(), fragmentContent)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     *  Listener do BottomMenu. Caso o usuario esteja autenticado, libera o acesso de toda navegacao do app.
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (SessionManager.isLoggedIn()) {
            when (item.itemId) {
                R.id.navigation_search -> {
                    replaceFragment(SearchFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
        } else {
            when (item.itemId) {
                R.id.navigation_search -> {
                    replaceFragment(SearchFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }
}
