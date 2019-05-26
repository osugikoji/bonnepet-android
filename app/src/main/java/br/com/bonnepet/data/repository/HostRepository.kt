package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.HostApi
import br.com.bonnepet.data.model.EditHostDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewHostDTO
import br.com.bonnepet.data.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

class HostRepository {

    private val hostApi = RetrofitConfig.getInstance().create<HostApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    fun registerHost(newHostDTO: NewHostDTO): Completable {
        return hostApi.registerHost(newHostDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }

    fun editHost(editHostDTO: EditHostDTO): Completable {
        return hostApi.editHost(editHostDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }

    fun getHost(id: Int): Single<HostDTO> {
        return hostApi.getHost(id).compose(schedulerProvider.getSchedulersForSingle())
    }

    fun getAllHost(): Single<List<HostDTO>> {
        return hostApi.getAllHosts().compose(schedulerProvider.getSchedulersForSingle())
    }
}