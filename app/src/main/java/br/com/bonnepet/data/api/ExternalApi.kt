package br.com.bonnepet.data.api

import br.com.bonnepet.data.model.AddressDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *  Apis de terceiros
 */
interface ExternalApi {

    /**
     * Retorna o endereco atraves do [cep] passado como parametro
     */
    @GET("https://viacep.com.br/ws/{cep}/json/")
    fun getAddress(@Path("cep") cep: String): Single<AddressDTO>
}