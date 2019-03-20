package br.com.lardopet.view

import android.os.Bundle
import android.widget.TextView
import br.com.lardopet.R
import br.com.lardopet.session.SessionManager
import br.com.lardopet.util.replaceFragment
import br.com.lardopet.view.base.BaseActivity
import br.com.lardopet.view.login.LoginFragment
import br.com.lardopet.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_main

    private val fragmentContent by lazy { fragment_content.id }

    private val toolbar by lazy { this.findViewById(R.id.toolbar_title) as TextView }

    override fun onPrepareActivity(state: Bundle?) {
        hideActionBarDisplayHome()
        toolbar.text = getString(R.string.search_accommodation)
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
                    toolbar.text = getString(R.string.search_accommodation)
                    replaceFragment(SearchFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    toolbar.text = getString(R.string.my_revervations)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    toolbar.text = getString(R.string.my_pets)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    toolbar.text = getString(R.string.menu)
                    return@OnNavigationItemSelectedListener true
                }
            }
        } else {
            when (item.itemId) {
                R.id.navigation_search -> {
                    toolbar.text = getString(R.string.search_accommodation)
                    replaceFragment(SearchFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    toolbar.text = getString(R.string.get_in)
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    toolbar.text = getString(R.string.get_in)
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    toolbar.text = getString(R.string.get_in)
                    replaceFragment(LoginFragment(), fragmentContent)
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }
}
