package com.zaynaxhealth.activator.ui.user_info_submit.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.*
import com.zaynaxhealth.activator.data.model.EventBusHistory
import com.zaynaxhealth.activator.data.model.EventDrawer
import com.zaynaxhealth.activator.data.model.SuccessFail
import com.zaynaxhealth.activator.data.model.requestmodels.ActivateSubscriptionRequestModel
import com.zaynaxhealth.activator.data.model.requestmodels.PaymentUrlRequest
import com.zaynaxhealth.activator.data.model.responsemodels.PackageResponseModel
import com.zaynaxhealth.activator.data.model.responsemodels.PatientData
import com.zaynaxhealth.activator.databinding.AlertPaymentSuccessBinding
import com.zaynaxhealth.activator.databinding.FragmentUserInfoSubmitBinding
import com.zaynaxhealth.activator.ui.bottom_sheets.view.BottomSheetPaySelection
import com.zaynaxhealth.activator.ui.bottom_sheets.view.BottomSheetPaymentSelectionNew
import com.zaynaxhealth.activator.ui.user_info_submit.viewmodel.PaymentUrlViewModel
import com.zaynaxhealth.activator.ui.user_info_submit.viewmodel.UserInfoSubmitViewModel
import com.zaynaxhealth.activator.utilities.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

private val genderArrayList = arrayListOf("Please select gender", "Male", "Female", "Other")
private val verificationTypeArrayList =
    arrayListOf("Please select type", "National ID Card", "Smart Card", "Passport")
private val relationshipArrayList =
    arrayListOf("Please select type", "Father", "Mother", "Sibling", "Spouse", "Child", "Others")

