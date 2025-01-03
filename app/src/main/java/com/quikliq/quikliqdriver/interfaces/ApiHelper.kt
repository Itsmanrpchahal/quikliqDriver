package com.quikliq.quikliqdriver.interfaces

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiHelper {

    @FormUrlEncoded
    @POST(".")
    fun login(@Field("method") UserLogin: String ,@Field("mobile") mobile: String, @Field("password") password: String, @Field("devicetype") devicetype: String, @Field("devicetoken") devicetoken: String,@Field("usetype") usertype: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun signup(@Field("method") Signup: String ,@Field("fname") fname: String, @Field("lname") lname: String, @Field("email") email: String, @Field("password") password: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun mobile(
        @Field("method") NewOtp: String, @Field("mobile") mobile: String,@Field("c_code") countryCode: String?
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun ForgetPassword(@Field("method") ForgetPassword: String,@Field("mobile")mobile: String,@Field("c_code") c_code:String,@Field("user_type") usertype:String)
            :Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun saveAdditionalDetail(@Field("method") SaveAdditionalDetail: String ,@Field("devicetype") devicetype: String, @Field("firstname") firstname: String, @Field("lastname") lastname: String, @Field("mobile") mobile: String, @Field("email") email: String, @Field("password") password: String, @Field("businessname") businessname: String, @Field("bankname") bankname: String, @Field("accountnumber") accountnumber: String, @Field("ifsc") ifsc: String, @Field("address") address: String, @Field("usertype") usertype: String, @Field("devicetoken") devicetoken: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun profile(@Field("method") UserLogin: String ,@Field("userid") userid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun forgetPassword(@Field("method") ForgetPassword: String,@Field("mobile")mobile: String,@Field("c_code") c_code:String,@Field("usertype") usertype:String)
            :Call<JsonObject>

    @Multipart
    @POST(".")
    fun AddProduct(@Part("method") AddProduct: RequestBody, @Part("userid") userid: RequestBody, @Part("category") category: RequestBody, @Part("productname") productname: RequestBody, @Part("price") price: RequestBody, @Part("quantity") quantity: RequestBody, @Part("description") description: RequestBody, @Part files: MultipartBody.Part): Call<JsonObject>

    @Multipart
    @POST(".")
    fun UpdateProfile(@Part("method") UpdateProfile: RequestBody ,@Part("userid") userid: RequestBody ,@Part("firstname") firstname: RequestBody ,@Part("lastname") lastname: RequestBody ,@Part("email") email: RequestBody, @Part files: MultipartBody.Part): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun UpdateProfileWithoutImage(@Field("method") UpdateProfile: String ,@Field("userid") userid: String ,@Field("firstname") firstname: String ,@Field("lastname") lastname: String ,@Field("email") email: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun ContactUs(@Field("method") ContactUs: String ,@Field("userid") userid: String, @Field("email") email: String, @Field("subject") subject: String, @Field("message") message: String): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun Logout(@Field("method") Logout: String ,@Field("userid") userid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun ChangePassword(@Field("method") ChangePassword: String ,@Field("userid") userid: String,@Field("oldpassword") oldpassword: String,@Field("newpassword") newpassword: String,@Field("confirmpassword") confirmpassword: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun GetAllProduct(@Field("method") GetAllProduct: String ,@Field("userid") userid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun ProductDetail(@Field("method") ProductDetail: String ,@Field("userid") userid: String, @Field("productid") productid: String): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun DriverOrderRequest(@Field("method") DriverOrderRequest: String ,@Field("userid") userid: String): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun DriverOrderAcceptReject(@Field("method") DriverOrderAcceptReject: String ,@Field("userid") userid: String,@Field("orderid") orderid: String,@Field("status") status: String): Call<JsonObject>


    @Multipart
    @POST(".")
    fun UploadIdProof(@Part("method") UploadIdProof: RequestBody, @Part("userid") userid: RequestBody, @Part("proof_type") proof_type: RequestBody, @Part("proof_status") proof_status: RequestBody, @Part files: MultipartBody.Part): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun UpdateOrderStatus(@Field("method") UpdateOrderStatus: String, @Field("orderid") orderid: String, @Field("status") status:String ): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun ProviderRuningOrders(@Field("method") ProviderRuningOrders: String, @Field("userid") userid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun OrderDelivered(@Field("method") OrderDelivered: String ,@Field("userid") userid: String,@Field("orderid") orderid: String): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun documentStatus(@Field("method") DocumentStatus: String ,@Field("userid") userid: String): Call<JsonObject>



    @FormUrlEncoded
    @POST(".")
    fun OrderHistory(@Field("method") OrderHistory: String ,@Field("userid") userid: String): Call<JsonObject>


    @FormUrlEncoded
    @POST(".")
    fun IsDriverVerified(@Field("method") DriverStatus : String,@Field("userid") userid: String):Call<JsonObject>

    @FormUrlEncoded
    @POST(".")
    fun setDriverStatus(@Field("method") onlineStatus:String,@Field("userid") userid: String,@Field("status") status :String):Call<JsonObject>

}
