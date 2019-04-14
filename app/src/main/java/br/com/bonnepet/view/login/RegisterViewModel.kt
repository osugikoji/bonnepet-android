package br.com.bonnepet.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.data.repository.ExternalRepository
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    private val externalRepository = ExternalRepository()

    private val userRepository = UserRepository()

    /**
     *  Flag pra informar que a requisicao de busca de endereco foi iniciada ou encerrada
     */
    private val _onAddressRequest = MutableLiveData<Boolean>()
    val onAddressRequest: LiveData<Boolean> = _onAddressRequest

    /**
     *  Flag pra informar o resultado da requisicao de cadastro
     */
    private val _userRegisterRequestResult = MutableLiveData<Boolean>()
    val userRegisterRequestResult: LiveData<Boolean> = _userRegisterRequestResult

    /**
     *  Mensagem de erro
     */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * O endereço
     */
    private val _address = MutableLiveData<CepDTO>()
    val address: LiveData<CepDTO> = _address

    /**
     *  Obtém o endereço através do [cep]
     */
    fun getAddress(cep: String) {
        if (cep.length == 8) {
            _onAddressRequest.value = true
            CompositeDisposable().add(externalRepository.getAddress(cep)
                    .subscribeBy(onSuccess = {
                        _onAddressRequest.value = false
                        if (!it.error.toBoolean()) _address.value = it
                    }, onError = {
                        _onAddressRequest.value = false
                    })
            )
        }
    }

    /**
     *  Efetua o cadastro de usuario. Caso [selectedUriImage] nao seja nulo, eh feito o upload da imagem
     *  no callback da requisicao
     *
     */
    fun doRegister(userDTO: UserDTO, selectedUriImage: String?) {
        val bodyImage = buildMultipartBodyImage(selectedUriImage)

        CompositeDisposable().add(userRepository.registerUser(userDTO)
            .subscribeBy(onSuccess = {
                if (bodyImage != null) {
                    // Faz o upload da imagem
                    CompositeDisposable().add(
                        userRepository.uploadProfilePicture(it.id, bodyImage).subscribeBy()
                    )
                }
                _userRegisterRequestResult.value = true
            }, onError = {
                _errorMessage.value = it.error(getApplication())
                _userRegisterRequestResult.value = false
            })
        )
    }

    /**
     *  Constroi [uriImage] no formato MuitipartBody.Part
     */
    private fun buildMultipartBodyImage(uriImage: String?): MultipartBody.Part? {
        return try {
            val file = File(uriImage)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        } catch (e: Exception) {
            return null
        }
    }
}