package br.com.bonnepet.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.MainFragmentEnum
import br.com.bonnepet.util.extension.addFragment
import br.com.bonnepet.util.extension.showFragment
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
    private val container by lazy { fragment_content.id }
    private lateinit var viewModel: MainViewModel

    private val bottomMenu by lazy { navigation }

    private val searchFragment by lazy { SearchHostFragment() }
    private val bookingFragment by lazy { BookingFragment() }
    private val petFragment by lazy { PetFragment() }
    private val menuFragment by lazy { MenuFragment() }
    private val loginFragment by lazy { LoginFragment() }

    private var activeFragment: Fragment = searchFragment

    private val fragmentManager = supportFragmentManager

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.initViewModel(intent)

        if (viewModel.isUserAuthenticated()) userAuthenticated()
        else userNotAuthenticated()

        bottomMenu.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }

    fun userAuthenticated() {
        bottomMenu.menu.findItem(R.id.navigation_menu).isChecked = true
        supportFragmentManager.beginTransaction().remove(MainFragmentEnum.LOGIN.instance)
        viewModel.userProfile()
        viewModel.onUserProfile.observe(this, Observer { profileDTO ->
            val bundle = Bundle().apply {
                putSerializable(Data.PROFILE_DTO, profileDTO)
            }
            menuFragment.arguments = bundle
            swapFragments(MainFragmentEnum.MENU)
        })
    }

    private fun userNotAuthenticated() {
        bottomMenu.menu.findItem(R.id.navigation_menu).isChecked = true
        swapFragments(MainFragmentEnum.LOGIN)
    }

    /**
     * Listener do BottomMenu. Caso o usuario esteja autenticado, libera o acesso de toda navegacao do app.
     */
    private val bottomNavigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                MainFragmentEnum.SEARCH.id -> {
                    swapFragments(MainFragmentEnum.SEARCH)
                    return@OnNavigationItemSelectedListener true
                }
                MainFragmentEnum.BOOKING.id -> {
                    swapFragments(MainFragmentEnum.BOOKING)
                    return@OnNavigationItemSelectedListener true
                }
                MainFragmentEnum.PET.id -> {
                    swapFragments(MainFragmentEnum.PET)
                    return@OnNavigationItemSelectedListener true
                }
                MainFragmentEnum.MENU.id -> {
                    swapFragments(MainFragmentEnum.MENU)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    private fun swapFragments(fragmentEnum: MainFragmentEnum) {
        if (activeFragment::class.java == fragmentEnum.instance::class.java) return

        if (viewModel.isUserNotAuthenticated() && fragmentEnum != MainFragmentEnum.SEARCH) {
            if (fragmentManager.findFragmentByTag(MainFragmentEnum.LOGIN.name) == null) {
                addFragment(container, loginFragment, fragmentEnum.name)
            }
            showFragment(activeFragment, loginFragment)
            activeFragment = loginFragment
            return
        }

        when (fragmentEnum) {
            MainFragmentEnum.SEARCH -> {
                if (fragmentManager.findFragmentByTag(fragmentEnum.name) == null) {
                    addFragment(container, searchFragment, fragmentEnum.name)
                }
                showFragment(activeFragment, searchFragment)
                activeFragment = searchFragment
            }
            MainFragmentEnum.BOOKING -> {
                if (fragmentManager.findFragmentByTag(fragmentEnum.name) == null) {
                    addFragment(container, bookingFragment, fragmentEnum.name)
                }
                showFragment(activeFragment, bookingFragment)
                activeFragment = bookingFragment
            }
            MainFragmentEnum.PET -> {
                if (fragmentManager.findFragmentByTag(fragmentEnum.name) == null) {
                    addFragment(container, petFragment, fragmentEnum.name)
                }
                showFragment(activeFragment, petFragment)
                activeFragment = petFragment
            }
            else -> {
                if (fragmentManager.findFragmentByTag(fragmentEnum.name) == null) {
                    addFragment(container, menuFragment, fragmentEnum.name)
                }
                showFragment(activeFragment, menuFragment)
                activeFragment = menuFragment
            }
        }

    }
}
