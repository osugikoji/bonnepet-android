package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.EditHostDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewHostDTO
import br.com.bonnepet.data.model.RateDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface HostApi {

    @POST("hosts/insert")
    fun registerHost(@Body newHostDTO: NewHostDTO): Completable

    @PUT("hosts/edit")
    fun editHost(@Body editHostDTO: EditHostDTO): Completable

    @GET("hosts/{id}")
    fun getHost(@Path("id") id: Int): Single<HostDTO>

    @GET("hosts")
    fun getAllHosts(): Single<List<HostDTO>>

    @POST("hosts/rate")
    fun rateHost(@Body rateDTO: RateDTO): Completable
}