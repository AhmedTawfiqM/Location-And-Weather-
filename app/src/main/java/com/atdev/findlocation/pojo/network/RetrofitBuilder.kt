package com.atdev.findlocation.pojo.network

import com.atdev.findlocation.pojo.network.HttpClient.okHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    const val BASE_URL = "https://samples.openweathermap.org/data/2.5/"
    const val API_KEY = "05a468bfd2d568fe3270f012498bfd0a"

    val retrofitBuilder: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

    }


    val apiService: ApiService by lazy {

        retrofitBuilder.build()
            .create(ApiService::class.java)
    }


}