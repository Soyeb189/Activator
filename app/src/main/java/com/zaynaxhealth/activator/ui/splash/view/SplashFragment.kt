package com.zaynaxhealth.activator.ui.splash.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.databinding.FragmentSplashBinding
import com.zaynaxhealth.activator.utilities.TOKEN
import com.zaynaxhealth.activator.utilities.sharedPreference


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreference().getString(TOKEN)?.isNotEmpty() == true) {
                findNavController().navigate(R.id.userNumberVerificationFragment)
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        },3000)
    }
}