class UserInfoSubmitFragment : Fragment(),
    DatePickerFragment.DatePickListener,
    AdapterView.OnItemSelectedListener,
    BottomSheetClickListener,
    PaymentClickListener {
    private lateinit var binding: FragmentUserInfoSubmitBinding
    private val viewModel: UserInfoSubmitViewModel by activityViewModels()
    private val paymentViewModel: PaymentUrlViewModel by activityViewModels()
    private var mobileNumber: String? = null
    private var gender: String? = ""
    private var verificationType: String? = null
    private var relationship: String? = null
    private var packageId: String? = null
    private var packageName: String? = null
    private var paymentMethod: String? = null
    private var packagePrice: Int? = null
    private var orderId: String? = null
    private var orderType: String? = null
    private var payment: String? = null

    private lateinit var bottomSheet: BottomSheetPaymentSelectionNew

    //    private var packageDescription: ArrayList<String?>? = null
    private var shouldShowDialog: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoSubmitBinding.inflate(layoutInflater, container, false)
        activity?.viewModelStore?.clear()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[UserInfoSubmitViewModel::class.java]
        mobileNumber = requireArguments().getString(MOBILE_NUMBER)
        spGender()
        spDocumentType()
        spRelationship()
        observers()
        clickEvents()



        if (mobileNumber!!.isNotEmpty()) {
            viewModel.getPatientInfo(sharedPreference().getString(TOKEN)!!, mobileNumber!!)
            binding.etContactNo.setText(mobileNumber)
        }
        viewModel.getPackages()

        binding.etActivatedBy.setText(sharedPreference().getString(ACTIVATOR_NAME))
    }

    private fun clickEvents() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.userNumberVerificationFragment)
        }

        binding.ivDrawer.setOnClickListener {
            EventBus.getDefault().post(EventDrawer())
        }

        binding.btnSubmitInfo.setOnClickListener {
            shouldShowDialog = true
            submitInfo()

        }

        binding.tvDobVal.setOnClickListener {
            DatePickerFragment.showDialog(
                childFragmentManager,
                getString(R.string.date_of_birth_tittle),
                this,
                binding.tvDobVal.text?.trim().toString(),
                true
            )
        }
    }

    private fun observers() {
        viewModel.successPatientData.observe(viewLifecycleOwner) {
            setPatientInfo(it)
        }

        viewModel.successInsurance.observe(viewLifecycleOwner) {
//            if (it== true){
//                binding.tvMobileNumber.text = colorMyText(resources.getString(R.string.mobile_number),13,14,
//                    Color.parseColor("#ea0663"))
//                binding.tvRelationship.text = colorMyText(resources.getString(R.string.relationship_asterisk),12,13,Color.parseColor("#ea0663"))
//                binding.tvNomineeName.text = colorMyText(resources.getString(R.string.nominee_name_asterisk),12,13,Color.parseColor("#ea0663"))
//            }else{
            binding.tvMobileNumber.text = resources.getString(R.string.nominee_mobile_number)
            binding.tvRelationship.text = resources.getString(R.string.relationship)
            binding.tvNomineeName.text = resources.getString(R.string.nominee_name)
//            }
        }

        viewModel.successPackages.observe(viewLifecycleOwner) {
            spPackages(it)
        }

        viewModel.isFamilyPackage.observe(this.viewLifecycleOwner) {
            if (it == true) {
                binding.tvSpouseName.visibility = VISIBLE
                binding.etSpouseName.visibility = VISIBLE
                binding.tvChildName1.visibility = VISIBLE
                binding.etChildName1.visibility = VISIBLE
                binding.tvChildName2.visibility = VISIBLE
                binding.etChildName2.visibility = VISIBLE
            } else {
                binding.tvSpouseName.visibility = GONE
                binding.etSpouseName.visibility = GONE
                binding.tvChildName1.visibility = GONE
                binding.etChildName1.visibility = GONE
                binding.tvChildName2.visibility = GONE
                binding.etChildName2.visibility = GONE
                binding.etSpouseName.text?.clear()
                binding.etChildName1.text?.clear()
                binding.etChildName2.text?.clear()
            }
        }

        viewModel.successOnlinePayment.observe(viewLifecycleOwner) {
//            if (shouldShowDialog) {
//                showDialog()
//                shouldShowDialog = false
//            }
        }

        viewModel.isExistSubscribedPackages.observe(viewLifecycleOwner) {
            if (it.data) {
                snackBar(binding.btnSubmitInfo, "This user has another active package")
            } else {
                paymentBottomSheet()
            }
        }

        viewModel.orderDetails.observe(viewLifecycleOwner) {
            orderId = it?.data?.orderID
            orderType = it?.data?.orderType
            payment = it?.data?.paymentMethod
            Log.d("AAA", "Payment Method: " + paymentMethod)
            if (it.success) {
                if (payment.equals("Online")) {
                    if (paymentMethod.equals("BKASH")) {
                        val token: List<String> = sharedPreference(ApplicationClass.AppContext!!).getString(TOKEN)!!.split(" ")
                        val paymentUrl = BKASH_BASE_URL+"?checkoutOrderID="+orderId+"&token="+token[1]+"&orderType="+orderType
                        Log.d("AAA", paymentUrl)
                        paymentPage(paymentUrl)

//                        paymentViewModel.getBkashUrl(PaymentUrlRequest(orderId!!, orderType!!))
                    } else if (paymentMethod.equals("NAGAD")) {
                        paymentViewModel.getNagadUrl(PaymentUrlRequest(orderId!!, orderType!!))
                    } else if (paymentMethod.equals("VISA")) {
                        paymentViewModel.getSslUrl(PaymentUrlRequest(orderId!!, orderType!!))
                    }
                } else if (payment.equals("Cash")) {
                    Log.d("AAA", "CASH")
                }
            }

        }

        paymentViewModel.bkashSuccess.observe(viewLifecycleOwner) {
            Log.d("AAA", "Payment Url: ${it.data}")
            paymentPage(it.data)
        }

        paymentViewModel.nagadSuccess.observe(viewLifecycleOwner) {
            Log.d("AAA", "Payment Url: ${it.data}")
            paymentPage(it.data)
        }

        paymentViewModel.sslSuccess.observe(viewLifecycleOwner) {
            Log.d("AAA", "Payment Url: ${it.data}")
            paymentPage(it.data)
        }

        viewModel.successCashPayment.observe(viewLifecycleOwner) {
            if (it == true) {
                bottomSheet.dismiss()
                cashSuccessFragment()
                viewModel.successCashPayment.value = false
            }
        }

        viewModel.error.observe(this.viewLifecycleOwner) {
            if (viewModel.shouldCall.value == true) {
                snackBar(requireView(), it)
                viewModel.shouldCall.value = false
            }
        }

        viewModel.errorPayment.observe(this.viewLifecycleOwner) {
            if (viewModel.shouldCall.value == true) {
                bottomSheet.dismiss()
                snackBar(requireView(), it)
                viewModel.shouldCall.value = false
            }
        }
    }

    private fun paymentPage(paymentUrl: String) {
        val bundle = bundleOf(
            PAYMENT_URL to paymentUrl,
        )
        findNavController().navigate(R.id.paymentGateWay, bundle)

        bottomSheet.dismiss()
    }

    private fun colorMyText(
        inputText: String,
        startIndex: Int,
        endIndex: Int,
        textColor: Int
    ): Spannable {
        val outPutColoredText: Spannable = SpannableString(inputText)
        outPutColoredText.setSpan(
            ForegroundColorSpan(textColor), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return outPutColoredText
    }

    private fun setPatientInfo(data: PatientData?) {

        binding.etFirstName.setText(data?.firstName)
        binding.etLastName.setText(data?.lastName)
        binding.tvDobVal.setText(data?.dateOfBirth)
//        binding.etContactNo.setText(mobileNumber)

        gender = data?.gender
        binding.spGender.setSelection(genderArrayList.indexOf(gender))

        binding.etFirstName.isEnabled = data?.firstName.isNullOrEmpty()
        binding.etLastName.isEnabled = data?.lastName.isNullOrEmpty()
        binding.tvDobVal.isClickable = data?.dateOfBirth.isNullOrEmpty()
        binding.tvDobVal.isEnabled = data?.dateOfBirth.isNullOrEmpty()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spVerificationDoc -> {
                if (verificationTypeArrayList[position] == "Please select type") {
                    verificationType = null
                } else {
                    verificationType = verificationTypeArrayList[position]
                }
                binding.tvVerificationDoc.error = null
            }
            R.id.spRelationship -> {
                relationship = relationshipArrayList[position]
                binding.tvVerificationDoc.error = null
            }
            R.id.spPackageName -> {
                setPackageId(position - 1)
            }
            R.id.spGender -> {
                gender = genderArrayList[position]
                binding.tvGender.error = null
            }
        }
    }

    private fun setPackageId(position: Int) {
        if (viewModel.successPackages.value?.size!! > 0 && position >= 0) {
            viewModel.getInsuranceStatus(
                sharedPreference().getString(TOKEN)!!,
                viewModel.successPackages.value!![position].id
            )
            viewModel.isFamilyPackage(
                sharedPreference().getString(TOKEN)!!,
                viewModel.successPackages.value!![position].id
            )
            packageId = viewModel.successPackages.value?.get(position)?.id
            packageName = viewModel.successPackages.value?.get(position)?.name?.en
            packagePrice = viewModel.successPackages.value?.get(position)?.price?.discountPrice
//            packageDescription?.add(viewModel.successPackages.value?.get(position)?.description?.en?:"hi")
            binding.tvPackageName.error = null
        } else {
            packageId = null
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun spGender() {
        val spinnerAdapter =
            StringDropDownAdapter(
                requireContext(),
                genderArrayList
            )
        spinnerAdapter.setSpinnerMode(StringDropDownAdapter.SPINNER_MODE_CLASSIC)
        binding.spGender.adapter = spinnerAdapter
        binding.spGender.setSelection(0)
        binding.spGender.onItemSelectedListener = this
    }

    private fun spDocumentType() {
        val spinnerAdapter =
            StringDropDownAdapter(
                requireContext(),
                verificationTypeArrayList
            )
        spinnerAdapter.setSpinnerMode(StringDropDownAdapter.SPINNER_MODE_CLASSIC)
        binding.spVerificationDoc.adapter = spinnerAdapter
        binding.spVerificationDoc.setSelection(0)
        binding.spVerificationDoc.onItemSelectedListener = this
    }

    private fun spRelationship() {
        val spinnerAdapter =
            StringDropDownAdapter(
                requireContext(),
                relationshipArrayList
            )
        spinnerAdapter.setSpinnerMode(StringDropDownAdapter.SPINNER_MODE_CLASSIC)
        binding.spRelationship.adapter = spinnerAdapter
        binding.spRelationship.setSelection(0)
        binding.spRelationship.onItemSelectedListener = this
    }

    private fun spPackages(data: MutableList<PackageResponseModel.Data>) {
        val packageList = arrayListOf<String>()
        val packagePrice = arrayListOf<String>()
        packageList.add("Please Select Package")
        packagePrice.add("")
        for (i in data) {
            packageList.add(i.name.en)
        }

        for (i in data) {
            packagePrice.add("BDT "+i.price.regularPrice.toString())
        }

        val spinnerAdapter =
            StringDropDownAdapterForPackage(
                requireContext(),
                packageList,
                packagePrice
            )
        spinnerAdapter.setSpinnerMode(StringDropDownAdapter.SPINNER_MODE_CLASSIC)
        binding.spPackageName.adapter = spinnerAdapter
        binding.spPackageName.setSelection(0)
        binding.spPackageName.onItemSelectedListener = this
    }

    private fun submitInfo() {
        if (validation()) {
//            paySelectionBottomSheet()
            viewModel.getIsExistSubscribedPackages(mobileNumber ?: "")
//            submitRequest(ONLINE)
        } else {
            snackBar(requireView(), "Please provide valid information")
        }
    }

    private fun paySelectionBottomSheet() {
        val bottomSheet = BottomSheetPaySelection(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "")
    }

    private fun paymentBottomSheet() {
        bottomSheet = BottomSheetPaymentSelectionNew(this, packageName, packagePrice, packageId)
        bottomSheet.show(requireActivity().supportFragmentManager, "")

    }

    private fun validation(): Boolean {

        var res = true
        if (binding.etFirstName.text.toString().trim().isEmpty()) {

            binding.etFirstName.error = "Required"
            binding.etFirstName.requestFocus()
            res = false

        } else binding.etFirstName.error = null

        if (binding.etLastName.text.toString().trim().isEmpty()) {

            binding.etLastName.error = "Required"
            binding.etLastName.requestFocus()
            res = false

        } else binding.etLastName.error = null

        if (binding.etContactNo.text.toString().trim().isEmpty()) {
            binding.etContactNo.error = "Required"
            binding.etContactNo.requestFocus()
            res = false
        } else {
            if (validatePhoneNumber(binding.etContactNo.text.toString().trim())) {
                binding.etContactNo.error = null
            } else {
                binding.etContactNo.error = "Provide valid phone number"
                res = false
            }
        } /*else binding.etContactNo.error = null*/

        //if user has insurance subscription
//        if (viewModel.successInsurance.value== true && binding.etNomineeName.text.toString().trim().isEmpty()) {
//            binding.etNomineeName.error = "Required"
//            binding.etNomineeName.requestFocus()
//            res = false
//        } else binding.etNomineeName.error = null

        if (binding.etActivatedBy.text.toString().trim().isEmpty()) {
            binding.etActivatedBy.error = "Required"
            binding.etActivatedBy.requestFocus()
            res = false
        } else binding.etActivatedBy.error = null

        if (packageId == null || packageId?.trim()!!.isEmpty()) {
            binding.tvPackageName.error = "Required"
            binding.tvPackageName.requestFocus()
            res = false
        } else binding.tvPackageName.error = null

        if (gender?.trim()!!.isEmpty() || gender?.trim() == genderArrayList[0]) {
            binding.tvGender.error = "Required"
            binding.tvGender.requestFocus()
            res = false
        } else binding.tvGender.error = null

        if (binding.tvDobVal.text?.trim()!!.isEmpty() /*|| gender?.trim() == genderArrayList[0]*/) {
            binding.tvDobVal.error = "Required"
            binding.tvDobVal.requestFocus()
            res = false
        } else binding.tvDobVal.error = null

//        if ( verificationType.isNullOrEmpty() || verificationType!!.contains("please select type",true)){
//            binding.tvVerificationDoc.error = "Required"
//            res = false
//        }else binding.tvVerificationDoc.error = null
//
//        if ( binding.etVerificationNumber.text.toString().trim().isEmpty()) {
//            binding.etVerificationNumber.error = "Required"
//            binding.etVerificationNumber.requestFocus()
//            res = false
//        }else binding.etVerificationNumber.error = null


//        if (viewModel.successInsurance.value == true && binding.etNomineeMobileNumber.toString().trim().isEmpty()) {
//            binding.etNomineeMobileNumber.error = "Required"
//            binding.etNomineeMobileNumber.requestFocus()
//            res = false
//        }else if (viewModel.successInsurance.value == true){
//            if(validatePhoneNumber(binding.etNomineeMobileNumber.text.toString().trim())){
//                binding.etNomineeMobileNumber.error = null
//            }else{
//                binding.etNomineeMobileNumber.error = "Provide valid phone number"
//                res = false
//            }
//        }else binding.etNomineeMobileNumber.error = null

//        if (viewModel.successInsurance.value == true && (relationship.isNullOrEmpty() || relationship!!.contains("please select type",true))){
//            binding.tvRelationship.error = "Required"
//            res = false
//        }else binding.tvRelationship.error = null

        /*if (viewModel.isFamilyPackage.value == true && binding.etSpouseName.text.toString().isEmpty()){
            binding.etSpouseName.error = "Required"
            res = false
        }else binding.etSpouseName.error = null

        if (viewModel.isFamilyPackage.value == true && binding.etChildName1.text.toString().isEmpty()){
            binding.etChildName1.error = "Required"
            res = false
        }else binding.etChildName1.error = null

        if (viewModel.isFamilyPackage.value == true && binding.etChildName2.text.toString().isEmpty()){
            binding.etChildName2.error = "Required"
            res = false
        }else binding.etChildName2.error = null*/

        return res
    }

    private fun cashSuccessFragment() {
        val successFail = SuccessFail(
            "Thank You.",
            "You've successfully subscribed to the package. Now enjoy Unlimited Online Doctor Consultations 24/7.",
            "Go Back",
            CASH
        )

        val bundle = bundleOf(
            SUCCESS_FAIL to successFail,
        )
        findNavController().navigate(R.id.successOrFailFragment, bundle)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDatePicked(date: String?, isBirthDate: Boolean) {

        if (isBirthDate) {
            val parser = SimpleDateFormat("M-dd-yyyy")
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val output = formatter.format(parser.parse(date!!)!!)
            binding.tvDobVal.setText(output)
        }
    }

    private fun showDialog() {

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog)
        val view = AlertPaymentSuccessBinding.inflate(LayoutInflater.from(context))
        alertDialog.setView(view.root)
        val dialog = alertDialog.create()

        view.tvAlertTitle.text = getString(R.string.cashPaymentAlertTitleMessage)
        view.tvAlertSubTitle.text = getString(R.string.cashPaymentAlertSubtitleMessage)
        view.btnOnlinePayment.visibility = GONE
        view.btnCashPayment.visibility = VISIBLE

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
            EventBus.getDefault().post(EventBusHistory())
            findNavController().navigate(R.id.historyContainerFragment)
        }

        view.btnCashPayment.setOnClickListener {
//            submitReq(CASH)
            submitRequest(CASH)
            dialog.dismiss()
        }
    }

    override fun cashPay(orderId: String?) {
//        showDialog()
    }

    override fun onlinePay(orderId: String?) {
//        submitReq(ONLINE)
    }

    /*private fun submitReq(paymentMethod: String){
        viewModel.onlinePayment(
            sharedPreference().getString(TOKEN)!!,
            ActivateSubscriptionRequestModel(
                firstName = binding.etFirstName.text.toString().trim(),
                lastName = binding.etLastName.text.toString().trim(),
                dateOfBirth = if (binding.tvDobVal.text.toString().trim()
                        .isEmpty()
                ) null else binding.tvDobVal.text.toString().trim(),
                gender = if (gender.toString().trim().isEmpty()) null else gender?.trim(),
                contactNumber = binding.etContactNo.text.toString().trim(),
//                email = if (binding.etEmail.text.toString().trim()
//                        .isEmpty()
//                ) null else binding.etEmail.text.toString().trim(),
                email ="noreply@zaynaxhealth.com",

                identityVerificationDocumentType = verificationType,
//                identityVerificationDocumentType = if (verificationType.isNullOrEmpty() || verificationType!!.contains(
//                        "please select type",
//                        true
//                    )
//                ) null else verificationType,
                identityVerificationNumber = binding.etVerificationNumber.text.toString().trim(),
//                identityVerificationNumber = if (binding.etVerificationNumber.text.toString()
//                        .trim().isEmpty()
//                ) null else binding.etVerificationNumber.text.toString().trim(),
                nomineePhoneNumber = if (binding.etNomineeMobileNumber.text.toString()
                        .trim().isEmpty()
                ) null else binding.etNomineeMobileNumber.text.toString().trim(),
                spouseName = if (binding.etSpouseName.text.toString().trim().isNotEmpty()) binding.etSpouseName.text.toString().trim() else null,
                childName1 = if (binding.etChildName1.text.toString().trim().isNotEmpty()) binding.etChildName1.text.toString().trim() else null,
                childName2 = if (binding.etChildName2.text.toString().trim().isNotEmpty()) binding.etChildName2.text.toString().trim() else null,
                relationshipWithNominee = if (relationship.isNullOrEmpty() || relationship!!.contains(
                        "please select type",
                        true
                    )
                ) null else relationship,
                packageId = packageId!!.trim(),
                comment = if (binding.etComment.text.toString().trim()
                        .isEmpty()
                ) null else binding.etComment.text.toString().trim(),
                paymentMethod = paymentMethod,
                deviceSource = "ANDROID",
                subPackSource = "RETAIL"
            )
        )
    }*/

    private fun submitRequest(paymentMethod: String) {
        viewModel.packageSubscriptions(
            sharedPreference().getString(TOKEN)!!,
            ActivateSubscriptionRequestModel(
                firstName = binding.etFirstName.text.toString().trim(),
                lastName = binding.etLastName.text.toString().trim(),
                dateOfBirth = if (binding.tvDobVal.text.toString().trim()
                        .isEmpty()
                ) null else binding.tvDobVal.text.toString().trim(),
                gender = if (gender.toString().trim().isEmpty()) null else gender?.trim(),
                contactNumber = binding.etContactNo.text.toString().trim(),
//                email = if (binding.etEmail.text.toString().trim()
//                        .isEmpty()
//                ) null else binding.etEmail.text.toString().trim(),
                email = "noreply@zaynaxhealth.com",

                identityVerificationDocumentType = verificationType,
//                identityVerificationDocumentType = if (verificationType.isNullOrEmpty() || verificationType!!.contains(
//                        "please select type",
//                        true
//                    )
//                ) null else verificationType,
                identityVerificationNumber = binding.etVerificationNumber.text.toString().trim(),
//                identityVerificationNumber = if (binding.etVerificationNumber.text.toString()
//                        .trim().isEmpty()
//                ) null else binding.etVerificationNumber.text.toString().trim(),
                nomineePhoneNumber = if (binding.etNomineeMobileNumber.text.toString()
                        .trim().isEmpty()
                ) null else binding.etNomineeMobileNumber.text.toString().trim(),
                spouseName = if (binding.etSpouseName.text.toString().trim()
                        .isNotEmpty()
                ) binding.etSpouseName.text.toString().trim() else null,
                childName1 = if (binding.etChildName1.text.toString().trim()
                        .isNotEmpty()
                ) binding.etChildName1.text.toString().trim() else null,
                childName2 = if (binding.etChildName2.text.toString().trim()
                        .isNotEmpty()
                ) binding.etChildName2.text.toString().trim() else null,
                relationshipWithNominee = if (relationship.isNullOrEmpty() || relationship!!.contains(
                        "please select type",
                        true
                    )
                ) null else relationship,
                packageId = packageId!!.trim(),
                comment = if (binding.etComment.text.toString().trim()
                        .isEmpty()
                ) null else binding.etComment.text.toString().trim(),
                paymentMethod = paymentMethod,
                deviceSource = "ANDROID",
                subPackSource = "RETAIL"
            )
        )
    }

    override fun onCashPay(paymentType: String) {
        paymentMethod = paymentType
        showDialog()
//        Toast.makeText(requireContext(), "Please Pay with $paymentType", Toast.LENGTH_SHORT).show()
    }

    override fun onBkashPay(paymentType: String) {
        paymentMethod = paymentType
        submitRequest(ONLINE)
//        Toast.makeText(requireContext(), "Please Pay with $paymentType", Toast.LENGTH_SHORT).show()
    }

    override fun onNagadPay(paymentType: String) {
        paymentMethod = paymentType
        submitRequest(ONLINE)
//        Toast.makeText(requireContext(), "Please Pay with $paymentType", Toast.LENGTH_SHORT).show()
    }

    override fun onCardPay(paymentType: String) {
        paymentMethod = paymentType
        submitRequest(ONLINE)
//        Toast.makeText(requireContext(), "Please Pay with $paymentType", Toast.LENGTH_SHORT).show()
    }


}