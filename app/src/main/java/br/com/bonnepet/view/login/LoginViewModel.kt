package br.com.bonnepet.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.data.StatusCode
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    private val userRepository = UserRepository()

    private val _onLoginSuccess = MutableLiveData<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun authenticateUser(credential: Credential) {
        CompositeDisposable().add(userRepository.authenticateUser(credential)
            .subscribeBy(onNext = { response ->
                when(response.code()) {
                    StatusCode.OK.code -> {
                        SessionManager.createUserSession(response.headers().get(Header.AUTHORIZATION))
                        _onLoginSuccess.value = true
                    }
                    else -> {
                        _message.value = app.getString(R.string.login_invalid)
                        _onLoginSuccess.value = false
                    }
                }
            }, onError = {
                _message.value = it.error(getApplication())
            })
        )
    }
}
