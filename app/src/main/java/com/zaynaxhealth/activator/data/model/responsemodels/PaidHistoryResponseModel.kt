package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class PaidHistoryResponseModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: Any, // null
    @SerializedName("success")
    val success: Boolean // true
) {
    data class Data(
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("perPage")
        val perPage: Any, // null
        @SerializedName("total")
        val total: Int, // 1
        @SerializedName("totalPackages")
        val totalPackages: Int, // 1
        @SerializedName("totalPages")
        val totalPages: Int, // 1
        @SerializedName("totalSales")
        val totalSales: Double // 249
    ) {
        data class Data(
            @SerializedName("createdOn")
            val createdOn: String, // 2022-07-04T12:23:43.275Z
            @SerializedName("id")
            val id: String, // 62c2dbcf6f3ebaeac38cd702
            @SerializedName("name")
            val name: String, // Hasibur Rahman
            @SerializedName("orderId")
            val orderId: String, // 62c2dbcf3c8b14232854024a
            @SerializedName("orderType")
            val orderType: String, // HEALTH_PACKAGE
            @SerializedName("packageName")
            val packageName: String, // Zaynax Nirvor - Silver (1 Month)
            @SerializedName("paymentGateway")
            val paymentGateway: String,
            @SerializedName("paymentMethod")
            val paymentMethod: String, // Cash
            @SerializedName("phoneNumber")
            val phoneNumber: String, // 01400525638
            @SerializedName("price")
            val price: Double, // 249
            @SerializedName("subscriptionId")
            val subscriptionId: String // 10EA
        )
    }
}