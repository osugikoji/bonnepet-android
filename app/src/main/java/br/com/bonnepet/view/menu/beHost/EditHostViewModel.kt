package br.com.bonnepet.view.menu.beHost

import Data
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.EditHostDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class EditHostViewModel(override val app: Application) : BaseViewModel(app) {

    private val hostRepository = HostRepository()

    private val _onEditHost = MutableLiveData<Boolean>()
    val onEditHost: LiveData<Boolean> = _onEditHost

    lateinit var editHostDTO: EditHostDTO

    fun initViewModel(intent: Intent) {
        editHostDTO = intent.getSerializableExtra(Data.EDIT_HOST_DTO) as EditHostDTO
    }

    fun getPrice(): String =
        editHostDTO.price

    fun getPreferenceSize(context: Context): String {
        var sizeList = ""
        editHostDTO.sizeList.forEach { size ->
            val sizeDescription = context.getString(PetSizeEnum.getPetSizeDescription(size)!!)
            sizeList += "$sizeDescription, "
        }
        return sizeList.removeSuffix(", ")
    }

    fun getAboutHost(): String =
        editHostDTO.about


    fun editHost(price: String, preferencesSize: String, aboutYou: String) {
        isLoading.value = true
        editHostDTO.apply {
            this.price = price
            sizeList = getSizeListEnum(preferencesSize)
            about = aboutYou
        }

        compositeDisposable.add(
            hostRepository.editHost(editHostDTO)
                .subscribeBy(onComplete = {
                    _onEditHost.value = true
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