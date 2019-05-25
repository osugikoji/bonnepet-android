package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class HostBookingViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _hostBookingList = MutableLiveData<MutableList<HostBookingDTO>>()
    val hostBookingList: LiveData<MutableList<HostBookingDTO>> = _hostBookingList

    fun getHostBookings() {
        isLoading.value = true
        compositeDisposable.add(
            bookingRepository.getHostBookings()
                .subscribeBy(onSuccess = { hostBookDTOList ->
                    _hostBookingList.value = hostBookDTOList.toMutableList()
                    isLoading.value = false
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }
}