package br.com.bonnepet.view.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class BaseViewModel(open val app: Application) : AndroidViewModel(app) {

    protected val sessionExpired = MutableLiveData<Boolean>()
    fun sessionExpired(): LiveData<Boolean> = sessionExpired

    /** Mensagem de erro */
    protected val errorMessage = MutableLiveData<String>()
    fun errorMessage(): LiveData<String> = errorMessage
}