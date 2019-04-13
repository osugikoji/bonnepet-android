package br.com.bonnepet.view.menu

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment
import br.com.bonnepet.view.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : BaseFragment() {
    override val layoutResource: Int = R.layout.fragment_menu
    override val fragmentTitle: Int = R.string.menu

    private lateinit var viewModel: MenuViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        hideActionBarDisplayHome()

        exit.setOnClickListener { logout() }
    }

    private fun logout() {
        viewModel.clearUserSession()

        val intent = Intent(activity, SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}
