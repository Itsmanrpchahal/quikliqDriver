package com.quikliq.quikliqdriver.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.models.MyOrderModel
import com.quikliq.quikliqdriver.utilities.Utility


class HistoryAdapter(
    private val context: Context,
    private var historyModelArraylist: ArrayList<MyOrderModel>
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var utility: Utility? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_order_item, parent, false)
        return ViewHolder(v)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        utility = Utility()
        val historymodel = historyModelArraylist[position]
        utility!!.loadImageWithLoader(
            context,
            historymodel.BusinessImage,
            holder.providerIV
        )
        holder.providernameTV.text = historymodel.BusinessName

        when {
            historymodel.status!! == "0" -> {
                holder.statusTV.text = "Pending"
            }
            historymodel.status!! == "1" -> holder.statusTV.text = "Preparing"
            historymodel.status!! == "2" -> holder.statusTV.text = "Ready to Delivery"
            historymodel.status!! == "3" -> holder.statusTV.text = "Delivered"
            historymodel.status!! == "4" -> holder.statusTV.text = "Reject"
            historymodel.status!! == "5" -> holder.statusTV.text = "Canceled by User"
        }
        holder.totalpriceTV.text = "$"+historymodel.totalprice
        holder.allitemsTV.text = historymodel.products!!.joinToString(limit = 20, truncated = "....")
        holder.ordertimeTV.text = historymodel.datetime
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
        val statusTV: TextView = itemView.findViewById(R.id.statusTV) as TextView
        val parentCart: CardView = itemView.findViewById(R.id.parentCart)
        val driverTV : TextView = itemView.findViewById(R.id.driverTV)
    }



}