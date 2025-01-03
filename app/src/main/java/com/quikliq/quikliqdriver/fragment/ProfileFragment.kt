package com.quikliq.quikliqdriver.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.activities.*
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var fm: FragmentManager
    private var edit_BT: Button? = null
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var parent_profile: LinearLayout? = null
    private var userName_profile_TV: TextView? = null
    private var user_image_IV: ImageView? = null
    private var order_history_TV: TextView? = null
    private var your_review_Tv:TextView?= null
    private var change_password_TV: TextView? = null
    private var contact_us_TV: TextView? = null
    private var toolbar_title: TextView? = null
    private var sign_out_Tv:TextView? = null
    private var statusSwitch:SwitchCompat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        parent_profile = view.findViewById(R.id.parent_profile)
        userName_profile_TV = view.findViewById(R.id.userName_profile_TV)
        user_image_IV = view.findViewById(R.id.user_image_IV)
        statusSwitch = view.findViewById(R.id.statusSwitch)
        utility = Utility()
        pd = ProgressDialog(activity)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        order_history_TV = view.findViewById(R.id.order_history_TV)
        edit_BT = view.findViewById(R.id.edit_BT)
        sign_out_Tv = view.findViewById(R.id.sign_out_Tv)
        change_password_TV = view.findViewById(R.id.change_password_TV)
        contact_us_TV = view.findViewById(R.id.contact_us_TV)
        toolbar_title = view.findViewById(R.id.toolbar_title)
        your_review_Tv = view.findViewById(R.id.your_review_Tv)
        toolbar_title!!.text = "Profile"
        edit_BT?.setOnClickListener(this)
        contact_us_TV?.setOnClickListener(this)
        order_history_TV?.setOnClickListener(this)
        change_password_TV?.setOnClickListener(this)
        sign_out_Tv?.setOnClickListener(this)
        your_review_Tv?.setOnClickListener(this)
        profileApiCall()
        statusSwitch!!.setOnCheckedChangeListener { _, b ->
            if (b) {
                UpdateDriverStatusApiCall("1")
            } else {
                UpdateDriverStatusApiCall("0")
            }
        }
        return view
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.edit_BT -> startActivity(Intent(activity,EditProfileActivity::class.java))
            R.id.order_history_TV -> startActivity(Intent(activity,AddDocumentsActivity::class.java))
            R.id.your_review_Tv -> startActivity(Intent(activity,OrderHistory::class.java))
            R.id.change_password_TV -> startActivity(Intent(activity,ChangePasswordActivity::class.java))
            R.id.contact_us_TV -> startActivity(Intent(activity,ContactUsActivity::class.java))
            R.id.about_Tv -> Log.d("about", "about")
            R.id.sign_out_Tv -> logoutApiCall()
        }
    }

    companion object {
        var TAG = ProfileFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            return fragment
        }
    }


    private fun profileApiCall() {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.profile(Prefs.getString("userid", "")).enqueue(object : Callback<JsonObject> {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful) {
                        Log.d("responsedata", response.body().toString())
                        val responsedata = response.body().toString()

                        try {

                            val jsonObject = JSONObject(responsedata)
                            val status = jsonObject.optJSONObject("data").getInt("driver_status")

                            if(status==1)
                            {
                                statusSwitch?.isChecked = true
                            }else
                            {
                                statusSwitch?.isChecked = false
                            }

                            if (jsonObject.optBoolean("status")) {
                                userName_profile_TV!!.text =
                                    jsonObject.optJSONObject("data").optString("FirstName") + " " + jsonObject.optJSONObject(
                                        "data"
                                    ).optString("LastName")
//                                if(!jsonObject.optJSONObject("data").optString("profileimage").isNullOrEmpty()){
//                                utility!!.loadImageWithLoader(
//                                    activity,
//                                    jsonObject.optJSONObject("data").optString("profileimage"),
//                                    user_image_IV
//                                )}


                                Prefs.putString(
                                    "profileimage",
                                    jsonObject.optJSONObject("data").optString("profileimage")
                                )
                                Prefs.putString("FirstName", jsonObject.optJSONObject("data").optString("FirstName"))
                                Prefs.putString("LastName", jsonObject.optJSONObject("data").optString("LastName"))
                                Prefs.putString("Mobile", jsonObject.optJSONObject("data").optString("Mobile"))
                                Prefs.putString("Email", jsonObject.optJSONObject("data").optString("Email"))
                                Prefs.putString(
                                    "BusinesNname",
                                    jsonObject.optJSONObject("data").optString("BusinesNname")
                                )
                                Prefs.putString("BankName", jsonObject.optJSONObject("data").optString("BankName"))
                                Prefs.putString(
                                    "AccountNumber",
                                    jsonObject.optJSONObject("data").optString("AccountNumber")
                                )
                                Prefs.putString("IFSC", jsonObject.optJSONObject("data").optString("IFSC"))
                                Prefs.putString("Address", jsonObject.optJSONObject("data").optString("Address"))
                                Prefs.putString("UserType", jsonObject.optJSONObject("data").optString("UserType"))
                                Prefs.putString("userid", jsonObject.optJSONObject("data").optString("userid"))
                            } else {
                                utility!!.linear_snackbar(
                                    parent_profile!!,
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                        parent_profile!!,
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        } else {
            utility!!.linear_snackbar(
                parent_profile!!,
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }

    private fun UpdateDriverStatusApiCall(s: String) {

        if (utility!!.isConnectingToInternet(context)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.setDriverStatus(Prefs.getString("userid",""),s).enqueue(object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful)
                    {
                        Log.d("status",""+response.body())
                        val responsedata = response.body().toString()
                        val jsonObject = JSONObject(responsedata)

                        utility!!.linear_snackbar(
                            parent_profile!!,
                            jsonObject.optString("message"),
                            getString(R.string.close_up)
                        )
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                        parent_profile!!,
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        }

    }

    private fun logoutApiCall() {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.Logout(
                Prefs.getString("userid", "")
            ).enqueue(object : Callback<JsonObject> {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful) {
                        Log.d("responsedata", response.body().toString())
                        val responsedata = response.body().toString()
                        try {
                            val jsonObject = JSONObject(responsedata)
                            if (jsonObject.optBoolean("status")) {

                                utility!!.linear_snackbar(
                                    parent_profile!!,
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                                Prefs.clear()
                                activity!!.startActivity(Intent(activity, LoginActivity::class.java))
                                activity!!.finishAffinity()
                            } else {
                                utility!!.linear_snackbar(
                                    parent_profile!!,
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }else{
                        utility!!.linear_snackbar(
                            parent_profile!!,
                            response.message(),
                            getString(R.string.close_up)
                        )
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                        parent_profile!!,
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        } else {
            utility!!.linear_snackbar(
                parent_profile!!,
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }

}
