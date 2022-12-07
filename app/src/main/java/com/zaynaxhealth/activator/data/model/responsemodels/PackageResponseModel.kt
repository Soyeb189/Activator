package com.zaynaxhealth.activator.data.model.responsemodels


import com.google.gson.annotations.SerializedName

data class PackageResponseModel(
    @SerializedName("data")
    val data: MutableList<Data>,
){
    data class Data(
        @SerializedName("description")
        val description: Description,
        @SerializedName("duration")
        val duration: Int, // 365
        @SerializedName("id")
        val id: String, // 602de69f15f5b42c74414f19
        @SerializedName("isPopular")
        val isPopular: Boolean, // false
        @SerializedName("name")
        val name: Name,
        @SerializedName("packageType")
        val packageType: String, // GENERAL
        @SerializedName("price")
        val price: Price
    ) {
        data class Description(
            @SerializedName("bn")
            val bn: String,
            @SerializedName("en")
            val en: String // - Unlimied Doctor Consultation (Audio/Video)- Pro Active Doctor Consultation & Follow Up for COVID Patient (Until Recovery)- COVID Test Sample Collection & Report Delivery (Any 1 Member- Test Price Included in Package)- Medicine Home Delivery (Up to 10% Discount)- Oxygen Rental Assistance- Hospital Admission & ICU Booking Assistance- Ambulance Booking Assistance- Nurse Booking Assistance
        )

        data class Name(
            @SerializedName("bn")
            val bn: String,
            @SerializedName("en")
            val en: String // Family Covid Care
        )

        data class Price(
            @SerializedName("discountPercent")
            val discountPercent: Int, // 25
            @SerializedName("discountPrice")
            val discountPrice: Int, // 3999
            @SerializedName("regularPrice")
            val regularPrice: Int // 5332
        )
    }
}