package com.quikliq.quikliqdriver.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import com.quikliq.quikliqdriver.R
import com.quikliq.quikliqdriver.imagePicker.PickerBuilder
import com.quikliq.quikliqdriver.utilities.Prefs
import com.quikliq.quikliqdriver.utilities.Utility
import kotlinx.android.synthetic.main.activity_add_documents.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException
import org.json.JSONObject
import retrofit.RequestsCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDocumentsActivity : AppCompatActivity(), View.OnClickListener {
    private var utility: Utility? = null
    private var pd: ProgressDialog? = null
    private var outputUri: Uri? = null
    private var type: String = ""
    private lateinit var picturebanner : Button
    private lateinit var policeVerificationbanner : Button
    private lateinit var driverbanner : Button
    private lateinit var insurancebanner : Button
    private lateinit var permitbanner : Button
    private lateinit var registeraitonbanner : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_documents)
        picturebanner = findViewById(R.id.picturebanner)
        policeVerificationbanner = findViewById(R.id.policeVerificationbanner)
        driverbanner = findViewById(R.id.driverbanner)
        insurancebanner = findViewById(R.id.insurancebanner)
        permitbanner = findViewById(R.id.permitbanner)
        registeraitonbanner = findViewById(R.id.registeraitonbanner)

        policeVerification.setOnClickListener(this)
        picture.setOnClickListener(this)
        driverliscense.setOnClickListener(this)
        vehicleinsurance.setOnClickListener(this)
        vehiclepermit.setOnClickListener(this)
        vehicleregistration.setOnClickListener(this)
        utility = Utility()
        pd = ProgressDialog(this@AddDocumentsActivity)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        documentStatusApiCall()


    }




    fun pictureSelection() {
        if ((ContextCompat.checkSelfPermission(
                this@AddDocumentsActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                this@AddDocumentsActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                this@AddDocumentsActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            methodRequiresPermissions()
        } else {
            pictureSelectionDialog()
        }
    }

    /**
     * used for rquesting the permissions in OS version Marshmallow or higher
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun methodRequiresPermissions() = runWithPermissions(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        options = quickPermissionsOption
    ) {
        pictureSelectionDialog()

    }

    /**methodRequiresPermissions
     * this will be called when permission is denied once or more time. Handle it your way
     */
    private fun rationaleCallback(req: QuickPermissionsRequest) {
        AlertDialog.Builder(this@AddDocumentsActivity)
            .setTitle("Permissions Denied")
            .setMessage("This is the custom rationale dialog. Please allow us to proceed " + "asking for permissions again, or cancel to end the permission flow.")
            .setPositiveButton("Go Ahead") { _, _ -> req.proceed() }
            .setNegativeButton("cancel") { _, _ -> req.cancel() }
            .setCancelable(false)
            .show()
    }

    /**
     * this will be called when some/all permissions required by the method are permanently
    denied. Handle it your way.
     */
    private fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        AlertDialog.Builder(this@AddDocumentsActivity)
            .setTitle("Permissions Denied")
            .setMessage(
                "This is the custom permissions permanently denied dialog. " +
                        "Please open app settings to open app settings for allowing permissions, " +
                        "or cancel to end the permission flow."
            )
            .setPositiveButton("App Settings") { _, _ -> req.openAppSettings() }
            .setNegativeButton("Cancel") { _, _ -> req.cancel() }
            .setCancelable(false)
            .show()
    }

    /**
     * used for permission callbacks
     */
    private val quickPermissionsOption = QuickPermissionsOptions(
        rationaleMessage = "Custom rational message",
        permanentlyDeniedMessage = "Custom permanently denied message",
        rationaleMethod = { rationaleCallback(it) },
        permanentDeniedMethod = { permissionsPermanentlyDenied(it) }
    )

    private fun pictureSelectionDialog() {
        val cameraIV: ImageView
        val galleryIV: ImageView
        val dialog = Dialog(this@AddDocumentsActivity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.profile_picture_dailog)
        cameraIV = dialog.findViewById<View>(R.id.cameraIV) as ImageView
        galleryIV = dialog.findViewById<View>(R.id.galleryIV) as ImageView
        galleryIV.setOnClickListener {
            PickerBuilder(this@AddDocumentsActivity!!, PickerBuilder.SELECT_FROM_GALLERY)
                .setOnImageReceivedListener { imageUri ->
                    outputUri = imageUri
                    uploadIdProofApiCall()
                }
                .setCropScreenColor(Color.CYAN)
                .setOnPermissionRefusedListener { }
                .start()


            dialog.dismiss()
        }
        cameraIV.setOnClickListener {
            PickerBuilder(this@AddDocumentsActivity!!, PickerBuilder.SELECT_FROM_CAMERA)
                .setOnImageReceivedListener { imageUri ->
                    outputUri = imageUri
                    uploadIdProofApiCall()
                }
                .setCropScreenColor(Color.CYAN)
                .setOnPermissionRefusedListener { }
                .start()

            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        var pic = "pictutre"
        var police_Verification = "police_verification"
        var driver_liscense = "driver_license"
        var vehicle_insurance = "vehicle_insurance"
        var vehicle_permit = "vehicle_permit"
        var vehicle_registration = "vehicle_registration"
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.picture -> {
                type = pic
                pictureSelection()
            }
            R.id.policeVerification -> {
                type = police_Verification
                pictureSelection()
            }
            R.id.driverliscense -> {
                type = driver_liscense
                pictureSelection()
            }
            R.id.vehicleinsurance -> {
                type = vehicle_insurance
                pictureSelection()
            }
            R.id.vehiclepermit -> {
                type = vehicle_permit
                pictureSelection()
            }
            R.id.vehicleregistration -> {
                type = vehicle_registration
                pictureSelection()
            }
        }
    }


    private fun uploadIdProofApiCall() {
        if (utility!!.isConnectingToInternet(this@AddDocumentsActivity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.UploadIdProof(
                this@AddDocumentsActivity,
                Prefs.getString("userid", ""),
                type,
                outputUri
            ).enqueue(object : Callback<JsonObject> {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful) {
                        Log.d("response", response.body().toString())
                        val responsedata = response.body().toString()
                        try {
                            val jsonObject = JSONObject(responsedata)
                            if (jsonObject.optBoolean("status")) {
                                utility!!.linear_snackbar(
                                   findViewById(android.R.id.content),
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                                /*when {
                                    type == pic -> pictureIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    type == police_Verification -> policeVerificationIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    type == driver_liscense -> driverliscenseIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    type == vehicle_insurance -> vehicleinsuranceIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    type == vehicle_permit -> vehiclepermitIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    type == vehicle_registration -> vehicleregistrationIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                }*/

                            } else {
                                utility!!.linear_snackbar(
                                   findViewById(android.R.id.content),
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {
                        utility!!.linear_snackbar(
                           findViewById(android.R.id.content),
                            response.message(),
                            getString(R.string.close_up)
                        )
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                       findViewById(android.R.id.content),
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        } else {
            utility!!.linear_snackbar(
               findViewById(android.R.id.content),
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }


    private fun documentStatusApiCall() {
        if (utility!!.isConnectingToInternet(this@AddDocumentsActivity)) {
            pd!!.show()
            pd!!.setContentView(R.layout.loading)
            val requestsCall = RequestsCall()
            requestsCall.documentStatus(Prefs.getString("userid", "")).enqueue(object : Callback<JsonObject> {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pd!!.dismiss()
                    if (response.isSuccessful) {
                        Log.d("response", response.body().toString())
                        val responsedata = response.body().toString()
                        try {
                            val jsonObject = JSONObject(responsedata)
                            if (jsonObject.optBoolean("status")) {
                                val jsonObject1 = jsonObject.optJSONArray("data").optJSONObject(0)

                                if (jsonObject1.get("profile_picture").equals(null) && jsonObject1.get("picture_status")==0)
                                {
                                    picturebanner.visibility = View.GONE
                                }


                               /* if(jsonObject1.optInt("picture_status") == 1 ){
                                    pictureIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    picturebanner.visibility = View.VISIBLE
                                    picture.isEnabled = false
                                }else if(jsonObject1.optInt("picture_status")==2 && jsonObject1.get("profile_picture").equals(null))
                                {
                                    pictureIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    picturebanner.visibility = View.GONE
                                    pictureIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if ( !jsonObject1.get("profile_picture").equals(""))
                                {
                                    picturebanner.visibility = View.VISIBLE
                                }*/


                                if(jsonObject1.optInt("police_verification_status") == 1){
                                    policeVerificationbanner.visibility = View.VISIBLE
                                    policeVerificationIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    policeVerification.isEnabled = false
                                }else if(jsonObject1.optInt("police_verification_status")==2 && jsonObject1.get("police_verification").equals(null))
                                {
                                    policeVerificationbanner.visibility = View.GONE
                                    policeVerificationIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    policeVerificationIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if (!jsonObject1.getString("police_verification").equals(""))
                                {
                                    policeVerificationbanner.visibility = View.VISIBLE
                                }


                                if(jsonObject1.optInt("driver_license_status") == 1){
                                    driverbanner.visibility = View.VISIBLE
                                    driverliscenseIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    driverliscense.isEnabled = false
                                }else if(jsonObject1.optInt("driver_license_status")==2 && jsonObject1.get("driver_license").equals(null))
                                {
                                    driverbanner.visibility = View.GONE
                                    driverliscenseIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    driverliscenseIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if(!jsonObject1.getString("driver_license").equals(""))
                                {
                                    driverbanner.visibility = View.VISIBLE
                                }


                                if(jsonObject1.optInt("vehicle_insurance_status") == 1){
                                    insurancebanner.visibility = View.VISIBLE
                                    vehicleinsuranceIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    vehicleinsurance.isEnabled = false
                                }else if(jsonObject1.optInt("vehicle_insurance_status")==2 && jsonObject1.get("vehicle_insurance").equals(null))
                                {
                                    insurancebanner.visibility = View.GONE
                                    vehicleinsuranceIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    vehicleinsuranceIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if (!jsonObject1.get("vehicle_insurance").equals(""))
                                {
                                    insurancebanner.visibility = View.VISIBLE
                                }


                                if(jsonObject1.optInt("vehicle_permit_status") == 1){
                                    permitbanner.visibility = View.VISIBLE
                                    vehiclepermitIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    vehiclepermit.isEnabled = false
                                }else if(jsonObject1.optInt("vehicle_permit_status")==2 && jsonObject1.get("vehicle_permit").equals(null))
                                {
                                    vehicleinsuranceIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    permitbanner.visibility = View.GONE
                                    vehicleinsuranceIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if (!jsonObject1.get("vehicle_permit").equals(""))
                                {
                                    permitbanner.visibility = View.VISIBLE
                                }


                                if(jsonObject1.optInt("vehicle_registration_status") == 1){
                                    registeraitonbanner.visibility = View.VISIBLE
                                    vehicleregistrationIV.setBackgroundResource(R.drawable.ic_done_black_24dp)
                                    vehicleregistration.isEnabled = false
                                }else if(jsonObject1.optInt("vehicle_registration_status")==2 && jsonObject1.get("vehicle_registration").equals(null))
                                {
                                    registeraitonbanner.visibility = View.GONE
                                    vehicleregistrationIV.setBackgroundResource(R.drawable.ic_error_outline_black_24dp)
                                    vehicleregistrationIV.setOnClickListener {
                                        utility!!.linear_snackbar(
                                            findViewById(android.R.id.content),
                                            getString(R.string.Rejectbyadmin),
                                            getString(R.string.close_up)
                                        )
                                    }
                                }else if(jsonObject1.getString("vehicle_registration").equals(""))
                                {
                                    registeraitonbanner.visibility = View.VISIBLE
                                }

                            } else {
                                utility!!.linear_snackbar(
                                    findViewById(android.R.id.content),
                                    jsonObject.optString("message"),
                                    getString(R.string.close_up)
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {
                        utility!!.linear_snackbar(
                            findViewById(android.R.id.content),
                            response.message(),
                            getString(R.string.close_up)
                        )
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    pd!!.dismiss()
                    utility!!.linear_snackbar(
                        findViewById(android.R.id.content),
                        getString(R.string.no_internet_connectivity),
                        getString(R.string.close_up)
                    )
                }
            })
        } else {
            utility!!.linear_snackbar(
                findViewById(android.R.id.content),
                getString(R.string.no_internet_connectivity),
                getString(R.string.close_up)
            )
        }
    }

}
