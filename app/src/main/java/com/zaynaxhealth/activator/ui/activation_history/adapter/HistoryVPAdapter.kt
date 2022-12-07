package com.zaynaxhealth.activator.ui.activation_history.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zaynaxhealth.activator.ui.activation_history.view.AllActivatedFragment
import com.zaynaxhealth.activator.ui.activation_history.view.HistoryFragment
import com.zaynaxhealth.activator.ui.activation_history.view.PaidFragment

class HistoryVPAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3 // it should be 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AllActivatedFragment()
            1 -> HistoryFragment()
            2 -> PaidFragment()
            else  -> PaidFragment()
        }
    }
}