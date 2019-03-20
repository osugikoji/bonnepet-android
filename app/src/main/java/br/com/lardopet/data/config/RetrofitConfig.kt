package br.com.lardopet.data.config

import br.com.lardopet.session.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    private const val BASE_URL = "http://192.168.100.3:8080/"


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
            val response = it.proceed(request)
            createUserSessionIfTokenExist(response.header("Authorization"))
            val requestBuilder = request.newBuilder()
//                        .addHeader("Authorization: ", "")
                .method(request.method(), request.body())
            it.proceed(requestBuilder.build())
        }.build()

    private fun createUserSessionIfTokenExist(tokenHeader: String?) {
        if (tokenHeader != null) {
            val token = tokenHeader.substringAfterLast(" ")
            SessionManager.createUserSession(token)
        }
    }

    fun getInstance() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

}