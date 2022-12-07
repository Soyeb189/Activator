package com.zaynaxhealth.activator.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.api.BaseResponse
import com.zaynaxhealth.activator.data.model.requestmodels.SignInWithEmailRequestModel
import com.zaynaxhealth.activator.data.model.requestmodels.SignInWithPhoneNumberRequestModel
import com.zaynaxhealth.activator.data.model.responsemodels.ActivatorInfoResponseModel
import com.zaynaxhealth.activator.utilities.GONE
import com.zaynaxhealth.activator.utilities.VISIBLE
import com.zaynaxhealth.activator.utilities.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel: ViewModel() {
    val success = MutableLiveData<BaseResponse<String>>()
    val successActivatorInfo = MutableLiveData<ActivatorInfoResponseModel?>()
    val error = MutableLiveData<String>()
    val isUsernameEmpty = MutableLiveData<String>()
    val isPasswordEmpty = MutableLiveData<String>()
    val progressBar = MutableLiveData<Int>()

    fun login(username: String,password: String){
        if (isValidInput(username,password)){
            progressBar.value = VISIBLE
            if (username.contains("@")){
                viewModelScope.launch {
                    try {
                        val reqModel = SignInWithEmailRequestModel(username,password)
                        val response = ApiResponse.getResult { apiCall?.signInWithEmail(reqModel)!!}
                        success.postValue(response)
                        progressBar.value = GONE
                    }catch (e:ApiException){
                        error.postValue(e.localizedMessage)
                        progressBar.value = GONE
                    }
                }
            }else{
                viewModelScope.launch {
                    try {
                        val reqModel = SignInWithPhoneNumberRequestModel(username,password)
                        val response = ApiResponse.getResult { apiCall?.signInWithPhone(reqModel)!! }
                        success.postValue(response)
                        progressBar.value = GONE
                    }catch (e:ApiException){
                        error.postValue(e.localizedMessage)
                        progressBar.value = GONE
                    }
                }
            }
        }
    }

    fun getActivatorInfo(token:String){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){ApiResponse.getResult { apiCall?.getActivatorInfo(/*token*/)!! }}
                if (response.success){
                    successActivatorInfo.postValue(response.data)
                }
            }catch (e:ApiException){
                error.postValue(e.localizedMessage)
            }
        }
    }

    private fun isValidInput(username: String,password: String): Boolean{
        if (username.isEmpty() || password.isEmpty()){
            if (username.isEmpty()){
                isUsernameEmpty.postValue("This field required")
            }
            if (password.isEmpty()){
                isPasswordEmpty.postValue("This field required")
            }
            return false
        }
        return true
    }
}