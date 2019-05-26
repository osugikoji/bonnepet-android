package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.HostBookingDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewBookingDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface BookingApi {

    @POST("bookings/insert")
    fun insertBooking(@Body newBookingDTO: NewBookingDTO): Completable

    @GET("bookings/requested")
    fun getRequestedBookings(): Single<List<HostDTO>>

    @GET("bookings/host")
    fun getBookingsHost(): Single<List<HostBookingDTO>>

    @DELETE("bookings/{id}/cancel")
    fun cancelBooking(@Path("id") id: String): Completable

    @PUT("bookings/{id}/refuse")
    fun refuseBooking(@Path("id") id: String): Single<HostBookingDTO>

    @PUT("bookings/{id}/accept")
    fun acceptBooking(@Path("id") id: String): Single<HostBookingDTO>
}