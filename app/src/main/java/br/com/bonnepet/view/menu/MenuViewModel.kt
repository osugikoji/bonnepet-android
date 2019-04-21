package br.com.bonnepet.view.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.PictureDTO
import br.com.bonnepet.data.model.ProfileDTO
import br.com.bonnepet.data.repository.UserRepository
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.extension.error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MenuViewModel(val app: Application) : AndroidViewModel(app) {

    private val userRepository = UserRepository()

    private val _onUserProfile = MutableLiveData<ProfileDTO>()
    val onUserProfile: LiveData<ProfileDTO> = _onUserProfile

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _onProfilePictureUpload = MutableLiveData<PictureDTO>()
    val onProfilePictureUpload: LiveData<PictureDTO> = _onProfilePictureUpload

    fun clearUserSession() {
        SessionManager.clearUserSession()
    }

    fun updateProfilePicture(selectedUriImage: File?) {
        val bodyImage = buildMultipartBodyImage(selectedUriImage)
        val id: String = SessionManager.getUserId()!!.toString()

        CompositeDisposable().add(
            userRepository.uploadProfilePicture(id, bodyImage!!)
                .subscribeBy(onSuccess = {
                    _onProfilePictureUpload.value = it
                }, onError = {
                    _message.value = it.error(app)
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
        CompositeDisposable().add(
            userRepository.getUserProfile()
                .subscribeBy(onSuccess = {
                    _onUserProfile.value = it
                }, onError = {
                    _message.value = it.error(app)
                })
        )
    }

    fun getProfileDTO(): ProfileDTO? {
        return onUserProfile.value
    }
}