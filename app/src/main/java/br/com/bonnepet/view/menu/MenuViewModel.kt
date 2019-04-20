package br.com.bonnepet.view.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class MenuViewModel(val app: Application) : AndroidViewModel(app) {

    private val userRepository = UserRepository()

    private val _onUserProfile = MutableLiveData<ProfileDTO>()
    val onUserProfile: LiveData<ProfileDTO> = _onUserProfile

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun clearUserSession() {
        SessionManager.clearUserSession()
    }

    fun userProfile() {

        CompositeDisposable().add(userRepository.getUserProfile()
            .subscribeBy(onSuccess = {
                _onUserProfile.value = it
            }, onError = {
                _message.value = it.error(app)
            })
        )
    }
}