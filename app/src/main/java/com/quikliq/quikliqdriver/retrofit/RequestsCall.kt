package retrofit


import android.content.Context
import android.net.Uri
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.interfaces.ApiHelper
import com.quikliq.quikliqdriver.utilities.ImageCompressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class RequestsCall {


    fun login(mobile: String, password: String, device_type : String, device_token : String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.login("UserLogin",mobile, password, device_type, device_token,"3")
    }

    fun signup(first_name: String, last_name: String, email : String, password : String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.signup("Signup",first_name, last_name, email, password)
    }

    fun mobile(mobile_number: String, countryCode: String?): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.mobile("NewOtp",mobile_number,countryCode)
    }

    fun ForgetPassword(method : String,mobile: String,c_code: String,user_type:String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.ForgetPassword(method,mobile,c_code,user_type)
    }

    fun forgetPassword(ResetPassword :String,mobile:String,c_code:String,usertype:String):Call<JsonObject>
    {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.forgetPassword(ResetPassword,mobile,c_code,usertype)
    }

    fun saveAdditionalDetail(devicetype: String, firstname: String, lastname : String, mobile : String, email : String, password : String, businessname : String, bankname : String, accountnumber : String, ifsc : String, address : String, usertype : String, devicetoken : String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.saveAdditionalDetail("SaveAdditionalDetail",devicetype, firstname, lastname, mobile, email, password, businessname, bankname, accountnumber, ifsc, address,usertype,devicetoken)
    }

    fun profile(userID: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.profile("UserProfile",userID)
    }

    fun setDriverStatus(userid: String,status: String):Call<JsonObject>
    {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.setDriverStatus("UpdateDriverstatus",userid,status)
    }



    fun UpdateProfile(
        context: Context,
        userid: String,
        firstname: String,
        lastname: String,
        email: String,
        image: Uri?
    ): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        val imageCompressor = ImageCompressor()
        val path = imageCompressor.compressImage(context, image.toString())
        val fbody = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val body = MultipartBody.Part.createFormData("image",  File(path).name, fbody)
        val method1 = RequestBody.create(MediaType.parse("text/plain"), "UpdateProfile")
        val user_id = RequestBody.create(MediaType.parse("text/plain"), userid)
        val first = RequestBody.create(MediaType.parse("text/plain"), firstname)
        val l_name = RequestBody.create(MediaType.parse("text/plain"), lastname)
        val email_id = RequestBody.create(MediaType.parse("text/plain"), email)

        return api.UpdateProfile(method1,user_id,first,l_name,email_id,body)
    }



    fun ContactUs(userid: String, email: String, subject : String, message : String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.ContactUs("ContactUs",userid, email, subject, message)
    }

    fun Logout(userid: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.Logout("Logout",userid)
    }
    fun ChangePassword(userid: String, oldpassword: String, newpassword : String, confirmpassword : String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.ChangePassword("ChangePassword",userid,oldpassword,newpassword,confirmpassword)
    }


    fun UpdateProfileWithoutImage(userid: String,firstname:String,lastname:String,email:String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.UpdateProfileWithoutImage("UpdateProfile",userid,firstname,lastname,email)
    }

    fun DriverOrderRequest(userid: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.DriverOrderRequest("DriverOrderRequest",userid)
    }

    fun DriverOrderAcceptReject(userid: String,orderid: String, status:String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.DriverOrderAcceptReject("DriverOrderAcceptReject",userid,orderid,status)
    }

    fun UploadIdProof(
        context: Context,
        userid: String,
        type: String,
        proof_status:String,
        image: Uri?
    ): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        val imageCompressor = ImageCompressor()
        val path = imageCompressor.compressImage(context, image.toString())
        val fbody = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val body = MultipartBody.Part.createFormData("image",  File(path).name, fbody)
        val method1 = RequestBody.create(MediaType.parse("text/plain"), "UploadIdProof")
        val user_id = RequestBody.create(MediaType.parse("text/plain"), userid)
        val type = RequestBody.create(MediaType.parse("text/plain"), type)
        val proof_status = RequestBody.create(MediaType.parse("text/plain"), proof_status)

        return api.UploadIdProof(method1,user_id,type,proof_status,body)
    }

    fun ProviderRuningOrders(userid: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.ProviderRuningOrders("DriverRunningOrder",userid)
    }

    fun UpdateOrderStatus(orderid: String, status:String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.UpdateOrderStatus("UpdateOrderStatus",orderid,status)
    }

    fun OrderDelivered(userid: String,orderid: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.OrderDelivered("OrderDelivered",userid,orderid)
    }

    fun documentStatus(userid: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.documentStatus("DocumentStatus",userid)
    }

    fun OrderHistory(userID: String): Call<JsonObject> {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.OrderHistory("GetMyOrders",userID)
    }

    fun IsDiriverVerified(userid: String):Call<JsonObject>
    {
        val apiCall = ApiCall()
        val api = apiCall.apiCall().create(ApiHelper::class.java)
        return api.IsDriverVerified("DriverStatus",userid)
    }
}
