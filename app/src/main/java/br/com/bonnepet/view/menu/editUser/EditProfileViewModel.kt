package br.com.bonnepet.view.menu.editUser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.data.model.EditProfileDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.ExternalRepository
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class EditProfileViewModel(override val app: Application) : BaseViewModel(app) {

    private val userRepository = UserRepository()

    private val externalRepository = ExternalRepository()

    /**
     *  Flag pra informar que a requisicao de busca de endereco foi iniciada ou encerrada
     */
    private val _onAddressRequest = MutableLiveData<Boolean>()
    val onAddressRequest: LiveData<Boolean> = _onAddressRequest

    /**
     * O endereço
     */
    private val _address = MutableLiveData<CepDTO>()
    val address: LiveData<CepDTO> = _address

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

    /**
     *  Obtém o endereço através do [cep]
     */
    fun getAddress(cep: String) {
        if (cep.length == 8) {
            _onAddressRequest.value = true
            compositeDisposable.add(
                externalRepository.getAddress(cep)
                    .subscribeBy(onSuccess = {
                        _onAddressRequest.value = false
                        if (!it.error.toBoolean()) _address.value = it
                    }, onError = {
                        _onAddressRequest.value = false
                    })
            )
        }
    }
}