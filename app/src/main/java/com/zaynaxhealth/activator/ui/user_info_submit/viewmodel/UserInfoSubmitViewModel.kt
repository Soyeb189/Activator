package com.zaynaxhealth.activator.ui.user_info_submit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.requestmodels.ActivateSubscriptionRequestModel
import com.zaynaxhealth.activator.data.model.responsemodels.BooleanResponse
import com.zaynaxhealth.activator.data.model.responsemodels.PackageResponseModel
import com.zaynaxhealth.activator.data.model.responsemodels.PackageSubscribeResponse
import com.zaynaxhealth.activator.data.model.responsemodels.PatientData
import com.zaynaxhealth.activator.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// val packagesMap = mapOf<String,String>()
class UserInfoSubmitViewModel : ViewModel() {
    val successPatientData = MutableLiveData<PatientData?>()
    val successPackages = MutableLiveData<MutableList<PackageResponseModel.Data>>()
    val successInsurance = MutableLiveData<Boolean?>()
    val successOnlinePayment = MutableLiveData<Boolean>()
    val successCashPayment = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val errorPayment = MutableLiveData<String>()
    val isPhoneNumberInvalid = MutableLiveData<String>()
    val isOtpEmpty = MutableLiveData<String>()
    val progressBar = MutableLiveData<Int>()
    val isFamilyPackage = MutableLiveData<Boolean?>()
    val paymentSelection = MutableLiveData<String>()
    val shouldCall = MutableLiveData<Boolean>()
    val orderDetails = MutableLiveData<PackageSubscribeResponse>()
    val isExistSubscribedPackages = MutableLiveData<BooleanResponse>()

    fun getPatientInfo(token: String, phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        ApiResponse.getResult {
                            apiCall?.getPatientInfo(
//                                token,
                                phoneNumber
                            )!!
                        }
                    }
                    if (response.success) {
                        successPatientData.postValue(response.data)
                    }
                } catch (e: ApiException) {
                    error.postValue(e.localizedMessage)
                }
            }
        }
    }

    fun getPackages() {
        viewModelScope.launch {
            try {
                val response =
                    withContext(Dispatchers.IO) { ApiResponse.getResult { apiCall?.getPackages()!! } }
                successPackages.postValue(response.data)
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
            }
        }
    }

    fun getInsuranceStatus(token: String, packageId: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.getInsuranceStatus(
//                            token,
                            packageId
                        )!!
                    }
                }
                successInsurance.postValue(response.data)
            } catch (e: ApiException) {

            }
        }
    }

    fun isFamilyPackage(token: String, packageId: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.isFamilyPackage(
//                            token,
                            packageId
                        )!!
                    }
                }
                isFamilyPackage.value = response.data
            } catch (e: ApiException) {

            }
        }
    }

    fun paymentSelection(paymentMethod : String){
        paymentSelection.value = paymentMethod
    }

//    fun onlinePayment(token: String, requestModel: ActivateSubscriptionRequestModel?) {
//        viewModelScope.launch {
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    ApiResponse.getResult {
//                        apiCall?.activateSubscription(
////                            token,
//                            requestModel
//                        )!!
//                    }
//                }
//                if (response.success){
//                    if (requestModel?.paymentMethod == ONLINE) {
//                        successOnlinePayment.value = true
//                    }else if (requestModel?.paymentMethod == CASH){
//                        successCashPayment.postValue(true)
//                    }
//                }
//            } catch (e: ApiException) {
//                shouldCall.value = true
//                error.postValue(e.message)
//            }
//        }
//    }


    fun packageSubscriptions(token: String,requestModel: ActivateSubscriptionRequestModel?) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.activateSubscriptionV2(
                            /*token,*/
                            requestModel
                        )!!
                    }
                }

                Log.d("Req",requestModel.toString())

                Log.d("Res",response.toString())

                if (response.success){
                    if (requestModel?.paymentMethod == ONLINE) {
                        Log.d("AAA","Success")
                        /*Log.d("AAA", "order ID : ${response.data.data.orderID}")
                        Log.d("AAA", "orderType : ${response.data.data.orderType}")
                        Log.d("AAA", "paymentMethod : ${response.data.data.paymentMethod}")*/

                        orderDetails.postValue(response)
                    }else if (requestModel?.paymentMethod == CASH){
                        successCashPayment.postValue(true)
                    }
                }
            } catch (e: ApiException) {
                shouldCall.value = true
                errorPayment.postValue(e.message)
                Log.d("AAA","Fail : ${e.message}")
            }
        }
    }


    fun getIsExistSubscribedPackages(mobileNumber: String) {
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
            } catch (e: ApiException) {
                shouldCall.value = true
                errorPayment.postValue(e.message)
                Log.d("AAA","Fail : ${e.message}")
            }
        }
    }
}