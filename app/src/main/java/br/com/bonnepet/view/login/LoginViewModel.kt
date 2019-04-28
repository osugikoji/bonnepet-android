package br.com.bonnepet.view.login

import Header
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.data.StatusCodeEnum
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class LoginViewModel(override val app: Application) : BaseViewModel(app) {

    private val userRepository = UserRepository()

    private val _onLoginSuccess = MutableLiveData<Boolean>()
    val onLoginSuccess: LiveData<Boolean> = _onLoginSuccess

    fun authenticateUser(credential: Credential) {
        compositeDisposable.add(userRepository.authenticateUser(credential)
            .subscribeBy(onNext = { response ->
                when(response.code()) {
                    StatusCodeEnum.OK.code -> {
                        SessionManager.createUserSession(response.headers().get(Header.AUTHORIZATION))
                        _onLoginSuccess.value = true
                    }
                    else -> {
                        errorMessage.value = app.getString(R.string.login_invalid)
                        _onLoginSuccess.value = false
                    }
                }
            }, onError = {
                errorMessage.value = it.error(getApplication())
            })
        )
    }
}
