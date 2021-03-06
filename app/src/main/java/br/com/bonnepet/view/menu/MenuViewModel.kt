package br.com.bonnepet.view.menu

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.PictureDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.data.util.SessionManager
import br.com.bonnepet.data.enums.StatusCodeEnum
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class MenuViewModel(override val app: Application) : BaseViewModel(app) {

    private val userRepository = UserRepository()

    private val _userProfileRetriever = MutableLiveData<ProfileDTO>()
    val userProfileRetriever: LiveData<ProfileDTO> = _userProfileRetriever

    private val _onProfilePictureUpload = MutableLiveData<PictureDTO>()
    val onProfilePictureUpload: LiveData<PictureDTO> = _onProfilePictureUpload

    private val _progressImageLoading = MutableLiveData<Boolean>()
    val progressImageLoading: LiveData<Boolean> = _progressImageLoading

    fun initViewModel(arguments: Bundle?) {
        try {
            _userProfileRetriever.value = arguments?.getSerializable(Data.PROFILE_DTO) as ProfileDTO
        } catch (e: Exception) {
            userProfile()
        }
    }

    fun clearUserSession() {
        SessionManager.clearUserSession()
    }

    fun updateProfilePicture(selectedUriImage: File?) {
        val bodyImage = buildMultipartBodyImage(selectedUriImage)
        val id: String = SessionManager.getUserId()!!.toString()

        _progressImageLoading.value = true
        compositeDisposable.add(
            userRepository.uploadProfilePicture(id, bodyImage!!)
                .subscribeBy(onSuccess = {
                    _progressImageLoading.value = false
                    _onProfilePictureUpload.value = it
                }, onError = {
                    _progressImageLoading.value = false
                    errorMessage.value = it.error(app)
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

    fun userProfile() {
        isLoading.value = true
        compositeDisposable.add(
            userRepository.getUserProfile()
                .subscribeBy(onSuccess = {
                    _userProfileRetriever.value = it
                    isLoading.value = false
                }, onError = {
                    isLoading.value = false
                    try {
                        if (StatusCodeEnum.Forbidden.code == (it as HttpException).code()) sessionExpired.value = true
                        else errorMessage.value = it.error(app)
                    } catch (e: Exception) {
                        sessionExpired.value = true
                    }
                })
        )
    }

    fun getProfileDTO(): ProfileDTO? {
        return userProfileRetriever.value
    }

    fun isProfileDTOEmpty(): Boolean {
        return userProfileRetriever.value == null
    }
}