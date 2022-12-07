package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class PaymentUrlRequestModel(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("paymentOption")
    val paymentOption: String
)