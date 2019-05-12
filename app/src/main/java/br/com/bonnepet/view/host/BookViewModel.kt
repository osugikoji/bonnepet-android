package br.com.bonnepet.view.host

import Data
import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.BookingRepository
import br.com.bonnepet.data.repository.PetRepository
import br.com.bonnepet.util.extension.parseToDate
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import org.joda.time.Days

class BookViewModel(override val app: Application) : BaseViewModel(app) {

    private val bookRepository = BookingRepository()

    private val petRepository = PetRepository()

    private lateinit var hostDTO: HostDTO

    private val _textNight = MutableLiveData<String>()
    val textNight: LiveData<String> = _textNight

    private val _textTotalPrice = MutableLiveData<String>()
    val textTotalPrice: LiveData<String> = _textTotalPrice

    private val _petList = MutableLiveData<MutableList<PetDTO>>()
    val petList: LiveData<MutableList<PetDTO>> = _petList

    fun initViewModel(intent: Intent) {
        hostDTO = intent.getSerializableExtra(Data.HOST_DTO) as HostDTO
    }

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

    fun calculateTotalPriceAndNight(takeDate: String, getDate: String) {
        if (takeDate.isEmpty() || getDate.isEmpty()) return

        val daysDifference = Days.daysBetween(takeDate.parseToDate(), getDate.parseToDate())

        if (daysDifference.days < 0) {
            _textNight.value = 0.toString()
            _textTotalPrice.value = 0.toString()
            return
        }
        val daysNumber = daysDifference.days

        _textNight.value =
            if (daysNumber > 1) "$daysNumber ${app.getString(R.string.nights)}"
            else "$daysNumber ${app.getString(R.string.night)}"

        _textTotalPrice.value = (daysNumber * getPrice().toInt()).toString()

    }

    fun getHostPicture() =
        hostDTO.profileDTO.profileImageURL

    fun getHostName() =
        hostDTO.profileDTO.userName

    fun getAddress() =
        hostDTO.profileDTO.addressDTO

    fun getPrice() =
        hostDTO.price


}
