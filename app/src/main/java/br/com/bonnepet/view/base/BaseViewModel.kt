package br.com.bonnepet.view.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(open val app: Application) : AndroidViewModel(app) {

    /** Disposable utilizado para requisição assíncronas */
    protected val compositeDisposable by lazy { CompositeDisposable() }

    protected val sessionExpired = MutableLiveData<Boolean>()
    fun sessionExpired(): LiveData<Boolean> = sessionExpired

    /** Mensagem de erro */
    protected val errorMessage = MutableLiveData<String>()
    fun errorMessage(): LiveData<String> = errorMessage

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}