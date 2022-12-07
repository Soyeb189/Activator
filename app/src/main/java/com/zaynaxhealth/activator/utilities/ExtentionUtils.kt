package com.zaynaxhealth.activator.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.data.local.sharedpreference.SharedPreferenceModelImpl


fun snackBar(view: View, message: String){
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.show()
//    Snackbar.make(view,message,Snackbar.LENGTH_INDEFINITE).apply {
//        setAction("OK", View.OnClickListener {
//            dismiss()
//        })
//    }.show()
}

fun Fragment.toast(message:String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}

fun Fragment.sharedPreference(): SharedPreferenceModelImpl{
    return SharedPreferenceModelImpl(requireContext())
}

fun sharedPreference(context: Context): SharedPreferenceModelImpl{
    return SharedPreferenceModelImpl(context)
}

fun Fragment.systemBackClick(){
    val callBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,callBack)
}

fun validatePhoneNumber(phoneNumber: String): Boolean {

    return (
            phoneNumber.length>2 && (
                            (
                                    phoneNumber.substring(0, 3) == "013" ||
                                            phoneNumber.substring(0, 3) == "014" ||
                                            phoneNumber.substring(0, 3) == "015" ||
                                            phoneNumber.substring(0, 3) == "016" ||
                                            phoneNumber.substring(0, 3) == "017" ||
                                            phoneNumber.substring(0, 3) == "018" ||
                                            phoneNumber.substring(0, 3) == "019"
                                    )
                                    && phoneNumber.length == 11

                            )
    )
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object: Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun hideKeyboard(context: Context, view: View) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun noInternetOrEmptyView(
    view: View,
    title: String,
    subTitle: String?,
    image: Int,
    refreshVisibility: Int
) {
    val txt_title = view.findViewById<TextView>(R.id.txt_title)
    val txt_sub_title = view.findViewById<TextView>(R.id.txt_sub_title)
    val txt_refresh = view.findViewById<TextView>(R.id.txt_refresh)
    val iv_empty_or_internet = view.findViewById<ImageView>(R.id.iv_empty_or_internet)
    txt_title.text = title
    txt_sub_title.text = subTitle
    txt_refresh.visibility = refreshVisibility
    Glide.with(view.context)
        .load(image)
        .placeholder(R.drawable.ic_empty_view)
        .into(iv_empty_or_internet)
    if (title.contains("Loading")) {
        iv_empty_or_internet.layoutParams.height = 150
        iv_empty_or_internet.layoutParams.width = 150
    }

}