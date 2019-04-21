package br.com.bonnepet.view.main

import android.app.Application
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.view.base.BaseViewModel

class MainViewModel(override val app: Application): BaseViewModel(app) {

    fun isUserAuthenticated(): Boolean {
        return SessionManager.isLoggedIn()
    }
}