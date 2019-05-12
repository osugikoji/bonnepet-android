package br.com.bonnepet.data.repository

import br.com.bonnepet.config.RetrofitConfig
import br.com.bonnepet.data.api.PetApi
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.create

/** Repositorio de pet */
class PetRepository {

    private val petApi = RetrofitConfig.getInstance().create<PetApi>()
    private val schedulerProvider = SchedulerProvider(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )

    fun getAllPets(): Single<List<PetDTO>> {
        return petApi.getAllPets().compose(schedulerProvider.getSchedulersForSingle())
    }

    fun registerPet(petDTO: PetDTO): Single<String> {
        return petApi.registerPet(petDTO).compose(schedulerProvider.getSchedulersForSingle())
    }

    fun uploadPetPicture(id: String?, file: MultipartBody.Part): Completable {
        return petApi.uploadPetPicture(id, file).compose(schedulerProvider.getSchedulersForCompletable())
    }
}