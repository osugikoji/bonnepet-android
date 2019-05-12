package br.com.bonnepet.view.menu.beHost

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.NewHostDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class BeHostViewModel(override val app: Application) : BaseViewModel(app) {

    private val hostRepository = HostRepository()

    private val _onHostRegister = MutableLiveData<Boolean>()
    val onHostRegister: LiveData<Boolean> = _onHostRegister

    fun registerHost(price: String, preferencesSize: String, aboutYou: String) {
        isLoading.value = true
        val hostDTO = NewHostDTO(price, getSizeListEnum(preferencesSize), aboutYou)
        compositeDisposable.add(
            hostRepository.registerHost(hostDTO)
                .subscribeBy(onComplete = {
                    _onHostRegister.value = true
                    isLoading.value = false
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

    private fun getSizeListEnum(behaviours: String): List<String> {
        val sizeList: List<String> = behaviours.split(", ")
        val sizeEnumList = ArrayList<String>()

        sizeList.forEach { behaviour ->
            when (behaviour) {
                app.getString(PetSizeEnum.SMALL.description) -> sizeEnumList.add(PetSizeEnum.SMALL.name)
                app.getString(PetSizeEnum.MEDIUM.description) -> sizeEnumList.add(PetSizeEnum.MEDIUM.name)
                app.getString(PetSizeEnum.LARGE.description) -> sizeEnumList.add(PetSizeEnum.LARGE.name)
            }
        }
        return sizeEnumList
    }
}