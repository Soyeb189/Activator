package com.zaynaxhealth.activator.data.model.requestmodels


data class PaymentUrlRequest(
    val orderID: String,
    val orderType: String
)