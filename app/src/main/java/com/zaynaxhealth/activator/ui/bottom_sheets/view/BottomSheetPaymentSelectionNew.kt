package com.zaynaxhealth.activator.ui.bottom_sheets.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.PaymentClickListener
import com.zaynaxhealth.activator.data.model.requestmodels.PaymentUrlRequest
import com.zaynaxhealth.activator.databinding.BottomPaymentSelectionNewBinding
import com.zaynaxhealth.activator.ui.bottom_sheets.adapter.HealthPackageDescriptionAdapter
import com.zaynaxhealth.activator.ui.bottom_sheets.viewmodel.PackageCommissionViewModel
import com.zaynaxhealth.activator.ui.login.viewmodel.LoginViewModel
import com.zaynaxhealth.activator.utilities.*

class BottomSheetPaymentSelectionNew(
    private val clickListener: PaymentClickListener,
    private val packageName: String?,
    private val packagePrice: Int?,
    private val packageId: String?,
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomPaymentSelectionNewBinding
    private lateinit var viewModel: PackageCommissionViewModel

    private var isTermsAndConditionClick : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentInputBottomSheetDialogNew)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomPaymentSelectionNewBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[PackageCommissionViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
        setViews()
        loadPackageDetails()
        observers()
        getPackageDiscountAmount()
    }

    private fun setViews() {

        if (sharedPreference().getString(USER_TYPE).equals("PHARMACY") ||
            sharedPreference().getString(USER_TYPE).equals("FIELDSALES")
        ) {
            binding.ivPayNagad.visibility = VISIBLE
            binding.ivPayWithVisa.visibility = VISIBLE
            binding.ivPayWithBkash.visibility = VISIBLE
            binding.ivPayWithCash.visibility = GONE
        } else {
            binding.ivPayNagad.visibility = VISIBLE
            binding.ivPayWithVisa.visibility = VISIBLE
            binding.ivPayWithBkash.visibility = VISIBLE
            binding.ivPayWithCash.visibility = VISIBLE
        }

        if (sharedPreference().getString(USER_TYPE).equals("FIELDSALES") ||
            sharedPreference().getString(USER_TYPE).equals("SUPERSALES")
        ) {
            binding.tvCommissionTitle.visibility = GONE
            binding.tvCommission.visibility = GONE
        } else if (sharedPreference().getString(USER_TYPE).equals("PHARMACY")) {
            binding.tvCommissionTitle.visibility = VISIBLE
            binding.tvCommission.visibility = VISIBLE
        }

        binding.tvPaySelectionTitle.text = packageName
        binding.tvPackagePrice.text = "BDT $packagePrice"


        binding.termsAndConditionRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            isTermsAndConditionClick = isChecked
        }

        binding.ivPayNagad.setOnClickListener {
            if (isTermsAndConditionClick){
               clickListener.onNagadPay("NAGAD")
            }else{
                Toast.makeText(requireContext(),"Please agree with TermsAndCondition",Toast.LENGTH_SHORT).show()
            }

        }

        binding.ivPayWithBkash.setOnClickListener {
            if (isTermsAndConditionClick){
                clickListener.onBkashPay("BKASH")
            }else{
                Toast.makeText(requireContext(),"Please agree with TermsAndCondition",Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivPayWithVisa.setOnClickListener {
            if (isTermsAndConditionClick){
               clickListener.onCardPay("VISA")
            }else{
                Toast.makeText(requireContext(),"Please agree with TermsAndCondition",Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivPayWithCash.setOnClickListener {
            if (isTermsAndConditionClick){
                clickListener.onCashPay("CASH")
            }else{
                Toast.makeText(requireContext(),"Please agree with TermsAndCondition",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
           dismiss()
        }
    }

    private fun loadPackageDetails() {
        val rvPackageDetails = binding.rvPackageDetails
        rvPackageDetails.setHasFixedSize(true)
        rvPackageDetails.setItemViewCacheSize(20)
        rvPackageDetails.isDrawingCacheEnabled = true
        rvPackageDetails.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        val layoutManager = LinearLayoutManager(
            binding.rvPackageDetails.context, LinearLayoutManager.VERTICAL, false
        )
        rvPackageDetails.layoutManager = layoutManager
        val healthPackageDescriptionAdapter = HealthPackageDescriptionAdapter()
//        healthPackageDescriptionAdapter.setData(packageDescription)
        rvPackageDetails.adapter = healthPackageDescriptionAdapter
    }

    private fun getPackageDiscountAmount(){
        if (packageId != null) {
            viewModel.getPackageDiscountAmount(packageId)
        }
    }

    private fun observers() {

        viewModel.packageDiscountAmount.observe(viewLifecycleOwner) {
            binding.tvCommission.text = "BDT ${it.data.discountAmount}"
            binding.tvGrandTotal.text = "৳ ${it.data.packageDiscountAmount}"
        }

        viewModel.error.observe(this.viewLifecycleOwner) {
            snackBar(requireView(), it)
        }
    }

}