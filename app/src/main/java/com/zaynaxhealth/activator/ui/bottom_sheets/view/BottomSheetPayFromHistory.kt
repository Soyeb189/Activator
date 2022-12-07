package com.zaynaxhealth.activator.ui.bottom_sheets.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.BottomSheetClickListener
import com.zaynaxhealth.activator.databinding.BottomPaymentSelectionBinding
import com.zaynaxhealth.activator.ui.activation_history.view.CASH
import com.zaynaxhealth.activator.utilities.*
import com.zaynaxhealth.activator.ui.activation_history.viewmodel.ActivatedHistoryViewModel
import com.zaynaxhealth.activator.ui.activation_history.viewmodel.ActivatedHistoryViewModelFactory

class BottomSheetPayFromHistory(private val clickListener: BottomSheetClickListener,private val orderId: String): BottomSheetDialogFragment() {
    private lateinit var binding: BottomPaymentSelectionBinding

    private val viewModel: ActivatedHistoryViewModel by activityViewModels{
        ActivatedHistoryViewModelFactory(sharedPreference().getString(TOKEN)!!, CASH,"","")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentInputBottomSheetDialogNew)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomPaymentSelectionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()
    }

    private fun clickEvents() {
        binding.btnBack.setOnClickListener {
            dialog?.dismiss()
        }

        binding.cvOnlinePayment.setOnClickListener {
            clickListener.onlinePay(orderId)
            dialog?.dismiss()
        }

        binding.cvCashPayment.setOnClickListener {
            clickListener.cashPay(orderId)
            dialog?.dismiss()
        }
    }

}