package br.com.bonnepet.view.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.repository.HostRepository
import br.com.bonnepet.view.base.BaseViewModel

class SearchHostViewModel(override val app: Application) : BaseViewModel(app) {
    private val hostRepository = HostRepository()

    private val _hostList = MutableLiveData<MutableList<HostDTO>>()
    val hostList: LiveData<MutableList<HostDTO>> = _hostList

    fun getAllHost() {
        _hostList.value = hostRepository.getAllHost()
    }

}
