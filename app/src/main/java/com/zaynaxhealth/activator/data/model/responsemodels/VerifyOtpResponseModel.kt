package com.zaynaxhealth.activator.data.model.responsemodels



class VerifyOtpResponseModel (var data: String) : BaseResponseModel() {
    override fun toString(): String {
        return "VerifyOtpResponseModel(data='$data')"
    }
}