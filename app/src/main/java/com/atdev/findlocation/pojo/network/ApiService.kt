package com.atdev.findlocation.pojo.network

import com.atdev.findlocation.pojo.models.RootObject
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    //api.openweathermap.org/data/2.5/weather?lat=35&lon=139

    @GET("weather")
    suspend fun getWeather(
        @Path("lat") lat: Int,
        @Path("lon") lon: Int
    ): RootObject

}