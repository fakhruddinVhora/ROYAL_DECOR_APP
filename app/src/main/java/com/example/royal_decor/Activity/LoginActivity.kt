package com.example.royal_decor.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var BTNlogin: Button

    private lateinit var et_username: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var loginprogressbar: ProgressBar
    private lateinit var createnewaccount: MaterialTextView
    private lateinit var reviewus: MaterialTextView


    private var mAuth: FirebaseAuth? = null
    private val RECORD_REQUEST_CODE = 101


    public override fun onStart() {
        super.onStart()

        //If user is signed in.. go to signed in screen
        if (mAuth!!.currentUser != null) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupPermissions()


        init()


        createnewaccount.setPaintFlags(createnewaccount.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


        BTNlogin.setOnClickListener(this)
        createnewaccount.setOnClickListener(this)
        reviewus.setOnClickListener(this)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //denies
                    finishAffinity()
                } else {
                    //granted
                }
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE/*,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION*/
            ),
            RECORD_REQUEST_CODE
        )
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
/*        val permission1 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val permission2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)*/
        if (permission != PackageManager.PERMISSION_GRANTED /*|| permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED*/) {
            makeRequest()
        }
    }


    private fun init() {
        mAuth = FirebaseAuth.getInstance()

        BTNlogin = findViewById(R.id.btn_login)
        loginprogressbar = findViewById(R.id.loginprogressbar)
        createnewaccount = findViewById(R.id.createnewaccount)
        reviewus = findViewById(R.id.reviewus)

        et_username = findViewById(R.id.username)
        et_password = findViewById(R.id.password)


        mAuth = FirebaseAuth.getInstance()

    }


    @SuppressLint("MissingPermission")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                if (validation()) {
                    loginprogressbar.visibility = View.VISIBLE

                    mAuth!!.signInWithEmailAndPassword(
                        et_username.text.toString(),
                        et_password.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            loginprogressbar.visibility = View.GONE

                            if (task.isSuccessful) {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        DashboardActivity::class.java
                                    )

                                )
                                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Authentication Failed.. Please Check Username/Password",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }

                //   loginprogressbar.visibility == View.VISIBLE

                /*   if (loginprogressbar.visibility == View.GONE) {
                       // Login()
                   }*/
                /* val lm =
                     getSystemService(Context.LOCATION_SERVICE) as LocationManager
                 val location: Location = getLastKnownLocation()
                 val longitude: Double = location.getLongitude()
                 val latitude: Double = location.getLatitude()

                 Toast.makeText(
                     applicationContext,
                     "Latitude $latitude and Longitude : $longitude",
                     Toast.LENGTH_LONG
                 ).show()*/
            }

            R.id.createnewaccount -> {
                DialogCreator()
            }

            R.id.reviewus -> {
                startActivity(Intent(applicationContext, CustomerReviewActivity::class.java))
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            }
        }

    }

    private fun validation(): Boolean {
        var returnBool = true
        if (et_username.text.toString() == "") {
            returnBool = false
            et_username.error = "Please Enter a Username"
        }
        if (et_password.text.toString() == "") {
            returnBool = false
            et_password.error = "Please Enter Password"
        }
        return returnBool
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location {
        val mLocationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = mLocationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation!!
    }


    fun DialogCreator() {

        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Enter secret Code")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.secretcode_dialog_layout, null)

        val secretcode = dialogView.findViewById<TextInputEditText>(R.id.secretcode)




        dialog.setView(dialogView)
        dialog.setPositiveButton("Create") { dialog, which ->
            if (secretcode.text!!.toString().equals(Constants.SECRET_CODE)) {
                startActivity(Intent(applicationContext, RegisterAccount::class.java))
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            } else {
                Toast.makeText(applicationContext, "Wrong Code", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }
}
