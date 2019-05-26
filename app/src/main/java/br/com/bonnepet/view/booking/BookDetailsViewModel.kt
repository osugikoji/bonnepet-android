package br.com.bonnepet.view.booking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class BookDetailsViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookingRepository = BookingRepository()

    private val _onCancelBooking = MutableLiveData<Boolean>()
    val onCancelBooking: LiveData<Boolean> = _onCancelBooking

    fun cancelBooking(id: String) {
        isLoading.value = true
        compositeDisposable.add(
            bookingRepository.cancelBooking(id)
                .subscribeBy(onComplete = {
                    isLoading.value = false
                    _onCancelBooking.value = true
                }, onError = {
                    _onCancelBooking.value = false
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }
}