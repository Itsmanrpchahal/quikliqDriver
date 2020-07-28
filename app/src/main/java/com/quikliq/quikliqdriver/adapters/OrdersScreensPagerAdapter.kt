package com.quikliq.quikliqdriver.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.quikliq.app.fragment.RuningOrdersFragment
import com.quikliq.app.fragment.IncomingOrdersFragment
import java.util.*


/**
 * Created by Navdeep Machine
 */
class OrdersScreensPagerAdapter(fm: FragmentManager, internal var context: Context, private val machinedataArraylist: ArrayList<String>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        System.gc()
        return if(position == 0){
            IncomingOrdersFragment()
        }else{
            RuningOrdersFragment()
        }
    }

    override fun getCount(): Int {
        return machinedataArraylist.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return machinedataArraylist[position]
    }
}
