package com.zaynaxhealth.activator.utilities

import com.zaynaxhealth.activator.data.api.ApiList
import com.zaynaxhealth.activator.data.api.RetrofitClient
import okhttp3.logging.HttpLoggingInterceptor


val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)



// Test Server
const val BASE_URL = "https://api.zaynax.health" //Test
const val PAYMENT_BASE_URL = "api.zaynax.health" //Test
const val BKASH_BASE_URL = "https://zaynax.health/payment/mobile-app-bkash" //Test

val apiCall = RetrofitClient.retrofit?.create(ApiList::class.java)