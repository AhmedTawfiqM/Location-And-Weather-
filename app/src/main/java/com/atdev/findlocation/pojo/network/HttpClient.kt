package com.atdev.findlocation.pojo.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object HttpClient {

    val okHttpClient: OkHttpClient by lazy {

        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interpetor)
            //.addInterceptor(interceptorLOG)
            .build()
        //
    }

    val interceptorLOG: HttpLoggingInterceptor by lazy {

        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }


    val interpetor: Interceptor by lazy {

        object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {

                //..
                val url = chain.request().url

                val httpUrl = url.newBuilder()
                    .addQueryParameter("appid", RetrofitBuilder.API_KEY)
                    .build()

                val requestBuilder = chain.request().newBuilder().url(httpUrl)
                val request = requestBuilder.build()
                return chain.proceed(request)

            }

        }

    }//....
}