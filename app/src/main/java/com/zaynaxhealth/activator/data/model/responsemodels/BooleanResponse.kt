package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class BooleanResponse(
    @SerializedName("data")
    val `data`: Boolean,
    @SerializedName("message")
    val message: Any,
    @SerializedName("success")
    val success: Boolean
)