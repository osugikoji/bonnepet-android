package br.com.bonnepet.view.menu

import android.os.Bundle
import br.com.bonnepet.R
import br.com.bonnepet.view.base.BaseFragment


class MenuFragment : BaseFragment() {
    override val layoutResource: Int =  R.layout.fragment_menu
    override val fragmentTitle: Int = R.string.menu

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionBarDisplayHome()
    }
}
