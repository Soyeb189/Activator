package com.zaynaxhealth.activator.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zaynaxhealth.activator.R

class StringDropDownAdapterForPackage(private val mContext: Context, private val datSet: List<String>,private val datSetPrice: List<String>) :
    ArrayAdapter<String?>(
        mContext, R.layout.string_item_view_classic
    ) {
    private var spinnerMode: Int
    private val layoutId = 0
    fun setSpinnerMode(mode: Int) {
        spinnerMode = mode
    }

    override fun getCount(): Int {
        return datSetPrice.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView?.setPadding(0, convertView.paddingTop, convertView.paddingRight, convertView.paddingBottom)
        var viewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater =
                (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            convertView = if (spinnerMode == SPINNER_MODE_BOLD_ITEM) {
                mInflater.inflate(
                    R.layout.string_item_view_bold, parent, false
                )
            } else if (spinnerMode == SPINNER_MODE_CLASSIC) {
                mInflater.inflate(
                    R.layout.string_item_view_classic_package, parent, false
                )
            } else {
                mInflater.inflate(
                    layoutId, parent, false
                )
            }
            viewHolder.stringName = convertView.findViewById(R.id.tv_string_item_name)
            viewHolder.itemPrice = convertView.findViewById(R.id.itemPrice)
            convertView.tag = viewHolder
        } else {
//            View view = super.getView(position, convertView, parent);
//            convertView.setPadding(0, convertView.paddingTop, convertView.paddingRight, convertView.paddingBottom)
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.stringName!!.text = datSet[position]
        viewHolder.itemPrice!!.text = datSetPrice[position]
        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        var stringName: TextView? = null
        var itemPrice: TextView? = null
    }

    companion object {
        const val SPINNER_MODE_CLASSIC = 0
        const val SPINNER_MODE_BOLD_ITEM = 1
        const val UNKNOWN_MODE = 2
    }

    init {
        spinnerMode = SPINNER_MODE_CLASSIC
    }
}