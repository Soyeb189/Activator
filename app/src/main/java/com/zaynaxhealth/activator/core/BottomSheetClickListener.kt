package com.zaynaxhealth.activator.core

interface BottomSheetClickListener {
    fun cashPay(orderId: String?)
    fun onlinePay(orderId: String?)
}