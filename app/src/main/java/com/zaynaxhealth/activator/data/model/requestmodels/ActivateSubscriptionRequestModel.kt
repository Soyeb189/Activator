package com.zaynaxhealth.activator.data.model.requestmodels


import com.google.gson.annotations.SerializedName

data class ActivateSubscriptionRequestModel(
    @SerializedName("firstName")
    val firstName: String, // Hasibur
    @SerializedName("lastName")
    val lastName: String, // Rahman
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?, // 2021-01-08
    @SerializedName("gender")
    val gender: String?, // Male
    @SerializedName("contactNumber")
    val contactNumber: String, // 01712345678
    @SerializedName("email")
    val email: String?, // hasib@email.com
    @SerializedName("identityVerificationDocumentType")
    val identityVerificationDocumentType: String?,
    @SerializedName("identityVerificationNumber")
    val identityVerificationNumber: String?,
    @SerializedName("nomineePhoneNumber")
    val nomineePhoneNumber: String?,
     @SerializedName("spouseName")
    val spouseName: String?,
    @SerializedName("childName1")
    val childName1: String?,
    @SerializedName("childName2")
    val childName2: String?,
    @SerializedName("relationshipWithNominee")
    val relationshipWithNominee: String?,
    @SerializedName("packageId")
    val packageId: String, // 602de69f15f5b42c74414f19
    @SerializedName("comment")
    val comment: String?, // Random comment
    @SerializedName("paymentMethod")
    val paymentMethod: String?, // Random comment
    @SerializedName("deviceSource")
    val deviceSource: String?,
    @SerializedName("subPackSource")
    val subPackSource: String?,


)