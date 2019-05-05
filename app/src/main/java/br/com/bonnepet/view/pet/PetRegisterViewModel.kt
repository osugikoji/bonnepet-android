package br.com.bonnepet.view.pet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.PetRepository
import br.com.bonnepet.view.base.BaseViewModel
import java.io.File

class PetRegisterViewModel(override val app: Application) : BaseViewModel(app) {

    private val petRepository = PetRepository()

    private val petAllergies: Array<String>

    /**
     *  Flag pra informar o resultado da requisicao de cadastro
     */
    private val _petRegisterRequestResult = MutableLiveData<Boolean>()
    val petRegisterRequestResult: LiveData<Boolean> = _petRegisterRequestResult


    init {
        petAllergies = petRepository.getPetAllergies()
    }

    fun registerPet(petDTO: PetDTO, selectedUriImage: File?) {
        isLoading.value = true
        petRepository.petRegister(petDTO)
        isLoading.value = false
        _petRegisterRequestResult.value = true

    }

    fun getAllergies(): Array<String> {
        return petAllergies
    }

    fun getBehaviours(): Array<String> {
        return petAllergies
    }
}