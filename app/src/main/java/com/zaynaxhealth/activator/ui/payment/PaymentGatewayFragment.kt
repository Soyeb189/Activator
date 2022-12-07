package com.zaynaxhealth.activator.ui.payment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.data.model.SuccessFail
import com.zaynaxhealth.activator.databinding.FragmentPaymentGatewayBinding
import com.zaynaxhealth.activator.utilities.*


class PaymentGatewayFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentPaymentGatewayBinding
    private lateinit var wvPayment: WebView
    private var paymentUrl: String? = null
    private lateinit var noInternetOrEmptyView: View
    private lateinit var refreshButton: TextView
    private lateinit var pullToRefresh: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentGatewayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wvPayment = binding.root.findViewById(R.id.wv_payment)
        pullToRefresh = binding.root.findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener(this)
        noInternetOrEmptyView = binding.root.findViewById(R.id.no_internet_or_empty_view)
        refreshButton = binding.root.findViewById(R.id.txt_refresh)
        noInternetOrEmptyView(
            noInternetOrEmptyView,
            "Payment Options are Loading..",
            "Please wait....",
            R.drawable.ic_loading_view,
            View.VISIBLE
        )
        paymentUrl = requireArguments().getString(PAYMENT_URL)
        webViewSettings
        loadPaymentGateway(paymentUrl)
        refreshButton.setOnClickListener(View.OnClickListener {
            pullToRefresh.isRefreshing = true
            noInternetOrEmptyView.visibility = View.VISIBLE
            noInternetOrEmptyView(
                noInternetOrEmptyView,
                "Payment Options are Loading..",
                "Please wait....",
                R.drawable.ic_loading_view,
                View.VISIBLE
            )
            loadPaymentGateway(paymentUrl)
        })


        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.userNumberVerificationFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isNetworkAvailable(activity?.applicationContext)) {
                    pullToRefresh.isRefreshing = false
                    noInternetOrEmptyView.visibility = View.GONE
                    wvPayment.visibility = View.VISIBLE
                } else {
                    noInternetOrEmptyView.visibility = View.VISIBLE
                    wvPayment.visibility = View.GONE
                    activity?.let {
                        noInternetOrEmptyView(
                            noInternetOrEmptyView, it.getString(R.string.no_internet_title),
                            resources.getString(R.string.no_internet_sub_title),
                            R.drawable.ic_oops,
                            View.VISIBLE
                        )
                    }
                }
                handler.postDelayed(this, 500)
            }
        }, 500)

    }

    private val webViewSettings: Unit
        private get() {
            val webSettings = wvPayment.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.setSupportZoom(true)
            webSettings.allowFileAccess = true
            webSettings.allowContentAccess = true
            webSettings.loadWithOverviewMode = true
            webSettings.defaultTextEncodingName = "utf-8"
        }

    private fun loadPaymentGateway(url: String?) {
        wvPayment.loadUrl(url!!)
        wvPayment.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                webView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                //Clearing the WebView
                try {
                    webView.stopLoading()
                } catch (e: Exception) {
                }
                try {
                    webView.clearView()
                } catch (e: Exception) {
                }
                if (webView.canGoBack()) {
                    webView.goBack()
                }
                if (failingUrl.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(failingUrl))
                    startActivity(intent)
                } else {
                    webView.loadUrl("about:blank")
                    pullToRefresh.isRefreshing = true
                    wvPayment.visibility = View.GONE
                    noInternetOrEmptyView.visibility = View.VISIBLE
                    noInternetOrEmptyView(
                        noInternetOrEmptyView,
                        resources.getString(R.string.no_internet_title),
                        resources.getString(R.string.no_internet_sub_title),
                        R.drawable.ic_oops,
                        View.VISIBLE
                    )
                }

                //Don't forget to call supper!
                super.onReceivedError(webView, errorCode, description, failingUrl)
            }

            override fun onPageFinished(view: WebView, url: String) {
                pullToRefresh.isRefreshing = false
                Log.d("AAA", "onPageFinished Payment Url:  $url");
                if (url.contains(PAYMENT_BASE_URL) && url.contains("success") ||
                    url.contains(PAYMENT_BASE_URL) && url.contains("Success")
                ) {
                    successFailIntent(true, "Payment Successfully Done")
                    return
                }
                if (url.contains(PAYMENT_BASE_URL) && url.contains("fail") ||
                    url.contains(PAYMENT_BASE_URL) && url.contains("Fail")
                ) {
                    successFailIntent(false, "Payment Failed")
                    return
                }
                if (url.contains(PAYMENT_BASE_URL) && url.contains("cancel") ||
                    url.contains(PAYMENT_BASE_URL) && url.contains("Aborted")
                ) {
                    successFailIntent(false, "Payment Canceled By User")
                    return
                }

            }
        }
    }

    override fun onRefresh() {
        pullToRefresh.isRefreshing = true
        loadPaymentGateway(paymentUrl)
    }

    private fun successFailIntent(isSuccess: Boolean, message: String) {
        cashSuccessFragment(isSuccess)
    }

    private fun cashSuccessFragment(isSuccess: Boolean) {
        if (isSuccess) {
            val successFail = SuccessFail(
                "Thank You.",
                "You've successfully subscribed to the package. Now enjoy Unlimited Online Doctor Consultations 24/7.",
                "Go Back",
                CASH
            )

            val bundle = bundleOf(
                SUCCESS_FAIL to successFail,
            )
            findNavController().navigate(R.id.successOrFailFragment, bundle)
        } else {
            val successFail = SuccessFail(
                "Oops!",
                "Your payment not complete",
                "Go Back",
                CASH
            )

            val bundle = bundleOf(
                SUCCESS_FAIL to successFail,
            )
            findNavController().navigate(R.id.successOrFailFragment, bundle)
        }

    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

}