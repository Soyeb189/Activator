package com.zaynaxhealth.activator.ui.activation_history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.zaynaxhealth.activator.databinding.ItemHistoryBinding

class ActivatedHistoryAdapterViewHolder(itemView: ItemHistoryBinding):RecyclerView.ViewHolder(itemView.root) {
    val tvActivationPatientName= itemView.tvActivationPatientName
    val tvActivationId= itemView.tvActivationId
    val tvActivationPhoneNumber= itemView.tvActivationPhoneNumber
    val tvActivationHistoryType= itemView.tvActivationHistoryType
    val tvPackageName= itemView.tvPackageName
    val tvPackagePrice= itemView.tvPackagePrice
    val tvActivationDate= itemView.tvActivationDate
    val clHolderHistoryItem = itemView.clHolderHistoryItem
//    val btnPayNow= itemView.llActivationHistoryType
}
