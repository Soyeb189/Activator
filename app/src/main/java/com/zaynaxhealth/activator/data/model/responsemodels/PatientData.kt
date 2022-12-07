package com.zaynaxhealth.activator.data.model.responsemodels

import com.google.gson.annotations.SerializedName

data class PatientData(
    @SerializedName("dateOfBirth")
    var dateOfBirth: String, // 2021-01-08
    @SerializedName("firstName")
    val firstName: String, // Hasibur
    @SerializedName("gender")
    var gender: String, // Male
    @SerializedName("lastName")
    val lastName: String // Rahman
)