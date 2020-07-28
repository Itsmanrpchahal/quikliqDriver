package com.quikliq.quikliqdriver.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.activity__otp.*

class OtpActivity : AppCompatActivity(), View.OnClickListener {
    private var utility: Utility? = null
    private var otp: String? = null
    private var phone_number: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__otp)
        otp = intent.getStringExtra("otp")
        phone_number = intent.getStringExtra("mobile")

        utility = Utility()
        utility!!.relative_snackbar(
            parent_otp!!,
            "OTP sent to "+phone_number,
            getString(R.string.close_up)
        )

        nextScreen.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.nextScreen -> checkValidation()
        }
    }

    private fun checkValidation() {
        hideKeyboard()
        when {
            otp_ET.text.isEmpty() -> {
                otp_ET.requestFocus()
                otp_ET.error = getString(R.string.txt_Error_required)
            }
            else -> when (otp_ET.text.toString()) {
                otp -> startActivity(Intent(this@OtpActivity, RegisterActivity::class.java).putExtra("mobile",phone_number).putExtra("otp",otp).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                else ->  utility!!.relative_snackbar(parent_otp!!, "Wrong OTP", getString(R.string.close_up))
            }
        }
    }


    private fun hideKeyboard() {
        val imm = this@OtpActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

}
