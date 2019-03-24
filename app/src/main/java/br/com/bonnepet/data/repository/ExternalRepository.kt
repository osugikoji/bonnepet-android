package br.com.bonnepet.data.repository

import br.com.bonnepet.data.api.ExternalApi
import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.model.AddressDTO
import br.com.bonnepet.util.SchedulerProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

/**
 *  Repositorios de Apis de terceiros
 */
class ExternalRepository {

    private val externalApi = RetrofitConfig.getInstance().create<ExternalApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    fun getAddress(cep: String): Single<AddressDTO> {
        return externalApi.getAddress(cep).compose(schedulerProvider.getSchedulersForSingle())
    }

}