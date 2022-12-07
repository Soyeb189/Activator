package com.zaynaxhealth.activator.ui.user_number_verification.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.ApplicationClass
import com.zaynaxhealth.activator.databinding.FragmentUserNumberVerificationBinding
import com.zaynaxhealth.activator.ui.user_number_verification.viewmodel.UserVerificationViewModel
import com.zaynaxhealth.activator.utilities.*

class UserNumberVerificationFragment : Fragment() {
    private lateinit var binding: FragmentUserNumberVerificationBinding
    private lateinit var viewModel: UserVerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserNumberVerificationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserVerificationViewModel::class.java]

        Log.d("AAA","TOKEN :  ${sharedPreference(ApplicationClass.AppContext!!).getString(TOKEN)!!}")

        clickEvents()
        observers()
    }

    private fun clickEvents() {
        systemBackClick()

        binding.btnSendOtp.setOnClickListener {
            viewModel.sendOtp(
                binding.etPhoneNumber.text.toString()
            )
        }

        binding.btnSubmitCode.setOnClickListener {
            val phoneNumber:String = binding.etPhoneNumber.text?.trim().toString()
//            val bundle = bundleOf(
//                MOBILE_NUMBER to phoneNumber
//            )
            if (phoneNumber.isEmpty()){
                binding.etPhoneNumber.error = "Enter phone number"
            }else{
                if (validatePhoneNumber(phoneNumber)){
                    hideKeyboard(requireContext(),requireView())
                    viewModel.getIsExistSubscribedPackages(phoneNumber)
                }else{
                    binding.etPhoneNumber.error = "Enter valid phone number"
                }
            }


//            viewModel.verifyOtp(
//                binding.etPhoneNumber.text.toString(),
//                binding.etOtp.text.toString()
//            )
        }
    }

    private fun observers(){
        viewModel.isPhoneNumberInvalid.observe(viewLifecycleOwner) {
            binding.etPhoneNumber.error = it
        }

        viewModel.isOtpEmpty.observe(viewLifecycleOwner) {
            binding.etOtp.error = it
        }

        viewModel.successSendOtp.observe(viewLifecycleOwner) {
            toast(it)
        }

        viewModel.isExistSubscribedPackages.observe(viewLifecycleOwner){
            if (it.data){
                snackBar(binding.btnSubmitCode,"This user has another active package")
            }else{
                val phoneNumber:String = binding.etPhoneNumber.text?.trim().toString()
                val bundle = bundleOf(
                    MOBILE_NUMBER to phoneNumber
                )
                if (phoneNumber.isEmpty()){
                    binding.etPhoneNumber.error = "Enter phone number"
                }else{
                    if (validatePhoneNumber(phoneNumber)){
                        findNavController().navigate(R.id.userInfoSubmitFragment,bundle)
                    }else{
                        binding.etPhoneNumber.error = "Enter valid phone number"
                    }
                }
            }
        }

        viewModel.successOtpVerification.observe(viewLifecycleOwner) {
            toast(it)
            val bundle = bundleOf(
                MOBILE_NUMBER to binding.etPhoneNumber.text?.trim().toString()
            )
            findNavController().navigate(R.id.userInfoSubmitFragment, bundle)
        }

        viewModel.error.observe(viewLifecycleOwner) {
//            snackBar(requireView(),it)
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.progressBar.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = it
        }

    }
}