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
import com.zaynaxhealth.activator.ui.user_info_submit.viewmodel.UserInfoSubmitViewModel
import com.zaynaxhealth.activator.utilities.CASH
import com.zaynaxhealth.activator.utilities.ONLINE

class BottomSheetPaySelection(private val clickListener: BottomSheetClickListener): BottomSheetDialogFragment() {
    private lateinit var binding: BottomPaymentSelectionBinding
    private val viewModel : UserInfoSubmitViewModel by activityViewModels()

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
//            viewModel.shouldCall.value = true
            viewModel.paymentSelection(ONLINE)
            clickListener.onlinePay(null)
            dialog?.dismiss()
        }

        binding.cvCashPayment.setOnClickListener {
//            viewModel.shouldCall.value = true
            viewModel.paymentSelection(CASH)
            clickListener.cashPay(null)
            dialog?.dismiss()
        }

    }

}