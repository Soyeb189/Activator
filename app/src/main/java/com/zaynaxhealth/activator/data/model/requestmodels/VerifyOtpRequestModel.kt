package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class VerifyOtpRequestModel(
    @SerializedName("otp")
    val otp: String, // 123456
    @SerializedName("phoneNumber")
    val phoneNumber: String // 01712345678
)