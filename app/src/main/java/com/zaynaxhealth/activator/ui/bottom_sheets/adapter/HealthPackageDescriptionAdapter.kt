package com.zaynaxhealth.activator.ui.bottom_sheets.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.databinding.ItemPackageLongDescriptionBinding

class HealthPackageDescriptionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet: ArrayList<String?>? = null

    fun clearData() {
        if (dataSet != null) {

        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return HEALTH_PACKAGE_DESCRIPTION_ITEM_VIEW_TYPE
    }

    fun setData(dataSet: ArrayList<String?>?) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPackageLongDescriptionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_package_long_description, parent, false
        )
        return AppointmentItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            HEALTH_PACKAGE_DESCRIPTION_ITEM_VIEW_TYPE -> {
                val appointmentItemViewHolder = holder as AppointmentItemViewHolder
                appointmentItemViewHolder.bindView(/*dataSet!![position]*/)
            }
        }
    }

    override fun getItemCount(): Int {
//        return if (dataSet == null) 0 else dataSet!!.size
        return 0
    }

    private class AppointmentItemViewHolder(private val binding: ItemPackageLongDescriptionBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bindView(/*model: String*/) {
//            binding.textView201.text = model

        }
    }


    companion object {
        private const val HEALTH_PACKAGE_DESCRIPTION_ITEM_VIEW_TYPE = 1

    }
}