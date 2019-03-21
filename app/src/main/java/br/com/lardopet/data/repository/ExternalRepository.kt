package br.com.lardopet.data.repository

import br.com.lardopet.data.api.ExternalApi
import br.com.lardopet.config.RetrofitConfig
import br.com.lardopet.data.model.AddressDTO
import br.com.lardopet.util.SchedulerProvider
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