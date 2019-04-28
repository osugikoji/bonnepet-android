package br.com.bonnepet.view.menu.editUser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.EditProfileDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class EditProfileViewModel(override val app: Application) : BaseViewModel(app) {

    private val userRepository = UserRepository()

    private val _onUpdateUserProfile = MutableLiveData<ProfileDTO>()
    val onUpdateUserProfile: LiveData<ProfileDTO> = _onUpdateUserProfile

    fun updateUserProfile(editProfileDTO: EditProfileDTO) {
        compositeDisposable.add(
            userRepository.updateUserProfile(editProfileDTO)
                .subscribeBy(onSuccess = {
                    _onUpdateUserProfile.value = it
                }, onError = {
                    errorMessage.value = it.error(app)
                })
        )
    }
}