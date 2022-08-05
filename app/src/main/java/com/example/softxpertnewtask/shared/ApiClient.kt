package com.example.softxpertnewtask.shared

import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private var retrofit: RetrofitService? = null

        private var authenticated = false

        fun getRetrofitService(): RetrofitService {
            if(retrofit==null || authenticated){
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(interceptor)
                httpClient.readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/")
                    .client(httpClient.build())
                    .addCallAdapterFactory(
                        CoroutinesResponseCallAdapterFactory.create()
                    ).build().create(RetrofitService::class.java)

                authenticated=false

            }
            return retrofit!!

        }
    }

}