package br.com.bonnepet.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.data.repository.ExternalRepository
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class RegisterViewModel : ViewModel() {

    private val externalRepository = ExternalRepository()

    private val userRepository = UserRepository()

    /**
     *  Flag pra informar que a requisicao de endereco foi iniciada ou encerrada
     */
    private val onAddressRequest = MutableLiveData<Boolean>()
    fun onAddressRequest(): LiveData<Boolean> = onAddressRequest

    /**
     *  Flag pra informar o resultado da requisicao de cadastro
     */
    private val userRegisterRequestResult = MutableLiveData<Boolean>()
    fun userRegisterRequestResult(): LiveData<Boolean> = userRegisterRequestResult

    /**
     *  Mensagem de erro
     */
    private val errorMessage = MutableLiveData<String>()
    fun errorMessage(): LiveData<String> = errorMessage

    /**
     * O endereço
     */
    private val address = MutableLiveData<AddressDTO>()
    fun address(): LiveData<AddressDTO> = address

    /**
     *  Obtém o endereço através do [cep]
     */
    fun getAddress(cep: String) {
        if (cep.length == 8) {
            onAddressRequest.value = true
            CompositeDisposable().add(externalRepository.getAddress(cep)
                    .subscribeBy(onSuccess = {
                        onAddressRequest.value = false
                        if (!it.error.toBoolean()) address.value = it
                    }, onError = {
                        onAddressRequest.value = false
                    })
            )
        }
    }

    fun doRegister(userDTO: UserDTO) {
        CompositeDisposable().add(userRepository.registerUser(userDTO)
            .subscribeBy(onComplete = {
                userRegisterRequestResult.value = true
            }, onError = {
                errorMessage.value = it.error()
                userRegisterRequestResult.value = false
            })
        )
    }
}