package br.com.lardopet.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lardopet.data.model.AddressDTO
import br.com.lardopet.data.repository.ExternalRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class RegisterViewModel : ViewModel() {

    private val externalRepository = ExternalRepository()

    /**
     *  Flag pra informar que a requisicao de endereco foi iniciada ou encerrada
     */
    private val onAddressRequest = MutableLiveData<Boolean>()
    fun onAddressRequest(): LiveData<Boolean> = onAddressRequest

    /**
     * O endereço
     */
    private val address = MutableLiveData<AddressDTO>()
    fun address(): LiveData<AddressDTO> = address

    /**
     *  Obtém o endereço
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
}