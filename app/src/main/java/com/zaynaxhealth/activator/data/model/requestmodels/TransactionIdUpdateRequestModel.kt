package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class TransactionIdUpdateRequestModel(
    @SerializedName("orderId")
    val orderId: String, // 62bd67c4fb107622c9c07375
    @SerializedName("transactionId")
    val transactionId: String // 12388
)