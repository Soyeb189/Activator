package com.zaynaxhealth.activator.data.model.responsemodels



class AuthLandingResponseModel (var data: String) : BaseResponseModel() {
    override fun toString(): String {
        return "AuthLandingResponseModel(data='$data')"
    }
}