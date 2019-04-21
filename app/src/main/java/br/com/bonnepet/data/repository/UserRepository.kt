package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.UserApi
import br.com.bonnepet.data.model.*
import br.com.bonnepet.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.create

/**
 *  Repositorios de Apis de terceiros
 */
class UserRepository {

    private val userApi = RetrofitConfig.getInstance().create<UserApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    fun authenticateUser(credential: Credential): Observable<Response<Void>> {
        return userApi.authenticateUser(credential).compose(schedulerProvider.getSchedulersForObservable())
    }

    fun registerUser(userDTO: UserDTO): Single<UserDTO> {
        return userApi.registerUser(userDTO).compose(schedulerProvider.getSchedulersForSingle())
    }

    fun uploadProfilePicture(id: String?, file: MultipartBody.Part): Single<PictureDTO> {
        return userApi.uploadProfilePicture(id, file).compose(schedulerProvider.getSchedulersForSingle())
    }

    fun getUserProfile(): Single<ProfileDTO> {
        return userApi.getUserProfile().compose(schedulerProvider.getSchedulersForSingle())
    }

    fun updateUserProfile(editProfileDTO: EditProfileDTO): Single<ProfileDTO> {
        return userApi.updateUser(editProfileDTO).compose(schedulerProvider.getSchedulersForSingle())
    }
}