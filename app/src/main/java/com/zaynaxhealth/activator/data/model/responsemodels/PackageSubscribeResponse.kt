package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class PackageSubscribeResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: Any,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("orderID")
        val orderID: String,
        @SerializedName("orderType")
        val orderType: String,
        @SerializedName("paymentMethod")
        val paymentMethod: String
    )
}