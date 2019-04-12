package br.com.bonnepet.view.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.bonnepet.data.session.SessionManager

class MainViewModel(app: Application): AndroidViewModel(app) {

    fun isUserAuthenticated(): Boolean {
        return SessionManager.isLoggedIn()
    }
}