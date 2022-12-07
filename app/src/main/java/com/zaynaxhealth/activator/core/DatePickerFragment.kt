package com.zaynaxhealth.activator.core

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.zaynaxhealth.activator.R
import java.util.*

class DatePickerFragment private constructor(
    private val tittle: String,
    listener: DatePickListener,
    date: String,
    isBirthDate: Boolean
) : DialogFragment() {
    private val birthDate: String?
    private val isBirthDate: Boolean
    private val listener: DatePickListener?
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val factory = LayoutInflater.from(context)
        val rootView = factory.inflate(R.layout.date_picker_dialog_fragment, null)
        val tvTittle = rootView.findViewById<TextView>(R.id.tv_date_picker_tittle)
        tvTittle.text = tittle
        if (birthDate!!.isNotEmpty()) {
            val date = birthDate.split("-").toTypedArray()

            /*int day = Integer.parseInt(date[1]);
            int month = Integer.parseInt(date[0])-1;
            int year = Integer.parseInt(date[2]);*/
            val day = date[2].toInt()
            val month = date[1].toInt() - 1
            val year = date[0].toInt()
            val datePicker = rootView.findViewById<DatePicker>(R.id.datePicker)
            datePicker.updateDate(year, month, day)
        }
        rootView.findViewById<View>(R.id.tv_pick_button).setOnClickListener { v: View? ->
            val datePicker = rootView.findViewById<DatePicker>(R.id.datePicker)
            if (listener != null) {
                val day = datePicker.dayOfMonth
                val monthName = datePicker.month + 1
                // format : mm-dd-yyyy
                val date = monthName.toString() + "-" + day + "-" + datePicker.year
                listener.onDatePicked(date, isBirthDate)
                dismiss()
            }
        }
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(rootView)
        alertDialogBuilder.setPositiveButton("", null)
        alertDialogBuilder.setNegativeButton("", null)
        val dialog: Dialog = alertDialogBuilder.create()
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    interface DatePickListener {
        fun onDatePicked(date: String?, isBirthDate: Boolean)
    }

    companion object {
        // date format expected : mm-dd-yyyy
        fun showDialog(
            fm: FragmentManager?,
            tittle: String,
            listener: DatePickListener,
            date: String,
            isBirthDate: Boolean
        ) {
            DatePickerFragment(tittle, listener, date, isBirthDate).show(fm!!, "fragment_alert")
        }
    }

    init {
        this.listener = listener
        birthDate = date
        this.isBirthDate = isBirthDate
    }
}