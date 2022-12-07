package com.zaynaxhealth.activator.ui.activation_history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zaynaxhealth.activator.ui.activation_history.adapter.HistoryVPAdapter
import com.zaynaxhealth.activator.databinding.FragmentHistoryContainerBinding


class HistoryContainerFragment : Fragment() {

    private val tagForLog = "TAG_HistoryContainerFragment"
    private var _binding: FragmentHistoryContainerBinding? = null
    private val binding get() = _binding!!

    // tab titles
    private val titles = arrayOf("All","Cash","Paid")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryContainerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initListener()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = HistoryVPAdapter(requireActivity())


        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int -> tab.text = titles[position] }.attach()



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initListener() {



    }


}