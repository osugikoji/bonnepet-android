package br.com.bonnepet.view.host.searchHost

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.util.extension.error
import br.com.bonnepet.view.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy

class SearchHostViewModel(override val app: Application) : BaseViewModel(app) {
    private val hostRepository = HostRepository()

    private val _hostList = MutableLiveData<MutableList<HostDTO>>()
    val hostList: LiveData<MutableList<HostDTO>> = _hostList

    fun getAllHost() {
        isLoading.value = true
        compositeDisposable.add(
            hostRepository.getAllHost()
                .subscribeBy(onSuccess = { hostDTOList ->
                    _hostList.value = hostDTOList.toMutableList()
                    isLoading.value = false
                }, onError = {
                    errorMessage.value = it.error(app)
                    isLoading.value = false
                })
        )
    }

}
