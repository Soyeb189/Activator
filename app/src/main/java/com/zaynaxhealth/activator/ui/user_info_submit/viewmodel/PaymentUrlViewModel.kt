package com.zaynaxhealth.activator.ui.user_info_submit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.requestmodels.PaymentUrlRequest
import com.zaynaxhealth.activator.data.model.responsemodels.PackageSubscribeResponse
import com.zaynaxhealth.activator.data.model.responsemodels.PaymentUrlResponse
import com.zaynaxhealth.activator.utilities.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentUrlViewModel : ViewModel() {

    val bkashSuccess = MutableLiveData<PaymentUrlResponse>()
    val nagadSuccess = MutableLiveData<PaymentUrlResponse>()
    val sslSuccess = MutableLiveData<PaymentUrlResponse>()
    val error = MutableLiveData<String>()

    fun getBkashUrl(requestModel: PaymentUrlRequest) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.bkashUrl(
                            requestModel
                        )!!
                    }
                }
                if (response.success){
                    bkashSuccess.postValue(response)
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
                Log.d("Error", "ApiError: $e")
            }
        }
    }

    fun getNagadUrl(requestModel: PaymentUrlRequest) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.nagadUrl(
                            requestModel
                        )!!
                    }
                }
                if (response.success){
                    nagadSuccess.postValue(response)
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
                Log.d("Error", "ApiError: $e")
            }
        }
    }


    fun getSslUrl(requestModel: PaymentUrlRequest) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.sslUrl(
                            requestModel
                        )!!
                    }
                }
                if (response.success){
                    sslSuccess.postValue(response)
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
                Log.d("Error", "ApiError: $e")
            }
        }
    }
}