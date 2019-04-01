package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.UserDTO
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApi {

    /**
     * Faz o cadastro do usuario
     */
    @POST("users/insert")
    fun registerUser(@Body userDTO: UserDTO): Single<UserDTO>

    /**
     *  Faz o upload da imagem, sendo [id] o id do usuario e [filePart] a imagem
     */
    @Multipart
    @POST("users/{id}/picture")
    fun uploadProfilePicture(@Path("id") id: String?, @Part filePart: MultipartBody.Part): Completable
}