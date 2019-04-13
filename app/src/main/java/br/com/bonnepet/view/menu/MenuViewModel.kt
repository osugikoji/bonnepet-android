package br.com.bonnepet.view.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.bonnepet.data.session.SessionManager

class MenuViewModel(app: Application): AndroidViewModel(app) {

    fun clearUserSession() {
        SessionManager.clearUserSession()
    }
}