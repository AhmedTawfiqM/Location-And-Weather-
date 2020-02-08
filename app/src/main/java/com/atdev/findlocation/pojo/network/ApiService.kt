package com.atdev.findlocation.pojo.network

import com.atdev.findlocation.pojo.models.RootObject
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): RootObject

    //
}