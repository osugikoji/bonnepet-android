package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.RateDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class BookDetailsViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val hostRepository = HostRepository()

    private val _onCancel = MutableLiveData<Boolean>()
    val onCancel: LiveData<Boolean> = _onCancel

    private val _onFinalize = MutableLiveData<Boolean>()
    val onFinalize: LiveData<Boolean> = _onFinalize

    fun cancelBooking(id: String) {
        isLoading.value = true
        compositeDisposable.add(
            bookingRepository.cancelBooking(id)
                .subscribeBy(onComplete = {
                    isLoading.value = false
                    _onCancel.value = true
                }, onError = {
                    _onCancel.value = false
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

    fun finalizeBooking(id: String) {
        isLoading.value = true
        compositeDisposable.add(
            bookingRepository.finalizeBooking(id)
                .subscribeBy(onComplete = {
                    isLoading.value = false
                    _onFinalize.value = true
                }, onError = {
                    _onFinalize.value = false
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

    fun rateHost(rateDTO: RateDTO) {
        isLoading.value = true
        compositeDisposable.add(
            hostRepository.rateHost(rateDTO)
                .subscribeBy(onComplete = {
                }, onError = {
                    errorMessage.value = it.error(app)
                })
        )
    }
}