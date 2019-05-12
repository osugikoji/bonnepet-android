package br.com.bonnepet.view.pet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.PetRepository
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class PetViewModel(override val app: Application) : BaseViewModel(app) {

    private val petRepository = PetRepository()

    private val _petList = MutableLiveData<MutableList<PetDTO>>()
    val petList: LiveData<MutableList<PetDTO>> = _petList

    fun getAllPets() {
        isLoading.value = true

        compositeDisposable.add(
            petRepository.getAllPets()
                .subscribeBy(onSuccess = { petList ->
                    _petList.value = petList.toMutableList()
                    isLoading.value = false
                }, onError = {
                    isLoading.value = false
                })
        )
    }
}