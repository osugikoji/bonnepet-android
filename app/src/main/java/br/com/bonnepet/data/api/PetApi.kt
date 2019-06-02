package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.PetDTO
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface PetApi {

    /**
     * Faz o cadastro de Pet
     */
    @POST("pets/insert")
    fun registerPet(@Body petDTO: PetDTO): Single<String>

    @PUT("pets/updatePet")
    fun editPet(@Body petDTO: PetDTO): Single<PetDTO>

    /**
     *  Faz o upload da imagem, sendo [id] o id do pet e [filePart] a imagem
     */
    @Multipart
    @POST("pets/{id}/picture")
    fun uploadPetPicture(@Path("id") id: String?, @Part filePart: MultipartBody.Part): Single<PetDTO>

    @GET("pets")
    fun getAllPets(): Single<List<PetDTO>>
}