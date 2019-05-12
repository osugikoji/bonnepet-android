package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.NewHostDTO
import br.com.bonnepet.data.model.PetDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HostApi {

    @POST("hosts/insert")
    fun registerHost(@Body newHostDTO: NewHostDTO): Completable

    @GET("hosts")
    fun getAllHosts(): Single<List<PetDTO>>
}