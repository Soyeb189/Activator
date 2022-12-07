package com.zaynaxhealth.activator.data.model.responsemodels

import com.google.gson.annotations.SerializedName

data class PaymentUrlResponse(
    @SerializedName("data")
    val data : String,
    @SerializedName("message")
    val message: Any,
    @SerializedName("success")
    val success: Boolean
)
