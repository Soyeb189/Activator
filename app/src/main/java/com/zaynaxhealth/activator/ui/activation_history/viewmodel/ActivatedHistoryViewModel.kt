package com.zaynaxhealth.activator.ui.activation_history.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.requestmodels.PaymentUrlRequest
import com.zaynaxhealth.activator.data.model.requestmodels.TransactionIdUpdateRequestModel
import com.zaynaxhealth.activator.data.model.responsemodels.HistoryResponseModel
import com.zaynaxhealth.activator.data.model.responsemodels.PaidHistoryResponseModel
import com.zaynaxhealth.activator.data.model.responsemodels.TransactionIdUpdateResponseModel
import com.zaynaxhealth.activator.data.repository.ActivatedPagingSource
import com.zaynaxhealth.activator.utilities.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivatedHistoryViewModel(
    private val token: String,
    private val type: String,
    private val fromDate: String,
    private val toDate: String
) : ViewModel() {

    val paymentSelection = MutableLiveData<String>()
    val shouldCall = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val successCash = MutableLiveData<Boolean>()
    val successResend = MutableLiveData<Boolean>()
    val paidHistorySuccess = MutableLiveData<PaidHistoryResponseModel>()
    val transactionIdSuccess = MutableLiveData<TransactionIdUpdateResponseModel>()

    fun getActivatedHistory(
        token: String,
        type: String,
        fromDate: String,
        toDate: String
    ): Flow<PagingData<HistoryResponseModel>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ActivatedPagingSource(token, type, fromDate, toDate) }
        ).flow
    }

    fun changePaymentMethod(token: String, orderId: String) {
        viewModelScope.launch {
            try {
                val res = apiCall?.changePaymentMethod(orderId)
                if (res?.isSuccessful!!) {
                    successCash.postValue(true)
                } else {
                    error.postValue(res.message())
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
            }
        }
    }

    fun resendPaymentUrl(token: String, orderId: String) {
        viewModelScope.launch {
            try {
                val res = apiCall?.resendPaymentUrl(orderId)
                if (res?.isSuccessful!!) {
                    successResend.postValue(true)
                } else {
                    error.postValue(res.message())
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
            }
        }
    }

    fun getPaidHistory(token: String, page: Int,fromDate:String,toDate: String) {

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.getActivationHistoryPaid(token, page, fromDate, toDate)!!
                    }
                }
                if (response.success) {
                    paidHistorySuccess.postValue(response)
                }
            } catch (e: ApiException) {
                error.postValue(e.message)
                Log.d("AAA", "Fail : ${e.message}")
            }
        }
    }

    fun setTransactionId(requestModel: TransactionIdUpdateRequestModel) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.setTranasctionNumber(
                            requestModel
                        )!!
                    }
                }
                if (response.success){
                    transactionIdSuccess.postValue(response)
                }
            } catch (e: ApiException) {
                error.postValue(e.localizedMessage)
                Log.d("Error", "ApiError: $e")
            }
        }
    }
}