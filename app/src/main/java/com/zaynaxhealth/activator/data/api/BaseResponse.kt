package com.zaynaxhealth.activator.data.api

import com.google.gson.annotations.SerializedName

data class BaseResponse<T> (
    @SerializedName("success")
    val success: Boolean,
    val data: T,
    @SerializedName("message")
    val message: String,
)