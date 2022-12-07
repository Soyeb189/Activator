package com.zaynaxhealth.activator.ui.user_number_verification.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaynaxhealth.activator.core.ApplicationClass
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.requestmodels.SendOtpRequestModel
import com.zaynaxhealth.activator.data.model.requestmodels.VerifyOtpRequestModel
import com.zaynaxhealth.activator.data.model.responsemodels.BooleanResponse
import com.zaynaxhealth.activator.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserVerificationViewModel:ViewModel() {
    val successSendOtp = MutableLiveData<String>()
    val successOtpVerification = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val isPhoneNumberInvalid = MutableLiveData<String>()
    val isOtpEmpty = MutableLiveData<String>()
    val progressBar = MutableLiveData<Int>()
    val isExistSubscribedPackages = MutableLiveData<BooleanResponse>()


    fun sendOtp(phoneNumber: String) {
        if ((!validatePhoneNumber(phoneNumber) || phoneNumber.trim().isEmpty())){
            isPhoneNumberInvalid.postValue("Please Provide a valid phone number")
        }else{
            progressBar.value = VISIBLE
            viewModelScope.launch {
                try {
                    val reqModel = SendOtpRequestModel(phoneNumber)
                    val response = ApiResponse.getResult {apiCall?.sendOtp(reqModel)!!}
                    if (response.success){
                        successSendOtp.postValue("Otp sent")
                    }else{
                        error.postValue("Something went wrong")
                    }
                    progressBar.value = GONE
                }catch (e: ApiException){
                    error.postValue(e.localizedMessage)
                    progressBar.value = GONE
                }
            }
        }
    }

    fun verifyOtp(phoneNumber: String,otp: String){
        if (otp.isEmpty() || (!validatePhoneNumber(phoneNumber) || phoneNumber.trim().isEmpty())){
            if (otp.isEmpty()){
                isOtpEmpty.postValue("otp required")
            }
            if ((!validatePhoneNumber(phoneNumber) || phoneNumber.trim().isEmpty())){
                isPhoneNumberInvalid.postValue("Please Provide a valid phone number")
            }
        }else{
            progressBar.value = VISIBLE
            viewModelScope.launch {
                try {
                    val reqModel = VerifyOtpRequestModel(otp = otp, phoneNumber = phoneNumber)
                    val response = ApiResponse.getResult {apiCall?.verifyOtp(reqModel)!!}
                    if (response.success){
                        successOtpVerification.postValue("Otp verification successful")
                    }else{
                        error.postValue("Something went wrong")
                    }
                    progressBar.value = GONE
                }catch (e: ApiException){
                    error.postValue(e.localizedMessage)
                    progressBar.value = GONE
                }
            }
        }
    }


    fun getIsExistSubscribedPackages(mobileNumber: String) {
        progressBar.value = VISIBLE
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.getIsExistSubscribedPackages(mobileNumber)!!
                    }
                }
                if (response.success){
                    isExistSubscribedPackages.postValue(response)
                }
                progressBar.value = GONE
            } catch (e: ApiException) {
                progressBar.value = GONE
                error.postValue(e.message)
                Log.d("AAA","Fail : ${e.message}")
            }
        }
    }

}