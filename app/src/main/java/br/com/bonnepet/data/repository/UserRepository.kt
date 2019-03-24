package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.UserApi
import br.com.bonnepet.data.model.UserDTO
import br.com.bonnepet.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun registerUser(userDTO: UserDTO): Completable {
        return userApi.registerUser(userDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }

}