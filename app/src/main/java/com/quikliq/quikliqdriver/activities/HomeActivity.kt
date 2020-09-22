package com.quikliq.quikliqdriver.activities


import android.R.color
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.fragment.HomeFragment
import com.quikliq.quikliqdriver.fragment.ProfileFragment


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
   private var navigation: BottomNavigationView? = null
    private lateinit var fm: FragmentManager
    private var nointernet: RelativeLayout? = null
    private var screendata: RelativeLayout? = null
    var notC = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__home)
        navigation = findViewById(R.id.navigation)
        nointernet = findViewById(R.id.nointernet)
        screendata = findViewById(R.id.screendata)
        navigation!!.setOnNavigationItemSelectedListener(this)
        fm = supportFragmentManager

        var red = Integer.toHexString(Color.red(R.color.colorPrimary))
        var green = Integer.toHexString(Color.green(R.color.colorPrimary))
        var blue = Integer.toHexString(Color.blue(R.color.colorPrimary))
        if (red.length == 1) red = "0$red"
        if (green.length == 1) green = "0$green"
        if (blue.length == 1) blue = "0$blue"
        Log.d("color",red+" "+green+"  "+blue)
        val ft = fm.beginTransaction()
        ft.add(R.id.homeFrag, HomeFragment()).commit()
    }


    companion object {
        var tab_position = 0
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


    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        runOnUiThread {
            when (item.itemId) {
                R.id.orders -> {
                    fm = supportFragmentManager
                    fm.popBackStackImmediate()
                    val drink = fm.beginTransaction()
                    drink.replace(R.id.homeFrag, HomeFragment()).commit()
                }
                R.id.profile -> {
                    fm = supportFragmentManager
                    fm.popBackStackImmediate()
                    val drink = fm.beginTransaction()
                    drink.replace(R.id.homeFrag, ProfileFragment()).commit()
                }
            }
        }
        return true
    }
}
