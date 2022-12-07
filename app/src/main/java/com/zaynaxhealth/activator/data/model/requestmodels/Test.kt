package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("data")
        val `data`: List<Data?>?,
        @SerializedName("perPage")
        val perPage: Any?,
        @SerializedName("total")
        val total: Int?,
        @SerializedName("totalPackages")
        val totalPackages: Int?,
        @SerializedName("totalPages")
        val totalPages: Int?,
        @SerializedName("totalSales")
        val totalSales: Double?
    ) {
        data class Data(
            @SerializedName("createdOn")
            val createdOn: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("orderType")
            val orderType: String?,
            @SerializedName("packageName")
            val packageName: String?,
            @SerializedName("paymentGateway")
            val paymentGateway: String?,
            @SerializedName("paymentMethod")
            val paymentMethod: String?,
            @SerializedName("paymentStatus")
            val paymentStatus: String?,
            @SerializedName("phoneNumber")
            val phoneNumber: String?,
            @SerializedName("price")
            val price: Double?,
            @SerializedName("subscriptionId")
            val subscriptionId: String?,
            @SerializedName("transactionId")
            val transactionId: String?
        )
    }
}