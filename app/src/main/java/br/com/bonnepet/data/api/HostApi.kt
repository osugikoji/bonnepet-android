package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.NewHostDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HostApi {

    @POST("hosts/insert")
    fun registerHost(@Body newHostDTO: NewHostDTO): Completable

    @GET("hosts/{id}")
    fun getHost(@Path("id") id: Int): Single<HostDTO>

    @GET("hosts")
    fun getAllHosts(): Single<List<HostDTO>>
}