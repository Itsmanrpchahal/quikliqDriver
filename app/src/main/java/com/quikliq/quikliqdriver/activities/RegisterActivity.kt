package com.quikliq.quikliqdriver.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.constants.Constant
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.activity__register.*
import org.json.JSONException
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var otp: String? = null
    private var phone_number: String? = null
    private var nointernet: RelativeLayout? = null
    private var screendata: RelativeLayout? = null
    var notC = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__register)
        utility = Utility()
        nointernet = findViewById(R.id.nointernet)
        screendata = findViewById(R.id.screendata)
        pd = ProgressDialog(this@RegisterActivity)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        otp = intent.getStringExtra("otp")
        phone_number = intent.getStringExtra("mobile")
        signUp_TV.setOnClickListener(this)
        nextScreen.setOnClickListener(this)
    }

    //Check Internet Connection
    private var broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val notConnected = p1!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)

            if (notConnected)
            {
                nointernet?.visibility = View.VISIBLE
                screendata?.visibility = View.GONE
                notC = "1"
            }else{
                nointernet?.visibility = View.GONE
                screendata?.visibility = View.VISIBLE
                notC = "0"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(notC.equals("1"))
        {
            finishAffinity()
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.signUp_TV -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.nextScreen -> checkValidation()
        }
    }

    private fun checkValidation() {
        when {
            firstName.text.isEmpty() -> {
                firstName.requestFocus()
                firstName.error = getString(R.string.txt_Error_required)
            }
            lastName.text.isEmpty() -> {
                lastName.requestFocus()
                lastName.error = getString(R.string.txt_Error_required)
            }
            !utility!!.isValidEmail(registerEmail!!.text.toString().trim { it <= ' ' }) -> {
                registerEmail.requestFocus()
                registerEmail.error = getString(R.string.Invalid_email)
            }
            password_ET.text.length < 6 -> {
                password_ET.requestFocus()
                password_ET.error = getString(R.string.error_password)
            }
            else -> signupApiCall(
                firstName.text.toString(),
                lastName.text.toString(),
                registerEmail!!.text.toString(),
                password_ET.text.toString()
            )
        }
    }

    private fun signupApiCall(
        firstName: String,
        lastName: String,
        registerEmail: String,
        password_ET: String
    ) {
        hideKeyboard()
        if (utility!!.isConnectingToInternet(this@RegisterActivity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.signup(firstName, lastName, registerEmail, password_ET)
                .enqueue(object : Callback<JsonObject> {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val responsedata = response.body().toString()
                            try {
                                val jsonObject = JSONObject(responsedata)
                                if (jsonObject.optBoolean("status")) {

                                    requestsCall.saveAdditionalDetail(
                                        "1",
                                        firstName,
                                        lastName,
                                        phone_number!!,
                                        registerEmail,
                                        password_ET,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "1",
                                        "3",
                                        Prefs.getString(Constant.FCM_TOKEN, "")
                                    ).enqueue(object : Callback<JsonObject> {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        override fun onResponse(
                                            call: Call<JsonObject>,
                                            response: Response<JsonObject>
                                        ) {
                                            pd!!.dismiss()
                                            if (response.isSuccessful) {
                                                val responsedata = response.body().toString()
                                                Log.d("response", response.body().toString())
                                                try {
                                                    val jsonObject = JSONObject(responsedata)
                                                    if (jsonObject.optBoolean("status")) {
                                                        Prefs.putString(
                                                            "userid",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "userid"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "FirstName",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "FirstName"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "LastName",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "LastName"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "Mobile",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "Mobile"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "Email",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "Email"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "BusinesNname",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "BusinesNname"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "BankName",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "BankName"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "AccountNumber",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "AccountNumber"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "IFSC",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "IFSC"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "Address",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "Address"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "UserType",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "UserType"
                                                            )
                                                        )
                                                        Prefs.putString(
                                                            "profileimage",
                                                            jsonObject.optJSONObject("data").optString(
                                                                "profileimage"
                                                            )
                                                        )
                                                        Prefs.putBoolean(
                                                            Constant.IS_LOGGED_IN,
                                                            true
                                                        )
                                                        startActivity(
                                                            Intent(
                                                                this@RegisterActivity,
                                                                HomeActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    } else {
                                                        utility!!.relative_snackbar(
                                                            parent_signup!!,
                                                            jsonObject.optString("message"),
                                                            getString(R.string.close_up)
                                                        )
                                                    }

                                                } catch (e: JSONException) {
                                                    e.printStackTrace()
                                                }

                                            } else {
                                                utility!!.relative_snackbar(
                                                    parent_signup!!,
                                                    response.message(),
                                                    getString(R.string.close_up)
                                                )
                                            }

                                        }

                                        override fun onFailure(
                                            call: Call<JsonObject>,
                                            t: Throwable
                                        ) {
                                            pd!!.dismiss()
                                            utility!!.relative_snackbar(
                                                parent_signup!!,
                                                getString(R.string.no_internet_connectivity),
                                                getString(R.string.close_up)
                                            )
                                        }
                                    })

                                } else {
                                    utility!!.relative_snackbar(
                                        parent_signup!!,
                                        jsonObject.optString("message"),
                                        getString(R.string.close_up)
                                    )
                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        } else {
                            utility!!.relative_snackbar(
                                parent_signup!!,
                                response.message(),
                                getString(R.string.close_up)
                            )
                        }

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        pd!!.dismiss()
                        utility!!.relative_snackbar(
                            parent_signup!!,
                            getString(R.string.no_internet_connectivity),
                            getString(R.string.close_up)
                        )
                    }
                })
        } else {
            utility!!.relative_snackbar(
                parent_signup!!,
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }

    private fun hideKeyboard() {
        val imm =
            this@RegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

}
