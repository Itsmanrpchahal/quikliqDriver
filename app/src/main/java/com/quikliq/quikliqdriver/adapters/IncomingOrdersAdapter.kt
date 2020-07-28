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
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quikliq.app.fragment.IncomingOrdersFragment
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.models.HistoryModel
import com.quikliq.quikliqdriver.utilities.Utility


class IncomingOrdersAdapter(
    private val context: Context,
    private var historyModelArraylist: ArrayList<HistoryModel>
) :
    RecyclerView.Adapter<IncomingOrdersAdapter.ViewHolder>() {
    private var builder: AlertDialog.Builder? = null

    private var utility: Utility? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_incoming_order, parent, false)
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
        holder.providernameTV.text = "Provider Name :"+historymodel.provider_name
        holder.totalpriceTV.text = "Customer Name :" + historymodel.user_name
        holder.allitemsTV.text = "Provider Adress :"+historymodel.provider_address
        holder.ordertimeTV.text = "Customer Adress :"+historymodel.user_address
        holder.ordernumTV.text = "Order Number :"+historymodel.order_id
        holder.reject_BT.setOnClickListener {
            builder = AlertDialog.Builder(context)
            builder!!.setTitle("Order Rejection")
            builder!!.setMessage("Do you want to Reject this Order ?")
            builder!!.setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    IncomingOrdersFragment.orderStatusUpdate!!.status(historymodel.order_id!!, "0")
                    dialog.cancel()
                    holder.parentCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                    holder.parentCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            val alert = builder!!.create()
            alert.show()
            holder.parentCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.accept_BT.setOnClickListener {
             IncomingOrdersFragment.orderStatusUpdate!!.status(historymodel.order_id!!, "1")
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
        val reject_BT: TextView = itemView.findViewById(R.id.reject_BT)
        val accept_BT: TextView = itemView.findViewById(R.id.accept_BT)
        val parentCart: CardView = itemView.findViewById(R.id.parentCart)
    }


}