package br.com.bonnepet.view.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.login.LoginFragment
import br.com.bonnepet.view.menu.MenuFragment
import br.com.bonnepet.view.pet.PetFragment
import br.com.bonnepet.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_main
    override val activityTitle: Nothing? = null

    private val fragmentContent by lazy { fragment_content.id }

    private lateinit var viewModel: MainViewModel

    private val bottomMenu by lazy { navigation }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        if (viewModel.isUserAuthenticated()) userAuthenticated() else userNotAuthenticated()
    }

    fun userAuthenticated() {
        bottomMenu.selectedItemId = R.id.navigation_menu
        bottomMenu.setOnNavigationItemSelectedListener(bottomNavigationListenerUserAuthenticated)
        replaceFragment(fragmentContent, MenuFragment())
    }

    private fun userNotAuthenticated() {
        bottomMenu.selectedItemId = R.id.navigation_menu
        bottomMenu.setOnNavigationItemSelectedListener(bottomNavigationListenerUserNotAuthenticated)
        replaceFragment(fragmentContent, LoginFragment())
    }

    /**
     * Listener do BottomMenu. Caso o usuario nao esteja autenticado, Eh liberado o acesso parcial da navegacao.
     */
    private val bottomNavigationListenerUserNotAuthenticated =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    replaceFragment(fragmentContent, SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    /**
     * Listener do BottomMenu. Caso o usuario esteja autenticado, libera o acesso de toda navegacao do app.
     */
    private val bottomNavigationListenerUserAuthenticated =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    replaceFragment(fragmentContent, SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    replaceFragment(fragmentContent, PetFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    replaceFragment(fragmentContent, MenuFragment())
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
}
