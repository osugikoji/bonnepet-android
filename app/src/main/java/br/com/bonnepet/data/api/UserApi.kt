package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.UserDTO
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    /**
     * Faz o cadastro do usuario
     */
    @POST("users/insert-user")
    fun registerUser(@Body userDTO: UserDTO): Completable
}