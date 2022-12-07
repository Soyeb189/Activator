package com.zaynaxhealth.activator.data.api

import com.google.gson.GsonBuilder
import com.zaynaxhealth.activator.utilities.BASE_URL
import com.zaynaxhealth.activator.utilities.logger
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    val gson = GsonBuilder().setLenient().create()
    val client = OkHttpClient
        .Builder()
        .addInterceptor(logger)
        .addInterceptor(TokenInterceptor())
        .build()

    var retrofit: Retrofit? = null
        get() {
            if (field == null) {
                retrofit = Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return field
        }
}