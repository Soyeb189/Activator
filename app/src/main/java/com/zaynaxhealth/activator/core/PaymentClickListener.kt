package com.zaynaxhealth.activator.core

interface PaymentClickListener {
    fun onCashPay(paymentType:String)
    fun onBkashPay(paymentType:String)
    fun onNagadPay(paymentType:String)
    fun onCardPay(paymentType:String)
}