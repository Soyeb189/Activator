package com.zaynaxhealth.activator.ui.activation_history.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.ClickListener
import com.zaynaxhealth.activator.data.model.EventBusHistory
import com.zaynaxhealth.activator.data.model.responsemodels.HistoryResponseModel
import com.zaynaxhealth.activator.databinding.FragmentHistoryBinding
import com.zaynaxhealth.activator.ui.activation_history.adapter.ActivatedHistoryAdapter
import com.zaynaxhealth.activator.ui.activation_history.viewmodel.ActivatedHistoryViewModel
import com.zaynaxhealth.activator.ui.activation_history.viewmodel.ActivatedHistoryViewModelFactory
import com.zaynaxhealth.activator.utilities.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

const val CASH = "Cash"

class HistoryFragment : Fragment(), ClickListener<HistoryResponseModel>,
    TransactionNumber.RefreshActivatedHistory {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: ActivatedHistoryViewModel

/*    private val viewModel: ActivatedHistoryViewModel by activityViewModels{
    ActivatedHistoryViewModelFactory(
    sharedPreference().getString(TOKEN)!!, ACTIVATED,"2022-06-01","2022-06-07"
)
}*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)

        loadData()
        observers()

        binding.calenderLyt.ivRefresh.setOnClickListener {
            initAdapter("", "")
            binding.calenderLyt.tvCalenderView.text = "Filter by Date"
            viewModel.getPaidHistory(
                CASH, 1, "", ""
            )
        }

        binding.calenderLyt.tvCalenderView.setOnClickListener {
            showDateRangePicker()
        }

    }



    fun loadData() {
        viewModel = ViewModelProvider(
            this,
            ActivatedHistoryViewModelFactory(
                sharedPreference().getString(TOKEN)!!, CASH, "", ""
            )
        )[ActivatedHistoryViewModel::class.java]
        initAdapter("", "")

        viewModel.getPaidHistory(
            CASH, 1, "", ""
        )
    }

    @Subscribe
    fun updateList(eventBusHistory: EventBusHistory) {

        viewModel = ViewModelProvider(
            this,
            ActivatedHistoryViewModelFactory(
                sharedPreference().getString(TOKEN)!!, CASH, "", ""
            )
        )[ActivatedHistoryViewModel::class.java]
        initAdapter("", "")

        viewModel.getPaidHistory(
            CASH, 1, "", ""
        )
    }

    private fun initAdapter(startDate: String, endDate: String) {
        val adapter = ActivatedHistoryAdapter(this, CASH)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.idRVUsers.layoutManager = layoutManager
        binding.idRVUsers.adapter = adapter

        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Loading) {
                binding.idPBLoading.visibility = VISIBLE
            } else if (it.refresh is LoadState.Error) {
                if ((it.refresh as LoadState.Error).error.localizedMessage.contains(
                        "failed to connect",
                        true
                    )
                ) {
                    snackBar(requireView(), "No Internet")
                }
                binding.idPBLoading.visibility = GONE
            } else {
                if (adapter.itemCount == 0) {
                    binding.includeEmptyView.emptyViewLayout.visibility = VISIBLE
                } else {
                    binding.includeEmptyView.emptyViewLayout.visibility = GONE
                }
                binding.idPBLoading.visibility = GONE
            }
        }
        lifecycleScope.launch {
            viewModel.getActivatedHistory(
                sharedPreference().getString(TOKEN)!!, CASH, startDate, endDate
            ).collectLatest {
                adapter.submitData(it)
            }


        }
    }

    private fun observers() {
        viewModel.paidHistorySuccess.observe(viewLifecycleOwner) {
//            binding.tvTotalPaid.text = it.data.totalSales.toString()
            Log.d("AAA", it.data.totalSales.toString())
            binding.tvTotalSales.text = "BDT " + roundOffDecimal(it.data.totalSales).toString()
            binding.tvTotalPackage.text = it.data.totalPackages.toString()
            Log.d("AAA", it.data.totalSales.toString())

            if (it.data.data.isEmpty()) {
                binding.totalLyt.visibility = GONE
                binding.btnDownload.visibility = GONE
            } else {
                binding.totalLyt.visibility = VISIBLE
                binding.btnDownload.visibility = GONE
            }
        }
    }

    override fun clickedData(data: HistoryResponseModel) {
        bottomSheet(data)
    }

    private fun bottomSheet(data: HistoryResponseModel) {
        val bottomSheet = TransactionNumber(data, viewModel, this)
        bottomSheet.show(requireActivity().supportFragmentManager, "")
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        loadData()
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    //******************* For Date Range Picker *****************//


    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select Date")
            .setTheme(R.style.MaterialCalendarTheme)
            .build()


        dateRangePicker.show(
            parentFragmentManager,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener {
            val startDate = it.first
            val endDate = it.second
            binding.calenderLyt.tvCalenderView.text =
                convertLongToDate(startDate) + " " + " to  " + convertLongToDate(endDate)
            Log.d(
                "Date",
                "Start: " + convertLongToDate(startDate) + "End: " + convertLongToDate(endDate)
            )
            val fromDate: String = convertLongToDate(startDate)
            val toDate: String = convertLongToDate(endDate)
            viewModel = ViewModelProvider(
                this,
                ActivatedHistoryViewModelFactory(
                    sharedPreference().getString(TOKEN)!!, CASH, fromDate, toDate
                )
            )[ActivatedHistoryViewModel::class.java]
            initAdapter(fromDate, toDate)

            viewModel.getPaidHistory(
                CASH, 1, fromDate, toDate
            )
        }

    }


    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )
        return format.format(date)
    }

    override fun onItemClick() {
        Log.d("AAA", "Page Refreshed")

        Handler(Looper.getMainLooper()).postDelayed(
            {
                loadData()// This method will be executed once the timer is over
            },
            1000 // value in milliseconds
        )


    }

    //******************* For Date Range Picker *****************//


    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

}