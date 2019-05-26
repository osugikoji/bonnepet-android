package br.com.bonnepet.view.host

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class HostDetailsViewModel(override val app: Application) : BaseViewModel(app) {
    private val hostRepository = HostRepository()

    private val _host = MutableLiveData<HostDTO>()
    val host: LiveData<HostDTO> = _host

    private lateinit var hostDTO: HostDTO

    fun initViewModel(hostDTO: HostDTO) {
        this.hostDTO = hostDTO
    }

    fun getHost() {
        isLoading.value = true
        compositeDisposable.add(
            hostRepository.getHost(hostDTO.id.toInt())
                .subscribeBy(onSuccess = { hostDTO ->
                    _host.value = hostDTO
                    isLoading.value = false
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

    fun getBookDetails(): BookingDetailsDTO? =
        _host.value?.bookingDetailsDTO


}
