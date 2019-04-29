package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.RequestBookingDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.view.base.BaseViewModel

class RequestBookingViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _requestBookingList = MutableLiveData<MutableList<RequestBookingDTO>>()
    val requestBookingList: LiveData<MutableList<RequestBookingDTO>> = _requestBookingList

    fun getRequestBookings() {
        _requestBookingList.value = bookingRepository.getRequestBookings()
    }
}