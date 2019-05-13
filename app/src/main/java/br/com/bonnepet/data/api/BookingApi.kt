package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.NewBookingDTO
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApi {

    @POST("bookings/insert")
    fun insertBooking(@Body newBookingDTO: NewBookingDTO): Completable
}