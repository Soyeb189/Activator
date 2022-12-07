package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class ActivatorInfoResponseModel(
    @SerializedName("activatorName")
    val activatorName: String, // Wilson Fisk
    @SerializedName("area")
    val area: String, // Gulshan-1
    @SerializedName("city")
    val city: String, // Dhaka
    @SerializedName("country")
    val country: String, // Bangladesh
    @SerializedName("email")
    val email: Any, // null
    @SerializedName("phoneNumber")
    val phoneNumber: String, // 01747473856
    @SerializedName("region")
    val region: String, // Dhaka
    @SerializedName("activatorType")
    val activatorType: String // PHARMACY
)