package com.zaynaxhealth.activator.ui.activation_history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActivatedHistoryViewModelFactory(
    private val token: String,
    private val type: String,
    private val fromDate: String,
    private val toDate: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActivatedHistoryViewModel(token, type, fromDate, toDate) as T
    }
}