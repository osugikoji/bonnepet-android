package br.com.bonnepet.view.host

import Data
import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.BookingDetailsDTO
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class HostDetailsViewModel(override val app: Application) : BaseViewModel(app) {
    private val hostRepository = HostRepository()

    private val _host = MutableLiveData<Boolean>()
    val host: LiveData<Boolean> = _host

    lateinit var hostDTO: HostDTO
        private set

    fun initViewModel(intent: Intent) {
        hostDTO = intent.getSerializableExtra(Data.HOST_DTO) as HostDTO
    }

    fun getHostImage(): String? =
        hostDTO.profileDTO.profileImageURL

    fun getHostAddress(): String {
        return "${hostDTO.profileDTO.addressDTO.street}, ${hostDTO.profileDTO.addressDTO.number}\n" +
                "${hostDTO.profileDTO.addressDTO.district}\n" +
                "${hostDTO.profileDTO.addressDTO.city} - ${hostDTO.profileDTO.addressDTO.state}"
    }

    fun getHostAbout(): String =
        hostDTO.about

    fun getHostTelephone(): String =
        hostDTO.profileDTO.cellphone

    fun getHostCellPhone(): String =
        hostDTO.profileDTO.cellphone

    fun getHostSizePreference(): String {
        var petSize = ""
        hostDTO.sizePreferenceList.forEach { size ->
            petSize += "${getApplication<Application>().getString(PetSizeEnum.getDescription(size)!!)}, "
        }
        return petSize.removeSuffix(", ")
    }

    fun getHostPrice(): String =
        hostDTO.price

    fun isHostImageEmpty(): Boolean =
        hostDTO.profileDTO.profileImageURL.isNullOrEmpty()

    fun getHostName(): String =
        hostDTO.profileDTO.userName

    fun hostHasPet(): Boolean =
        hostDTO.petDTO.isNotEmpty()

    fun getAllPetHost(): List<PetDTO> =
        hostDTO.petDTO

    fun getHost() {
        isLoading.value = true
        compositeDisposable.add(
            hostRepository.getHost(hostDTO.id.toInt())
                .subscribeBy(onSuccess = { hostDTO ->
                    this.hostDTO = hostDTO
                    _host.value = true
                    isLoading.value = false
                }, onError = {
                    _host.value = false
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

    fun getBookDetails(): BookingDetailsDTO? =
        hostDTO.bookingDetailsDTO

}
