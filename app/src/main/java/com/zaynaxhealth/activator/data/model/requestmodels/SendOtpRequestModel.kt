package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class SendOtpRequestModel(
    @SerializedName("phoneNumber")
    val phoneNumber: String // 01712345678
)