package com.quikliq.app.fragment


import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import com.quikliq.quikliqdriver.adapters.IncomingOrdersAdapter
import com.quikliq.quikliqdriver.interfaces.OrderStatusUpdate
import com.quikliq.quikliqdriver.models.HistoryModel
import com.quikliq.quikliqdriver.utilities.Utility
import org.json.JSONException
import org.json.JSONObject
import com.quikliq.quikliqdriver.utilities.Prefs
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IncomingOrdersFragment : Fragment(), OrderStatusUpdate {
    private var rvHome: RecyclerView? = null
    private var historyModelArraylist: ArrayList<HistoryModel>? = null
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var incomingOrdersAdapter: IncomingOrdersAdapter? = null
    private var products: ArrayList<String>? = null
    private var price: ArrayList<String>? = null
    private var quantity: ArrayList<String>? = null
    private var items: ArrayList<String>? = null
    private var no_dataRL: RelativeLayout? = null
    private var msgTV: TextView? = null
    private var titleTV: TextView? = null
    private var swipeview: SwipeRefreshLayout? = null
    private var timer: Timer? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_incoming_orders, container, false)
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
        swipeview = view.findViewById(R.id.swipeview)
        swipeview!!.setColorSchemeResources(
            R.color.colorPrimaryDark,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        swipeview!!.setOnRefreshListener {
            incomingOrdersApiCall()
        }
        orderStatusUpdate = this
        return view
    }

    override fun onResume() {
        super.onResume()
        val handler = Handler()
        timer = Timer()
        val doTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    incomingOrdersApiCall()
                }
            }
        }
        timer!!.schedule(doTask, 0, 30000)
    }

    companion object {
        var orderStatusUpdate: OrderStatusUpdate? = null
    }

    private fun incomingOrdersApiCall() {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.DriverOrderRequest(Prefs.getString("userid", ""))
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
                                    historyModelArraylist = ArrayList()
                                    for (i in 0 until jsonObject.optJSONArray("data").length()) {
                                        val jsonObject1 =
                                            jsonObject.optJSONArray("data").optJSONObject(i)
                                        val historyModel = HistoryModel()
                                        historyModel.order_id = jsonObject1.optString("order_id")
                                        historyModel.driver_id = jsonObject1.optString("driver_id")
                                        historyModel.provider_id =
                                            jsonObject1.optString("provider_id")
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
                                        historyModel.user_name = jsonObject1.optString("user_name")
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
                                        incomingOrdersAdapter = IncomingOrdersAdapter(
                                            activity!!,
                                            historyModelArraylist!!
                                        )
                                        val mLayoutManager =
                                            LinearLayoutManager(activity!!.applicationContext)
                                        rvHome!!.layoutManager = mLayoutManager
                                        rvHome!!.adapter = incomingOrdersAdapter
                                    } else {
                                        rvHome!!.visibility = View.GONE
                                        no_dataRL!!.visibility = View.VISIBLE
                                        titleTV!!.text = "Relax"
                                        msgTV!!.text = "No Orders found"
                                    }
                                } else {
                                    rvHome!!.visibility = View.GONE
                                    no_dataRL!!.visibility = View.VISIBLE
                                    titleTV!!.text = "Relax"
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

    private fun orderStatusApiCall(order_id: String, status: String) {
        if (utility!!.isConnectingToInternet(activity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.DriverOrderAcceptReject(Prefs.getString("userid", ""), order_id, status)
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
                                    incomingOrdersAdapter = null
                                    incomingOrdersApiCall()
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
        orderStatusApiCall(order_id, status)
    }

    /**
     * canceling timer task on pause
     */
    override fun onPause() {
        super.onPause()
        timer!!.cancel()
    }

    /**
     * canceling timer task on stop
     */
    override fun onStop() {
        super.onStop()
        timer!!.cancel()
    }

    /**
     * on destroying acttivity cancel timer
     */
    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }
}
