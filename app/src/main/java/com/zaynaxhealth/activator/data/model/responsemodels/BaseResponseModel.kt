package com.zaynaxhealth.activator.data.model.responsemodels

import com.google.gson.annotations.SerializedName

open class BaseResponseModel {




    @SerializedName("success")
    val isSuccessful: Boolean = false




    @SerializedName("message")
    open val message: List<String>? = null






}