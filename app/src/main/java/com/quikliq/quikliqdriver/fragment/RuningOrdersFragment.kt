package com.quikliq.app.fragment


import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.adapters.RuningOrderAdapter
import com.quikliq.quikliqdriver.interfaces.RuningStatusUpdate
import com.quikliq.quikliqdriver.models.HistoryModel
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import org.json.JSONException
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RuningOrdersFragment : Fragment(), RuningStatusUpdate {
    private var rvHome: RecyclerView? = null
    private var historyModelArraylist: ArrayList<HistoryModel>? = null
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var runingOrdersAdapter: RuningOrderAdapter? = null
    private var no_dataRL: RelativeLayout? = null
    private var msgTV: TextView? = null
    private var titleTV: TextView? = null
    private var swipeview: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_order, container, false)
        utility = Utility()
        pd = ProgressDialog(activity)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        rvHome = view.findViewById(R.id.rvHome)
        no_dataRL = view.findViewById(R.id.no_dataRL)
        msgTV = view.findViewById(R.id.msgTV)
        titleTV = view.findViewById(R.id.titleTV)
        runingStatusUpdate = this
        swipeview = view.findViewById(R.id.swipeview)
        swipeview!!.setColorSchemeResources(
            R.color.colorPrimaryDark,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        swipeview!!.setOnRefreshListener {
            runingOrdersApiCall()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        runingOrdersApiCall()
    }

    private fun runingOrdersApiCall() {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.ProviderRuningOrders(Prefs.getString("userid", ""))
                .enqueue(object : Callback<JsonObject> {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        pd!!.dismiss()
                        swipeview!!.isRefreshing = false

                        if (response.isSuccessful) {
                            Log.d("incoming_order", response.body().toString())
                            val responsedata = response.body().toString()
                            try {
                                val jsonObject = JSONObject(responsedata)
                                if (jsonObject.optBoolean("status")) {
                                    val data_object = jsonObject.optJSONObject("data")
                                    historyModelArraylist = ArrayList()
                                    for (i in 0 until jsonObject.optJSONArray("data").length()) {
                                        val jsonObject1 =
                                            jsonObject.optJSONArray("data").optJSONObject(i)
                                        val historyModel = HistoryModel()
                                        historyModel.order_id = jsonObject1.optString("orderid")
                                        historyModel.driver_id = jsonObject1.optString("driver_id")
                                        historyModel.provider_id =
                                            jsonObject1.optString("businessid")
                                        historyModel.provider_name =
                                            jsonObject1.optString("provider_name")
                                        historyModel.provider_image =
                                            jsonObject1.optString("provider_image")
                                        historyModel.provider_latitude =
                                            jsonObject1.optString("provider_latitude")
                                        historyModel.provider_longitude =
                                            jsonObject1.optString("provider_longitude")
                                        historyModel.provider_address =
                                            jsonObject1.optString("provider_address")
                                        historyModel.user_name =
                                            jsonObject1.optString("user_first_name") + " " + jsonObject1.optString(
                                                "user_last_name"
                                            )
                                        historyModel.user_latitude =
                                            jsonObject1.optString("user_latitude")
                                        historyModel.user_longitude =
                                            jsonObject1.optString("user_longitude")
                                        historyModel.user_address =
                                            jsonObject1.optString("user_address")
                                        historyModelArraylist!!.add(historyModel)
                                    }

                                    if (historyModelArraylist!!.isNotEmpty()) {
                                        rvHome!!.visibility = View.VISIBLE
                                        no_dataRL!!.visibility = View.GONE
                                        runingOrdersAdapter =
                                            RuningOrderAdapter(activity!!, historyModelArraylist!!)
                                        val mLayoutManager =
                                            LinearLayoutManager(activity!!.applicationContext)
                                        rvHome!!.layoutManager = mLayoutManager
                                        rvHome!!.adapter = runingOrdersAdapter
                                    } else {
                                        rvHome!!.visibility = View.GONE
                                        no_dataRL!!.visibility = View.VISIBLE
                                        titleTV!!.text = "Welcome"
                                        msgTV!!.text = "No Orders found"
                                    }
                                } else {
                                    rvHome!!.visibility = View.GONE
                                    no_dataRL!!.visibility = View.VISIBLE
                                    titleTV!!.text = "Welcome"
                                    msgTV!!.text = "No Orders found"
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        } else {
                            utility!!.relative_snackbar(
                                activity!!.findViewById(android.R.id.content),
                                response.message(),
                                getString(R.string.close_up)
                            )
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        pd!!.dismiss()
                        utility!!.relative_snackbar(
                            activity!!.findViewById(android.R.id.content),
                            getString(R.string.no_internet_connectivity),
                            getString(R.string.close_up)
                        )
                    }
                })
        } else {
            utility!!.relative_snackbar(
                activity!!.findViewById(android.R.id.content),
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }

    companion object {
        var runingStatusUpdate: RuningStatusUpdate? = null
    }

    private fun OrderDeliveredApiCall(order_id: String) {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.OrderDelivered(Prefs.getString("userid", ""),order_id).enqueue(object : Callback<JsonObject> {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful) {
                        Log.d("responsedata", response.body().toString())
                        val responsedata = response.body().toString()
                        try {
                            val jsonObject = JSONObject(responsedata)
                            if (jsonObject.optBoolean("status")) {
                                runingOrdersApiCall()
                            } else {
                                utility!!.linear_snackbar(
                                    activity!!.findViewById(android.R.id.content),
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {
                        utility!!.linear_snackbar(
                            activity!!.findViewById(android.R.id.content),
                            response.message(),
                            getString(R.string.close_up)
                        )
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        } else {
            utility!!.linear_snackbar(
                activity!!.findViewById(android.R.id.content),
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }


    override fun status(order_id: String, status: String) {
        OrderDeliveredApiCall(order_id)
    }


}
