package com.zaynaxhealth.activator.ui.activation_history.view

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.data.model.EventBusHistory
import com.zaynaxhealth.activator.data.model.requestmodels.TransactionIdUpdateRequestModel
import com.zaynaxhealth.activator.data.model.responsemodels.HistoryResponseModel
import com.zaynaxhealth.activator.databinding.AlertHistorySuccessBinding
import com.zaynaxhealth.activator.databinding.FragmentTransactionNumberBinding
import com.zaynaxhealth.activator.ui.activation_history.viewmodel.ActivatedHistoryViewModel
import com.zaynaxhealth.activator.utilities.GONE
import com.zaynaxhealth.activator.utilities.KeyboardUtil
import com.zaynaxhealth.activator.utilities.VISIBLE
import org.greenrobot.eventbus.EventBus

class TransactionNumber(
    private var data: HistoryResponseModel,
    private var viewModel: ActivatedHistoryViewModel,
    private var  refreshActivatedHistory: RefreshActivatedHistory
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTransactionNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_transaction_number,
            container,
            false
        )
        binding.edtTransactionId.setText(data.transactionId)

        observers()

        binding.btnSubmit.setOnClickListener {
            var transctionNumber = binding.edtTransactionId.text.toString().trim()
            if(transctionNumber == ""){
                binding.edtTransactionId.error = "Please enter a valid transaction number"
            }else{
                dialog?.dismiss()

                showDialog(refreshActivatedHistory)
            }


        }

        //For hide or show Keyboard
        KeyboardUtil(activity, binding.root).enable()


        binding.header.tvHeader.text = "Enter Transaction Number"


        binding.header.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }

        if (data.paymentGateway== null){
            binding.btnSubmit.visibility = VISIBLE
            binding.header.tvHeader.text = "Enter Transaction Number"
            binding.edtTransactionId.setEnabled(true)
            binding.edtTransactionId.isClickable = true
            binding.edtTransactionId.isEnabled = true
        }else if (data.paymentGateway == ""){
            binding.btnSubmit.visibility = VISIBLE
            binding.header.tvHeader.text = "Enter Transaction Number"
            binding.edtTransactionId.setEnabled(true)
            binding.edtTransactionId.isClickable = true
            binding.edtTransactionId.isEnabled = true
        }else{
            binding.btnSubmit.visibility = GONE
            binding.header.tvHeader.text = "Transaction Number"
            binding.edtTransactionId.setEnabled(false)
            binding.edtTransactionId.isClickable = false
            binding.edtTransactionId.isEnabled = false
            binding.edtTransactionId.setTypeface(binding.edtTransactionId.getTypeface(), Typeface.BOLD)
            binding.edtTransactionId.setPadding(10,10,10,30)
        }



/*        if (data.transactionId==null){
            binding.btnSubmit.visibility = GONE
            binding.header.tvHeader.text = "Transaction Number"
            binding.edtTransactionId.setEnabled(false)
            binding.edtTransactionId.isClickable = false
            binding.edtTransactionId.isEnabled = false
            binding.edtTransactionId.setTypeface(binding.edtTransactionId.getTypeface(), Typeface.BOLD)
            binding.edtTransactionId.setPadding(10,10,10,30)
        }else if (data.transactionId!= ""){
            binding.btnSubmit.visibility = GONE
            binding.header.tvHeader.text = "Transaction Number"
            binding.edtTransactionId.setEnabled(false)
            binding.edtTransactionId.isClickable = false
            binding.edtTransactionId.isEnabled = false
            binding.edtTransactionId.setTypeface(binding.edtTransactionId.getTypeface(), Typeface.BOLD)
            binding.edtTransactionId.setPadding(10,10,10,30)
        }else{
            binding.btnSubmit.visibility = VISIBLE
            binding.header.tvHeader.text = "Enter Transaction Number"
            binding.edtTransactionId.setEnabled(true)
            binding.edtTransactionId.isClickable = true
            binding.edtTransactionId.isEnabled = true
        }*/



        // Inflate the layout for this fragment
        return binding.root
    }

    private fun observers() {
        viewModel.transactionIdSuccess.observe(viewLifecycleOwner){
            EventBus.getDefault().post(EventBusHistory())
        }
    }

    private fun showDialog(refreshActivatedHistory: RefreshActivatedHistory) {

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog)
        val view = AlertHistorySuccessBinding.inflate(LayoutInflater.from(context))
        alertDialog.setView(view.root)
        val dialog = alertDialog.create()

        view.tvAlertTitle.text = getString(R.string.cashPaymentAlertTitleMessage)
        view.tvAlertSubTitle.text = getString(R.string.cashPaymentAlertSubtitleMessage)
        view.tvPatientName.text = "${data.name}"
        view.tvPatientPhone.text = "${data.phoneNumber}"
        view.tvPackageName.text = "${data.packageName}"
        view.tvTransactionId.text = "Transaction ID: ${binding.edtTransactionId.text}"
        view.tvAmount.text = "BDT ${data.price}"
        view.btnOnlinePayment.visibility = GONE



        view.btnYes.setOnClickListener {
            if (binding.edtTransactionId.text.toString()
                    .trim() == null && binding.edtTransactionId.text.toString().trim() == ""
            ) {

            } else {
                viewModel.setTransactionId(
                    TransactionIdUpdateRequestModel(
                        data.orderId,
                        binding.edtTransactionId.text.toString().trim()
                    )
                )
                if (refreshActivatedHistory!=null)
                refreshActivatedHistory.onItemClick()
                dialog.dismiss()
            }
        }

        view.btnNo.setOnClickListener {
            dialog.dismiss()
        }

//        if (viewModel.paymentSelection.value == ONLINE) {
//            view.tvAlertTitle.text = getString(R.string.onlinePaymentAlertTitleMessage)
//            view.tvAlertSubTitle.text = getString(R.string.onlinePaymentAlertSubTitleMessage)
//            view.btnOnlinePayment.visibility = VISIBLE
//            view.btnCashPayment.visibility = GONE
//        } else if (viewModel.paymentSelection.value == CASH) {
//            view.tvAlertTitle.text = getString(R.string.cashPaymentAlertTitleMessage)
//            view.tvAlertSubTitle.text = getString(R.string.cashPaymentAlertSubtitleMessage)
//            view.btnOnlinePayment.visibility = GONE
//            view.btnCashPayment.visibility = VISIBLE
//        }

        dialog.show()

        view.btnOnlinePayment.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.historyContainerFragment)
        }

    }

    public interface RefreshActivatedHistory{
        fun onItemClick()
    }

}