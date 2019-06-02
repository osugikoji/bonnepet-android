package br.com.bonnepet.view.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.data.util.SessionManager
import br.com.bonnepet.data.enums.StatusCodeEnum
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException

class SplashViewModel(override val app: Application) : BaseViewModel(app) {

    private val userRepository = UserRepository()

    private val _onUserProfile = MutableLiveData<ProfileDTO>()
    val onUserProfile: LiveData<ProfileDTO> = _onUserProfile

    fun userProfile() {
        compositeDisposable.add(
            userRepository.getUserProfile()
                .subscribeBy(onSuccess = {
                    _onUserProfile.value = it
                }, onError = {
                    try {
                        if (StatusCodeEnum.Forbidden.code == (it as HttpException).code()) sessionExpired.value = true
                        else errorMessage.value = it.error(app)
                    } catch (e: Exception) {
                        sessionExpired.value = true
                    }
                })
        )
    }

    fun isUserAuthenticated(): Boolean =
        SessionManager.isUserAuthenticated()
}