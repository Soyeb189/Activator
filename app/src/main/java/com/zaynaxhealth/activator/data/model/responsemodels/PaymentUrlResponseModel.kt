package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class PaymentUrlResponseModel(
    @SerializedName("data")
    val data: String,
) : BaseResponseModel()