package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.view.base.BaseViewModel

class RequestBookingViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _requestBookingList = MutableLiveData<MutableList<BookingDetailsDTO>>()
    val bookingDetailsList: LiveData<MutableList<BookingDetailsDTO>> = _requestBookingList

    fun getRequestBookings() {
        _requestBookingList.value = bookingRepository.getRequestBookings()
    }
}