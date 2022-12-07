package com.zaynaxhealth.activator.ui.bottom_sheets.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.responsemodels.DiscountAmountResponseModel
import com.zaynaxhealth.activator.utilities.GONE
import com.zaynaxhealth.activator.utilities.VISIBLE
import com.zaynaxhealth.activator.utilities.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PackageCommissionViewModel : ViewModel() {

    val error = MutableLiveData<String>()
    val progressBar = MutableLiveData<Int>()
    val packageDiscountAmount = MutableLiveData<DiscountAmountResponseModel>()


    fun getPackageDiscountAmount(packageId: String) {
        progressBar.value = VISIBLE
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiResponse.getResult {
                        apiCall?.getPackageDiscountAmount(packageId)!!
                    }
                }
                if (response.success) {
                    packageDiscountAmount.postValue(response)
                }
                progressBar.value = GONE
            } catch (e: ApiException) {
                progressBar.value = GONE
                error.postValue(e.message)
                Log.d("AAA", "Fail : ${e.message}")
            }
        }
    }

}