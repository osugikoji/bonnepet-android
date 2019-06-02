package br.com.bonnepet.view.pet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.GenderEnum
import br.com.bonnepet.data.enums.PetBehaviourEnum
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.data.repository.PetRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PetEditViewModel(override val app: Application) : BaseViewModel(app) {

    private val petRepository = PetRepository()

    private val _onPetEditSuccess = MutableLiveData<PetDTO>()
    val onPetEditSuccess: LiveData<PetDTO> = _onPetEditSuccess

    fun editPet(
        petId: String, name: String, breed: String, gender: String, birthDate: String, size: String,
        observations: String, behaviours: String, selectedUriImage: File?
    ) {
        isLoading.value = true
        val bodyImage = buildMultipartBodyImage(selectedUriImage)

        val petDTO = PetDTO(
            "",
            name,
            breed,
            getGenderEnum(gender),
            birthDate,
            getSizeEnum(size),
            observations,
            getPetBehaviourListEnum(behaviours),
            petId
        )

        compositeDisposable.add(
            petRepository.editPet(petDTO)
                .subscribeBy(onSuccess = { petEditDTO ->
                    if (bodyImage != null) {
                        // Faz o upload da imagem
                        compositeDisposable.add(
                            petRepository.uploadPetPicture(petDTO.id, bodyImage)
                                .subscribeBy(onSuccess = { petDTO ->
                                    isLoading.value = false
                                    _onPetEditSuccess.value = petDTO
                                }, onError = {
                                    isLoading.value = false
                                    errorMessage.value = it.error(getApplication())
                                })
                        )
                    } else {
                        isLoading.value = false
                        _onPetEditSuccess.value = petEditDTO
                    }
                }, onError = {
                    isLoading.value = false
                    errorMessage.value = it.error(getApplication())
                })
        )
    }

    private fun getGenderEnum(gender: String): String {
        return when (gender) {
            app.getString(GenderEnum.MALE.description) -> GenderEnum.MALE.name
            else -> GenderEnum.FEMALE.name
        }
    }

    private fun getSizeEnum(size: String): String {
        return when (size) {
            app.getString(PetSizeEnum.SMALL.description) -> PetSizeEnum.SMALL.name
            app.getString(PetSizeEnum.MEDIUM.description) -> PetSizeEnum.MEDIUM.name
            else -> PetSizeEnum.LARGE.name
        }
    }

    private fun getPetBehaviourListEnum(behaviours: String): List<String> {
        val behaviourList: List<String> = behaviours.split(", ")
        val behaviourEnumList = ArrayList<String>()

        behaviourList.forEach { behaviour ->
            when (behaviour) {
                app.getString(PetBehaviourEnum.AGGRESSIVE.description) -> behaviourEnumList.add(PetBehaviourEnum.AGGRESSIVE.name)
                app.getString(PetBehaviourEnum.CONFIDENT.description) -> behaviourEnumList.add(PetBehaviourEnum.CONFIDENT.name)
                app.getString(PetBehaviourEnum.INDEPENDENT.description) -> behaviourEnumList.add(PetBehaviourEnum.INDEPENDENT.name)
                app.getString(PetBehaviourEnum.SHY.description) -> behaviourEnumList.add(PetBehaviourEnum.SHY.name)
                app.getString(PetBehaviourEnum.SOCIABLE.description) -> behaviourEnumList.add(PetBehaviourEnum.SOCIABLE.name)
            }
        }
        return behaviourEnumList
    }

    /**
     *  Constroi [file] no formato MuitipartBody.Part
     */
    private fun buildMultipartBodyImage(file: File?): MultipartBody.Part? {
        return try {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file ?: return null)
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        } catch (e: Exception) {
            return null
        }
    }
}