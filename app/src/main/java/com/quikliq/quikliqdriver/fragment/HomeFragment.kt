package com.quikliq.quikliqdriver.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.adapters.IncomingOrderAdapter
import com.quikliq.quikliqdriver.adapters.OrdersScreensPagerAdapter
import com.quikliq.quikliqdriver.utilities.CustomViewPager
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList
import javax.security.auth.callback.Callback


class HomeFragment : Fragment() {
    private var toolbar_title : TextView? = null
    private var viewPager: CustomViewPager? = null
    private var adapter: OrdersScreensPagerAdapter? = null
    private var daysArrayList: ArrayList<String>? = null
    private var tabLayout: TabLayout? = null
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var no_dataRL: RelativeLayout? = null
    private var msgTV: TextView? = null
    private var titleTV: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        toolbar_title = view.findViewById(R.id.toolbar_title)

        tabLayout = view.findViewById(R.id.tab_layout)
        no_dataRL = view.findViewById(R.id.no_dataRL)
        msgTV = view.findViewById(R.id.msgTV)
        titleTV = view.findViewById(R.id.titleTV)
        viewPager = view.findViewById(R.id.pager)

        utility = Utility()
        pd = ProgressDialog(context)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)

        checkDriverIsVerified()

        return view
    }

    private fun checkDriverIsVerified() {
        if (utility!!.isConnectingToInternet(context)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.IsDiriverVerified(Prefs.getString("userid","")).enqueue(object :retrofit2.Callback<JsonObject>{

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if(response.isSuccessful)
                    {
                        val responseData = response.body().toString()
                        val jsonObject = JSONObject(responseData).getJSONObject("data").getInt("is_verified")

                        if (jsonObject==0)
                        {
                            addEmptyArrayList()
                            toolbar_title!!.text = "Verification"
                            no_dataRL?.visibility = View.VISIBLE
                            titleTV?.setText("Document verification Pending.")
                            msgTV?.setText("Your document verification is pending. Please update your document to activate your account if not updated yet.")
                            pager?.visibility = View.GONE
                        }else{
                            addDaysArraylistData()
                            toolbar_title!!.text = "Orders"
                            no_dataRL?.visibility = View.GONE
                            pager?.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })

        }
    }

    private fun addEmptyArrayList() {
        daysArrayList = ArrayList()
    }

    private fun addDaysArraylistData() {
        daysArrayList = ArrayList()

        daysArrayList!!.add("Incoming Orders")
        daysArrayList!!.add("Runing Orders")

        for (i in daysArrayList!!.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(daysArrayList!![i]))
        }
        adapter = OrdersScreensPagerAdapter(childFragmentManager, activity!!, daysArrayList!!)
        viewPager!!.adapter = adapter
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewPager!!.setOnPageChangeListener(object : CustomViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tabLayout!!.setScrollPosition(position, 0f, true)
                tabLayout!!.getTabAt(position)!!.select()
            }

        })
        viewPager!!.offscreenPageLimit = 0
    }

}
