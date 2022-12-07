package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class AvailablePaymentGatewayResponseModel(
    @SerializedName("data")
    val data: List<String>,
) : BaseResponseModel()