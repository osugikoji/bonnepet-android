package br.com.bonnepet.view.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.util.extension.replaceFragment
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.booking.BookingFragment
import br.com.bonnepet.view.host.SearchHostFragment
import br.com.bonnepet.view.login.LoginFragment
import br.com.bonnepet.view.menu.MenuFragment
import br.com.bonnepet.view.pet.PetFragment
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
        bottomMenu.setOnNavigationItemSelectedListener(bottomNavigationListener)
        if (viewModel.isUserAuthenticated()) userAuthenticated()
        else userNotAuthenticated()
    }

    fun userAuthenticated() {
        bottomMenu.menu.findItem(R.id.navigation_menu).isChecked = true
        replaceFragment(fragmentContent, MenuFragment())
    }

    private fun userNotAuthenticated() {
        bottomMenu.menu.findItem(R.id.navigation_menu).isChecked = true
        replaceFragment(fragmentContent, LoginFragment())
    }

    /**
     * Listener do BottomMenu. Caso o usuario esteja autenticado, libera o acesso de toda navegacao do app.
     */
    private val bottomNavigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    replaceFragment(fragmentContent, SearchHostFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reservations -> {
                    if (viewModel.isUserAuthenticated()) replaceFragment(fragmentContent, BookingFragment())
                    else replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_pet -> {
                    if (viewModel.isUserAuthenticated()) replaceFragment(fragmentContent, PetFragment())
                    else replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_menu -> {
                    if (viewModel.isUserAuthenticated()) replaceFragment(fragmentContent, MenuFragment())
                    else replaceFragment(fragmentContent, LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
}
