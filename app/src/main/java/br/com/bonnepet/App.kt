package br.com.bonnepet

import android.app.Application
import br.com.bonnepet.config.RetrofitConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        RetrofitConfig.init(this)
        SharedPreferencesUtil.init(this)
    }
}