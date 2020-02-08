package com.atdev.findlocation.pojo.repos

import androidx.lifecycle.LiveData
import com.atdev.findlocation.pojo.models.Main
import com.atdev.findlocation.pojo.models.RootObject
import com.atdev.findlocation.pojo.network.RetrofitBuilder
import kotlinx.coroutines.*

object WeatherRepo {

    var job: CompletableJob? = null


    fun getWeather(lat: Double, lon: Double): LiveData<RootObject> {

        job = Job()

        return object : LiveData<RootObject>() {

            override fun onActive() {
                super.onActive()

                job?.let { thejob ->

                    CoroutineScope(Dispatchers.IO + thejob).launch {

                        val rootObject = RetrofitBuilder.apiService.getWeather(lat, lon)
                        withContext(Dispatchers.Main) {

                            value = rootObject
                            thejob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJob() {
        job!!.cancel()
    }
}