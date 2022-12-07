package com.zaynaxhealth.activator.data.model.responsemodels

import com.google.gson.annotations.SerializedName
import java.util.*

class HistoryResponseModel(
    var id: String,
    var subscriptionId: String,
    var name: String,
    var phoneNumber: String,
    var orderId: String,
    var orderType: String,
    var price: Double,
    var packageName: String,
    var createdOn: Date,
    var paymentMethod: String,
    var paymentGateway: String,
    var transactionId: String,
)

data class ActivatedHistoryList(
    @SerializedName("data")
    val data: MutableList<HistoryResponseModel>,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("totalPackages")
    val totalPackages: Int,
    @SerializedName("totalSales")
    val totalSales: Double,
    )