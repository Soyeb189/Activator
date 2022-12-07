package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class DiscountAmountResponseModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: Any,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("activatorType")
        val activatorType: String,
        @SerializedName("discountAmount")
        val discountAmount: Double,
        @SerializedName("discountRate")
        val discountRate: Double,
        @SerializedName("discountType")
        val discountType: String,
        @SerializedName("packageDiscountAmount")
        val packageDiscountAmount: Double,
        @SerializedName("packageName")
        val packageName: PackageName,
        @SerializedName("packageRegularPrice")
        val packageRegularPrice: Double
    ) {
        data class PackageName(
            @SerializedName("bn")
            val bn: String,
            @SerializedName("en")
            val en: String
        )
    }
}