package br.com.bonnepet

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(this)
    }
}