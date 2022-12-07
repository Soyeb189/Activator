package com.zaynaxhealth.activator.ui.success_fail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.data.model.EventBusHistory
import com.zaynaxhealth.activator.data.model.SuccessFail
import com.zaynaxhealth.activator.databinding.FragmentSuccessOrFailBinding
import com.zaynaxhealth.activator.utilities.SUCCESS_FAIL
import org.greenrobot.eventbus.EventBus
import androidx.navigation.NavOptions
import com.zaynaxhealth.activator.utilities.CASH


class SuccessOrFailFragment : Fragment() {
    private lateinit var binding: FragmentSuccessOrFailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessOrFailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val successFail = arguments?.get(SUCCESS_FAIL) as SuccessFail

        binding.tvTitle.text = successFail.title
        binding.tvMessageSubTitle.text = successFail.subTitle
        binding.btnAction.text = successFail.buttonMessage

        clickEvents(successFail)

    }

    private fun clickEvents(successFail: SuccessFail) {

        binding.btnAction.setOnClickListener {
//            if (successFail.title.contains("thank",true) && successFail.from == CASH){
//                EventBus.getDefault().post(EventBusHistory())
//                findNavController().navigate(R.id.userNumberVerificationFragment)
//            }else{
//                findNavController().popBackStack()
//            }
            EventBus.getDefault().post(EventBusHistory())
            findNavController().navigate(R.id.userNumberVerificationFragment)
        }

        binding.btnBack.setOnClickListener {
//            if (successFail.title.contains("thank",true) && successFail.from == CASH){
//                EventBus.getDefault().post(EventBusHistory())
//                findNavController().navigate(R.id.userNumberVerificationFragment)
//            }else{
//                findNavController().popBackStack()
//            }
            EventBus.getDefault().post(EventBusHistory())
            findNavController().navigate(R.id.userNumberVerificationFragment)
        }
    }
}