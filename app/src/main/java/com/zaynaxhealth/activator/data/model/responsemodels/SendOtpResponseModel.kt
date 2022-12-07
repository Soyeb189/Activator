package com.zaynaxhealth.activator.data.model.responsemodels



class SendOtpResponseModel (var data: String) : BaseResponseModel() {
    override fun toString(): String {
        return "SendOtpResponseModel(data='$data')"
    }
}