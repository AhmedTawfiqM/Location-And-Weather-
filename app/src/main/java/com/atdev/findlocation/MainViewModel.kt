package com.atdev.findlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.atdev.findlocation.pojo.models.RootObject
import com.atdev.findlocation.pojo.repos.WeatherRepo

class MainViewModel : ViewModel() {

    private var lat: MutableLiveData<Int> = MutableLiveData()
    private var lon: Int =0


    val rootObject: LiveData<RootObject> = Transformations
        .switchMap(lat) {

                lat ->
            WeatherRepo.getWeather(lat, lon)
        }


}