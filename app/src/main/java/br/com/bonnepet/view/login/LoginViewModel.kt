package br.com.bonnepet.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.data.session.SessionManager
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val userRepository = UserRepository()

    private val _onLoginSuccess = MutableLiveData<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun authenticateUser(credential: Credential) {
        CompositeDisposable().add(userRepository.authenticateUser(credential)
            .subscribeBy(onNext = {
                createUserSessionIfTokenExist(it.headers().get("Authorization"))
                _onLoginSuccess.value = true
            }, onError = {
                _message.value = it.error(getApplication())
            }))
    }

    private fun createUserSessionIfTokenExist(tokenHeader: String?) {
        if (tokenHeader != null) {
            val token = tokenHeader.substringAfterLast(" ")
            SessionManager.createUserSession(token)
        }
    }
}
