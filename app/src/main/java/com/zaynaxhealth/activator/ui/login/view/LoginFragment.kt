package com.zaynaxhealth.activator.ui.login.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.startUpdateFlowForResult
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.ApplicationClass
import com.zaynaxhealth.activator.data.local.sharedpreference.SharedPreferenceModelImpl
import com.zaynaxhealth.activator.data.model.EventActivatorInfo
import com.zaynaxhealth.activator.databinding.FragmentLoginBinding
import com.zaynaxhealth.activator.ui.login.viewmodel.LoginViewModel
import com.zaynaxhealth.activator.utilities.*
import org.greenrobot.eventbus.EventBus

class LoginFragment : Fragment(){
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreference: SharedPreferenceModelImpl

    private var appUpdate : AppUpdateManager? = null
    private val REQUEST_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedPreference = SharedPreferenceModelImpl(requireContext())
        clickEvents()
        observers()
        checkUpdate()
    }

    fun checkUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                appUpdate?.startUpdateFlowForResult(updateInfo,
                    AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
                Log.d("UPDATE","updateProgress")
            }
        }
    }

    fun inProgressUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdate?.startUpdateFlowForResult(updateInfo,
                    AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
                Log.d("UPDATE","updateProgress")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun clickEvents() {
        systemBackClick()
        binding.btnLogin.setOnClickListener {
            hideKeyboard(requireContext(), requireView())
            viewModel.login(binding.etEmailPhone.text?.trim().toString(),binding.etPassword.text?.trim().toString())
        }
    }

    private fun observers(){
        viewModel.progressBar.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            snackBar(requireView(), it)
        }
        viewModel.success.observe(viewLifecycleOwner) {
//            toast("Login Successful")
//            findNavController().navigate(R.id.userNumberVerificationFragment)
            if (it.data.isNotEmpty()) {
                viewModel.getActivatorInfo(it.data)
                sharedPreference.saveString(TOKEN, it.data)
            } else {
                toast("Something went wrong")
            }
        }

        viewModel.successActivatorInfo.observe(viewLifecycleOwner) {
            if (it?.activatorType == "SUPER_AGENT"){
                toast("Please Login with Super Agent App")
            }else{
                toast("Login Successful")
                sharedPreference.saveString(ACTIVATOR_NAME, it?.activatorName)
                sharedPreference.saveString(USER_TYPE, it?.activatorType)
                var data = it?.activatorType?.let { it1 ->
                    EventActivatorInfo(
                        it?.activatorName,
                        it1
                    )
                }
                EventBus.getDefault().post(data)
                findNavController().navigate(R.id.userNumberVerificationFragment)
            }

        }

        viewModel.isUsernameEmpty.observe(this.viewLifecycleOwner) {
            binding.etEmailPhone.error = it
        }
        viewModel.isPasswordEmpty.observe(viewLifecycleOwner) {
            binding.etPassword.error = it
        }
    }


}