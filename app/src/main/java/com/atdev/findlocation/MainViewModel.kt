package com.atdev.findlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.atdev.findlocation.pojo.models.RootObject
import com.atdev.findlocation.pojo.repos.WeatherRepo

class MainViewModel : ViewModel() {

    private var lat: MutableLiveData<Double> = MutableLiveData()
    private var lon: MutableLiveData<Double> = MutableLiveData()


    val rootObject: LiveData<RootObject> = Transformations
        .switchMap(lat) {

                lat ->
            WeatherRepo.getWeather(lat, lon.value!!)
        }


    fun setLatLon(lat: Double, lon: Double) {

        val updateLat = lat
        val updateLon = lon

        if (this.lat.value != updateLat)
            this.lat.value = updateLat

        if (this.lon.value != updateLon)
            this.lon.value = updateLon


    }

    fun cancelJob() {
        WeatherRepo.cancelJob()
    }
}