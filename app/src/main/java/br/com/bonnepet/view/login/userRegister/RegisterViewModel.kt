package br.com.bonnepet.view.login.userRegister

import Header
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.model.CepDTO
import br.com.bonnepet.data.model.Credential
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.data.repository.ExternalRepository
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.data.util.SessionManager
import br.com.bonnepet.data.enums.StatusCodeEnum
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterViewModel(override val app: Application) : BaseViewModel(app) {

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

    /**
     *  Efetua o cadastro de usuario. Caso [selectedUriImage] nao seja nulo, eh feito o upload da imagem
     *  no callback da requisicao
     */
    fun doRegister(userDTO: UserDTO, selectedUriImage: File?) {
        val bodyImage = buildMultipartBodyImage(selectedUriImage)

        compositeDisposable.add(
            userRepository.registerUser(userDTO)
                .subscribeBy(onSuccess = {
                    val credential = Credential(userDTO.email, userDTO.password)
                    if (bodyImage != null) {
                        // Faz o upload da imagem
                        compositeDisposable.add(
                            userRepository.uploadProfilePicture(it.id, bodyImage)
                                .subscribeBy(onSuccess = {
                                    authenticateUser(credential)
                                }, onError = {
                                    authenticateUser(credential)
                                })
                        )
                    } else {
                        authenticateUser(credential)
                    }
                }, onError = {
                    errorMessage.value = it.error(getApplication())
                    _userRegisterRequestResult.value = false
                })
        )
    }

    private fun authenticateUser(credential: Credential) {
        compositeDisposable.add(
            userRepository.authenticateUser(credential)
                .subscribeBy(onNext = { response ->
                    when (response.code()) {
                        StatusCodeEnum.OK.code -> {
                            SessionManager.createUserSession(response.headers().get(Header.AUTHORIZATION))
                            _userRegisterRequestResult.value = true
                        }
                        else -> {
                            errorMessage.value = app.getString(R.string.login_invalid)
                        }
                    }
                }, onError = {
                    errorMessage.value = it.error(getApplication())
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