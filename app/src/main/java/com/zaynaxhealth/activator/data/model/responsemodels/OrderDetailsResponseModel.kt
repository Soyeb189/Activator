package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class OrderDetailsResponseModel(
    @SerializedName("data")
    val data: Int,
) : BaseResponseModel()