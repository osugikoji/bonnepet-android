package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class BookRequestViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _hostBooking = MutableLiveData<HostBookingDTO>()
    val hostBooking: LiveData<HostBookingDTO> = _hostBooking

    fun refuseBooking(id: String) {
        isLoading.value = true

        compositeDisposable.add(
            bookingRepository.refuseBooking(id)
                .subscribeBy(onSuccess = {
                    isLoading.value = false
                    _hostBooking.value = it
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }


}
