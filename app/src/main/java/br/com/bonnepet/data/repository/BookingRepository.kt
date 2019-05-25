package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.BookingApi
import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewBookingDTO
import br.com.bonnepet.data.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.create

/** Repositorio de Reservas */
class BookingRepository {

    private val bookingApi = RetrofitConfig.getInstance().create<BookingApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    fun insertBooking(newBookingDTO: NewBookingDTO): Completable {
        return bookingApi.insertBooking(newBookingDTO).compose(schedulerProvider.getSchedulersForCompletable())
    }

    fun getHostBookings(): Single<List<HostBookingDTO>> {
        return bookingApi.getBookingsHost().compose(schedulerProvider.getSchedulersForSingle())
    }

    fun getRequestBookings(): Single<List<HostDTO>> {
        return bookingApi.getRequestedBookings().compose(schedulerProvider.getSchedulersForSingle())
    }

}