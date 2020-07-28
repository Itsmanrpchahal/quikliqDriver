package com.quikliq.quikliqdriver.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quikliq.app.fragment.RuningOrdersFragment
import com.quikliq.app.fragment.RuningOrdersFragment.Companion.runingStatusUpdate
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.interfaces.RuningStatusUpdate
import com.quikliq.quikliqdriver.models.HistoryModel
import com.quikliq.quikliqdriver.utilities.Utility


class RuningOrderAdapter(
    private val context: Context,
    private var historyModelArraylist: ArrayList<HistoryModel>
) :
    RecyclerView.Adapter<RuningOrderAdapter.ViewHolder>() {

    private var utility: Utility? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_runing_order, parent, false)
        return ViewHolder(v)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        utility = Utility()
        val historymodel = historyModelArraylist[position]
        utility!!.loadImageWithLoader(
            context,
            historymodel.provider_image,
            holder.providerIV
        )
        holder.providernameTV.text = "Provider Name :" + historymodel.provider_name
        holder.totalpriceTV.text = "Customer Name :" + historymodel.user_name
        holder.allitemsTV.text = "Provider Adress :" + historymodel.provider_address
        holder.ordertimeTV.text = "Customer Adress :" + historymodel.user_address
        holder.ordernumTV.text = "Order Number :" + historymodel.order_id
        holder.customer_location.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q="+historymodel.user_latitude+","+historymodel.user_longitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }
        holder.provider_Location.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q="+historymodel.provider_latitude+","+historymodel.provider_longitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }
        holder.completed_BT.setOnClickListener {
            runingStatusUpdate!!.status(historymodel.order_id!!, "")
        }
    }


    override fun getItemCount(): Int {
        return historyModelArraylist.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allitemsTV: TextView = itemView.findViewById(R.id.allitemsTV) as TextView
        val totalpriceTV: TextView = itemView.findViewById(R.id.totalpriceTV) as TextView
        val ordertimeTV: TextView = itemView.findViewById(R.id.ordertimeTV) as TextView
        val providerIV: ImageView = itemView.findViewById(R.id.providerIV) as ImageView
        val providernameTV: TextView = itemView.findViewById(R.id.providernameTV) as TextView
        val ordernumTV: TextView = itemView.findViewById(R.id.ordernumTV)
        val completed_BT: TextView = itemView.findViewById(R.id.completed_BT)
        val provider_Location : TextView = itemView.findViewById(R.id.provider_Location)
        val customer_location : TextView = itemView.findViewById(R.id.customer_location)
        val call: ImageButton = itemView.findViewById(R.id.call)
    }


}