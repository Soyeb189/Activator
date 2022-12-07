package com.zaynaxhealth.activator.data.api


import com.zaynaxhealth.activator.data.model.requestmodels.*
import com.zaynaxhealth.activator.data.model.responsemodels.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiList {

    @POST("auth_service/auth/login")
    suspend fun signInWithPhone(@Body requestModel: SignInWithPhoneNumberRequestModel): Response<BaseResponse<String>>

    @POST("auth_service/auth/login")
    suspend fun signInWithEmail(@Body requestModel: SignInWithEmailRequestModel): Response<BaseResponse<String>>

    @GET("activator_service/auth-check/get-profile")
    suspend fun getActivatorInfo(
    ): Response<BaseResponse<ActivatorInfoResponseModel>>

    @POST("activator_service/activate-subscription/send-otp")
    suspend fun sendOtp(
        @Body requestModel: SendOtpRequestModel?
    ): Response<BaseResponse<String>>

    @POST("activator_service/activate-subscription/verify-otp")
    suspend fun verifyOtp(
        @Body requestModel: VerifyOtpRequestModel?
    ): Response<BaseResponse<String>>

    @GET("activator_service/activate-subscription/fetch-user")
    suspend fun getPatientInfo(
        @Query("phoneNumber") phoneNumber: String?
    ): Response<BaseResponse<PatientData>>

    @GET("product_service/health-packages")
    suspend fun getPackages(): Response<PackageResponseModel>

    @POST("activator_service/activate-subscription/activate")
    suspend fun activateSubscription(
        @Body requestModel: ActivateSubscriptionRequestModel?
    ): Response<BaseResponse<String>>

    @POST("activator_service/activate-subscription/activate_V2")
    suspend fun activateSubscriptionV2(
        @Body requestModel: ActivateSubscriptionRequestModel?
    ): Response<PackageSubscribeResponse>

    @POST("payment_service/payment/create/ssl")
    suspend fun bkashUrl(
        @Body requestModel : PaymentUrlRequest
    ) : Response<PaymentUrlResponse>

    @POST("payment_service/payment/create/nagad")
    suspend fun nagadUrl(
        @Body requestModel : PaymentUrlRequest
    ) : Response<PaymentUrlResponse>

    @POST("payment_service/payment/create/ssl/card")
    suspend fun sslUrl(
        @Body requestModel : PaymentUrlRequest
    ) : Response<PaymentUrlResponse>

    @GET("activator_service/activate-subscription/check-health-insurance")
    suspend fun getInsuranceStatus(
        @Query("packageId") packageId: String?
    ): Response<BaseResponse<Boolean>>

    @GET("activator_service/activate-subscription/is-family-package")
    suspend fun isFamilyPackage(
        @Query("packageId") packageId: String?
    ): Response<BaseResponse<Boolean>>

    @GET("activator_service/activation-history/fetch-activation-history")
    suspend fun getActivationHistory(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ):Response<BaseResponse<ActivatedHistoryList>>

    @GET("activator_service/activation-history/fetch-activation-history")
    suspend fun getActivationHistoryPaid(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ):Response<PaidHistoryResponseModel>

    @POST("activator_service/activation-history/subscription-update-by-transactionId")
    suspend fun setTranasctionNumber(
        @Body requestModel: TransactionIdUpdateRequestModel?
    ): Response<TransactionIdUpdateResponseModel>

    @GET("order_service/health-package/isExist-subscribed-packages")
    suspend fun getIsExistSubscribedPackages(
        @Query("mobileNumber") type: String
    ):Response<BooleanResponse>

    @GET("activator_service/activate-subscription/activator-package-amount-and-discount-amount")
    suspend fun getPackageDiscountAmount(
        @Query("packageId") type: String
    ):Response<DiscountAmountResponseModel>

    @FormUrlEncoded
    @POST("activator_service/activation-history/change-payment-method")
    suspend fun changePaymentMethod(
        @Field("orderId") orderId:String
    ):Response<BaseResponse<Any?>?>

    @FormUrlEncoded
    @POST("activator_service/activation-history/resend-payment-url")
    suspend fun resendPaymentUrl(
        @Field("orderId") orderId:String
    ):Response<BaseResponse<Any?>?>

}