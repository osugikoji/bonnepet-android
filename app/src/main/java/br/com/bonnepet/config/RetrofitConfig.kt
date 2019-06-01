package br.com.bonnepet.config

import br.com.bonnepet.App
import br.com.bonnepet.R
import br.com.bonnepet.data.util.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    private lateinit var app: App

    fun init(app: App) {
        this.app = app

    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor {
                val request = it.request()
                val requestBuilder = request.newBuilder()
                    .method(request.method(), request.body())
                    .addHeader(Header.AUTHORIZATION, SessionManager.getAuthorizationHeader() ?: "")
                it.proceed(requestBuilder.build())

            }.build()
    }

    fun getInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(app.getString(R.string.server_name))
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}