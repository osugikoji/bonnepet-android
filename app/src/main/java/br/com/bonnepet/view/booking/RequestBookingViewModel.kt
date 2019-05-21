package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class RequestBookingViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _requestBookingList = MutableLiveData<MutableList<HostDTO>>()
    val bookingDetailsList: LiveData<MutableList<HostDTO>> = _requestBookingList

    fun getRequestBookings() {
        isLoading.value = true
        compositeDisposable.add(
            bookingRepository.getRequestBookings()
                .subscribeBy(onSuccess = { hostDTOList ->
                    _requestBookingList.value = hostDTOList.toMutableList()
                    isLoading.value = false
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }
}