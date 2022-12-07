package com.zaynaxhealth.activator.ui.activation_history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zaynaxhealth.activator.core.ClickListener
import com.zaynaxhealth.activator.data.model.responsemodels.HistoryResponseModel
import com.zaynaxhealth.activator.databinding.ItemHistoryBinding
import com.zaynaxhealth.activator.ui.activation_history.view.CASH
import com.zaynaxhealth.activator.ui.activation_history.view.PAID
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat

//ActivatedHistoryAdapter
class ActivatedHistoryAdapter(
    private val clickListener: ClickListener<HistoryResponseModel>,
    private val type: String
) : PagingDataAdapter<HistoryResponseModel, ActivatedHistoryAdapterViewHolder>(
    DiffUtils
) {
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivatedHistoryAdapterViewHolder {
        context = parent.context
        val view = ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ActivatedHistoryAdapterViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ActivatedHistoryAdapterViewHolder, position: Int) {
        val historyModel = getItem(position)
        holder.tvActivationId.text = "${historyModel?.paymentGateway}"
        holder.tvActivationPatientName.text = historyModel?.name
        holder.tvActivationPhoneNumber.text = historyModel?.phoneNumber
        holder.tvPackagePrice.text =
            "BDT " + historyModel?.price?.let { roundOffDecimal(it).toString() }
        holder.tvPackageName.text = historyModel?.packageName

        val formatter: DateFormat = SimpleDateFormat("hh:mm:a, dd MMM yyyy")
        var activatedDate = formatter.format(historyModel?.createdOn)
        holder.tvActivationDate.text = activatedDate

/*        if (type == ACTIVATED) {
            holder.tvActivationHistoryType.text = context.getString(R.string.pay_now)
        } else if (type == PAID) {
            holder.tvActivationHistoryType.text = context.getString(R.string.paid)
        }*/

        if (historyModel?.paymentMethod == "Cash" && historyModel.transactionId == "") {
            holder.tvActivationHistoryType.text = "Cash"
        } else {
            holder.tvActivationHistoryType.text = "Paid"
        }

        holder.clHolderHistoryItem.setOnClickListener {
            clickListener.clickedData(
                historyModel!!
            )
        }

        holder.tvActivationHistoryType.setOnClickListener {
            if (type == CASH) {

                clickListener.clickedData(
                    historyModel!!
                )
            } else if (type == PAID) {
                clickListener.clickedData(
                    historyModel!!
                )
            }
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<HistoryResponseModel>() {
        override fun areItemsTheSame(
            oldItem: HistoryResponseModel,
            newItem: HistoryResponseModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HistoryResponseModel,
            newItem: HistoryResponseModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

}