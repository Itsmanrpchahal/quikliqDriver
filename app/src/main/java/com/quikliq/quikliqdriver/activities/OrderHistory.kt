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
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.adapters.HistoryAdapter
import com.quikliq.quikliqdriver.models.MyOrderModel
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.activity_order_history2.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderHistory : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var historyModelArraylist: ArrayList<MyOrderModel>? = null
    private var products: ArrayList<String>? = null
    private var price: ArrayList<String>? = null
    private var quantity: ArrayList<String>? = null
    private var items: ArrayList<String>? = null
    private var historyAdapter: HistoryAdapter? = null
    private var order_success: Boolean? = false
    private var no_dataRL: RelativeLayout? = null
    private var msgTV: TextView? = null
    private var titleTV: TextView? = null
    private var nointernet: RelativeLayout? = null
    private var screendata: RelativeLayout? = null
    var notC = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history2)
        toolbar = findViewById(R.id.toolbar)
        no_dataRL = findViewById(R.id.no_dataRL)
        msgTV = findViewById(R.id.msgTV)
        titleTV = findViewById(R.id.titleTV)
        nointernet = findViewById(R.id.nointernet)
        screendata = findViewById(R.id.screendata)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar_title!!.text = "Order History"
        utility = Utility()
        pd = ProgressDialog(this@OrderHistory)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        if (intent.hasExtra("order_success")) {
            order_success = true
        }

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
                orderHistoryApiCall()
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (order_success!!) {
                    startActivity(
                        Intent(
                            this@OrderHistory,
                            HomeActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    finish()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (order_success!!) {
            startActivity(
                Intent(
                    this@OrderHistory,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        } else {
            super.onBackPressed()
        }

        if(notC.equals("1"))
        {
            finishAffinity()
        }
    }

    private fun orderHistoryApiCall() {
        if (utility!!.isConnectingToInternet(this@OrderHistory)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.OrderHistory(Prefs.getString("userid", ""))
                .enqueue(object : Callback<JsonObject> {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        pd!!.dismiss()
                        if (response.isSuccessful) {
                            Log.d("responsedata", response.body().toString())
                            val responsedata = response.body().toString()
                            try {
                                val jsonObject = JSONObject(responsedata)
                                if (jsonObject.optBoolean("status")) {
                                    historyModelArraylist = ArrayList()
                                    for (i in 0 until jsonObject.optJSONArray("data").length()) {
                                        val jsonObject1 =
                                            jsonObject.optJSONArray("data").optJSONObject(i)
                                        val myOrderModel = MyOrderModel()
                                        myOrderModel.orderid = jsonObject1.optString("orderid")
                                        myOrderModel.totalprice =
                                            jsonObject1.optString("totalprice")
                                        myOrderModel.ordernote = jsonObject1.optString("ordernote")
                                        products = ArrayList()
                                        for (j in 0 until jsonObject.optJSONArray("data").optJSONObject(
                                            i
                                        ).optJSONArray("products").length()) {
                                            products!!.add(
                                                jsonObject.optJSONArray("data").optJSONObject(
                                                    i
                                                ).optJSONArray("products").optString(j)
                                            )
                                        }
                                        myOrderModel.products = products
                                        price = ArrayList()
                                        for (k in 0 until jsonObject.optJSONArray("data").optJSONObject(
                                            i
                                        ).optJSONArray("price").length()) {
                                            price!!.add(
                                                jsonObject.optJSONArray("data").optJSONObject(
                                                    i
                                                ).optJSONArray("price").optString(k)
                                            )
                                        }
                                        myOrderModel.price = price
                                        quantity = ArrayList()
                                        for (l in 0 until jsonObject.optJSONArray("data").optJSONObject(
                                            i
                                        ).optJSONArray("quantity").length()) {
                                            quantity!!.add(
                                                jsonObject.optJSONArray("data").optJSONObject(
                                                    i
                                                ).optJSONArray("quantity").optString(l)
                                            )
                                        }
                                        myOrderModel.quantity = quantity
                                        myOrderModel.status = jsonObject1.optString("status")
                                        myOrderModel.delivertime =
                                            jsonObject1.optString("delivertime")
                                        myOrderModel.datetime = jsonObject1.optString("datetime")
                                        myOrderModel.UserName = jsonObject1.optString("UserName")
                                        myOrderModel.Business_latitude =
                                            jsonObject1.optString("Business_latitude")
                                        myOrderModel.Business_longitude =
                                            jsonObject1.optString("Business_longitude")
                                        myOrderModel.customer_latitude =
                                            jsonObject1.optString("customer_latitude")
                                        myOrderModel.customer_longitude =
                                            jsonObject1.optString("customer_longitude")
                                        myOrderModel.BusinessImage =
                                            jsonObject1.optString("BusinessImage")
                                        myOrderModel.BusinessName =
                                            jsonObject1.optString("BusinessName")
                                        historyModelArraylist!!.add(myOrderModel)
                                    }

                                    if (historyModelArraylist!!.isNotEmpty()) {
                                        historyAdapter = HistoryAdapter(
                                            this@OrderHistory,
                                            historyModelArraylist!!
                                        )
                                        val mLayoutManager =
                                            LinearLayoutManager(this@OrderHistory.applicationContext)
                                        historyRV!!.layoutManager = mLayoutManager
                                        historyRV!!.adapter = historyAdapter
                                    }

                                } else {
                                    utility!!.linear_snackbar(
                                        parent_order_history!!,
                                        jsonObject.optString("message"),
                                        getString(R.string.close_up)
                                    )
                                    no_dataRL?.visibility = View.VISIBLE
                                    msgTV?.setText("")
                                    titleTV?.setText("No Orders")
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        } else {
                            utility!!.linear_snackbar(
                                parent_order_history!!,
                                response.message(),
                                getString(R.string.close_up)
                            )
                        }

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        pd!!.dismiss()
                        utility!!.linear_snackbar(
                            parent_order_history!!,
                            getString(R.string.no_internet_connectivity),
                            getString(R.string.close_up)
                        )
                    }
                })
        } else {
            utility!!.linear_snackbar(
                parent_order_history!!,
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }


}
