package br.com.bonnepet.view.login.userRegister

import Header
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.data.repository.ExternalRepository
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.data.StatusCode
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterViewModel(val app: Application) : AndroidViewModel(app) {

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
            CompositeDisposable().add(
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

    /**
     *  Efetua o cadastro de usuario. Caso [selectedUriImage] nao seja nulo, eh feito o upload da imagem
     *  no callback da requisicao
     */
    fun doRegister(userDTO: UserDTO, selectedUriImage: File?) {
        val bodyImage = buildMultipartBodyImage(selectedUriImage)

        CompositeDisposable().add(
            userRepository.registerUser(userDTO)
                .subscribeBy(onSuccess = {
                    if (bodyImage != null) {
                        val credential = Credential(userDTO.email, userDTO.password)
                        // Faz o upload da imagem
                        CompositeDisposable().add(
                            userRepository.uploadProfilePicture(it.id, bodyImage)
                                .subscribeBy(onSuccess = {
                                    authenticateUser(credential)
                                }, onError = {
                                    authenticateUser(credential)
                                })
                        )
                    }
                }, onError = {
                    _errorMessage.value = it.error(getApplication())
                    _userRegisterRequestResult.value = false
                })
        )
    }

    private fun authenticateUser(credential: Credential) {
        CompositeDisposable().add(
            userRepository.authenticateUser(credential)
                .subscribeBy(onNext = { response ->
                    when (response.code()) {
                        StatusCode.OK.code -> {
                            SessionManager.createUserSession(response.headers().get(Header.AUTHORIZATION))
                            _userRegisterRequestResult.value = true
                        }
                        else -> {
                            _errorMessage.value = app.getString(R.string.login_invalid)
                        }
                    }
                }, onError = {
                    _errorMessage.value = it.error(getApplication())
                })
        )
    }

    /**
     *  Constroi [file] no formato MuitipartBody.Part
     */
    private fun buildMultipartBodyImage(file: File?): MultipartBody.Part? {
        return try {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file ?: return null)
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        } catch (e: Exception) {
            return null
        }
    }
}