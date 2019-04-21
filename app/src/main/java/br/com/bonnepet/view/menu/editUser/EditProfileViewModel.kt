package br.com.bonnepet.view.menu.editUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.EditProfileDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class EditProfileViewModel(val app: Application) : AndroidViewModel(app) {

    private val userRepository = UserRepository()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _onUpdateUserProfile = MutableLiveData<ProfileDTO>()
    val onUpdateUserProfile: LiveData<ProfileDTO> = _onUpdateUserProfile

    fun updateUserProfile(editProfileDTO: EditProfileDTO) {
        CompositeDisposable().add(
            userRepository.updateUserProfile(editProfileDTO)
                .subscribeBy(onSuccess = {
                    _onUpdateUserProfile.value = it
                }, onError = {
                    _message.value = it.error(app)
                })
        )
    }
}