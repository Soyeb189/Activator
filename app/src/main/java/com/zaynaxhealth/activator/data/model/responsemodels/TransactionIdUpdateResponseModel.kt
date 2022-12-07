package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class TransactionIdUpdateResponseModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: Any, // null
    @SerializedName("success")
    val success: Boolean // true
) {
    data class Data(
        @SerializedName("createdOn")
        val createdOn: String, // 2022-06-30T09:06:58.833Z
        @SerializedName("id")
        val id: String, // 62bd67b26eb816b5939afd7c
        @SerializedName("name")
        val name: String, // Nasrin Akter
        @SerializedName("orderId")
        val orderId: String, // 62bd67c4fb107622c9c07375
        @SerializedName("orderType")
        val orderType: String, // HEALTH_PACKAGE
        @SerializedName("packageName")
        val packageName: String, // Zaynax Nirvor - Silver (1 month)
        @SerializedName("paymentGateway")
        val paymentGateway: String,
        @SerializedName("paymentMethod")
        val paymentMethod: String, // Cash
        @SerializedName("phoneNumber")
        val phoneNumber: String, // 01929298674
        @SerializedName("price")
        val price: Double, // 10
        @SerializedName("subscriptionId")
        val subscriptionId: String, // 109B
        @SerializedName("transactionId")
        val transactionId: String // 12388
    )
}