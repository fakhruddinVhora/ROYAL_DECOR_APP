package com.example.royal_decor.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Patterns
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
    private lateinit var forgotpassword: MaterialTextView
    private lateinit var createnewaccount: MaterialTextView
    private var isFirstTimeLogin: Boolean = false;
    private var Const: Constants = Constants();

    private lateinit var prefs: SharedPreferences

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
        init()
        if (Const.isNetworkConnected(this)) {
            setupPermissions()
        } else {
            Const.CloseAppDialog(this)
        }


        BTNlogin.setOnClickListener(this)
        forgotpassword.setOnClickListener(this)
        createnewaccount.setOnClickListener(this)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //denies
                    finishAffinity()
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
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }


    private fun init() {
        mAuth = FirebaseAuth.getInstance()


        prefs = getSharedPreference()
        BTNlogin = findViewById(R.id.btn_login)
        loginprogressbar = findViewById(R.id.loginprogressbar)
        forgotpassword = findViewById(R.id.forgetpassword)
        createnewaccount = findViewById(R.id.createnewaccount)
        et_username = findViewById(R.id.username)
        et_password = findViewById(R.id.password)
        forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        setEmailID()

    }

    private fun setEmailID() {
        val emailIDFromPrefs = getEmailIDFromPreferences()
        if (emailIDFromPrefs != "") {
            et_username.setText(emailIDFromPrefs)
            et_password.requestFocus()
        } else {
            isFirstTimeLogin = true
        }
    }

    private fun getEmailIDFromPreferences(): String {
        prefs.getString(Constants.PREF_EMAIL_ID, "")?.let {
            return it
        }
        return ""
    }

    private fun getSharedPreference(): SharedPreferences {
        return this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
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
                    ).addOnCompleteListener { task ->

                        loginprogressbar.visibility = View.GONE

                        if (task.isSuccessful) {
                            setEmailIDForSharedPrefs(et_username.text.toString())
                            startActivity(
                                Intent(
                                    applicationContext,
                                    DashboardActivity::class.java
                                )

                            )
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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


            }

            R.id.createnewaccount -> {
                DialogCreator()
            }

            R.id.forgetpassword -> {
                if (ValidateEmail()) {
                    loginprogressbar.visibility = View.VISIBLE
                    mAuth!!.sendPasswordResetEmail(et_username.text.toString())
                        .addOnCompleteListener { task ->
                            loginprogressbar.visibility = View.GONE
                            if (task.isSuccessful) {
                                ForgetPasswordDialogCreator()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Reset Password Failed.. Please try after sometime",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

        }

    }

    private fun setEmailIDForSharedPrefs(emailidVal: String) {
        var isToBeSavedInPrefs = false;
        if (isFirstTimeLogin) {
            isToBeSavedInPrefs = true
        } else {
            if (!emailidVal.equals(getEmailIDFromPreferences())) {
                isToBeSavedInPrefs = true
            }
        }
        if (isToBeSavedInPrefs) {
            prefs.edit().apply {
                putString(Constants.PREF_EMAIL_ID, emailidVal)
                apply()
            }

        }
    }

    private fun ValidateEmail(): Boolean {
        var returnBool = true;
        if (et_username.text.toString().equals("")) {
            et_username.setError("Please enter a email ID")
            returnBool = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.toRegex().matches(et_username.text.toString())) {
                et_username.setError("Please Enter a Valid Email ID")
                returnBool = false
            }
        }

        return returnBool
    }

    private fun validation(): Boolean {
        var returnBool = true
        if (et_username.text.toString() == "") {
            returnBool = false
            et_username.error = "Please Enter a Username"
        } else {
            if (!Patterns.EMAIL_ADDRESS.toRegex().matches(et_username.text.toString())) {
                et_username.setError("Please Enter a Valid Email ID")
                returnBool = false
            }
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
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(applicationContext, "Wrong Code", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun ForgetPasswordDialogCreator() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Reset Password")
        dialog.setMessage("Sent password link to ${et_username.text.toString()}")
        dialog.setPositiveButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }

}
