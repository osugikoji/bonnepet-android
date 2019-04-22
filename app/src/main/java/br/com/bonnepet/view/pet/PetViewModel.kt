package br.com.bonnepet.view.pet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.PetRepository
import br.com.bonnepet.view.base.BaseViewModel

class PetViewModel(override val app: Application) : BaseViewModel(app) {

    private val petRepository = PetRepository()

    private val _petList = MutableLiveData<MutableList<PetDTO>>()
    val petList: LiveData<MutableList<PetDTO>> = _petList

    fun getAllPets() {
        _petList.value = petRepository.getAllPets()
    }
}