package com.zaynaxhealth.activator.data.api

import android.util.Log
import com.zaynaxhealth.activator.core.ApplicationClass
import com.zaynaxhealth.activator.utilities.AUTHORIZATION
import com.zaynaxhealth.activator.utilities.TOKEN
import com.zaynaxhealth.activator.utilities.sharedPreference
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.d("AAA",originalRequest.url.toString())
        if (sharedPreference(ApplicationClass.AppContext!!).getString(TOKEN) != null
            && (!originalRequest.url.toString().contains("auth")
                    || !originalRequest.url.toString().contains("health-packages")
                    || !originalRequest.url.toString().contains("payment_service")
                    || !originalRequest.url.toString().contains("activate_V2"))){
            val builder = originalRequest.newBuilder()
            builder.addHeader(AUTHORIZATION,sharedPreference(ApplicationClass.AppContext!!).getString(TOKEN)!!)
            val request = builder.build()
            return chain.proceed(request)
        }
        return chain.proceed(originalRequest)
    }
}