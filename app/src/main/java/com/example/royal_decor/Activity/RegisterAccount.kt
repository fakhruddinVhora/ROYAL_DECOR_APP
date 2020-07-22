package com.example.royal_decor.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterAccount : AppCompatActivity(), View.OnClickListener {

    private lateinit var etname: TextInputEditText
    private lateinit var et_emailid: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var et_confirmpassword: TextInputEditText
    private lateinit var pb_registeraccount: ProgressBar
    private var mAuth: FirebaseAuth? = null

    private lateinit var btn_Submit: Button
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)


        init()
        initialization()
        btn_Submit.setOnClickListener(this)
        backImg.setOnClickListener(this)
    }

    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        backImg = findViewById(R.id.img_back)
        headertext = findViewById(R.id.header_text)

        pb_registeraccount = findViewById(R.id.pb_registeraccount)
        etname = findViewById(R.id.name)
        et_emailid = findViewById(R.id.emailid)
        et_password = findViewById(R.id.password)
        et_confirmpassword = findViewById(R.id.confirmpassword)

        btn_Submit = findViewById(R.id.btn_register)
    }

    private fun initialization() {
        headertext.setText("Register New Account")
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.img_back -> {
                finish()
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            R.id.btn_register -> {
                if (validation()) {
                    mAuth!!.createUserWithEmailAndPassword(
                        et_emailid.text.toString(),
                        et_password.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            pb_registeraccount.visibility = View.GONE

                            if (task.isSuccessful) {
                                val user =
                                    FirebaseAuth.getInstance().currentUser
                                val profileUpdates =
                                    UserProfileChangeRequest.Builder()
                                        .setDisplayName(etname.text.toString()).build()
                                user!!.updateProfile(profileUpdates)
                                DialogCreator()

                            } else {
                                Toast.makeText(
                                    this@RegisterAccount,
                                    "Registration failed." + task.getException(),
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e("MyTag", task.getException().toString())
                            }
                        }

                }
            }
        }
    }

    private fun validation(): Boolean {
        var returnbool = true
        if (etname.text!!.isEmpty()) {
            etname.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (etname.text.toString().length > 30) {
                etname.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (et_emailid.text!!.isEmpty()) {
            et_emailid.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_emailid.text.toString().length > 30) {
                et_emailid.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (et_password.text!!.isEmpty()) {
            et_password.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_password.text.toString().length > 20) {
                et_password.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (et_confirmpassword.text!!.isEmpty()) {
            et_confirmpassword.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_confirmpassword.text.toString().length > 20) {
                et_confirmpassword.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (!et_password.text!!.isEmpty() && !et_confirmpassword.text!!.isEmpty()) {
            if (!et_confirmpassword.text.toString().equals(et_password.text.toString())) {
                et_confirmpassword.error = "Password doesnt match"
                returnbool = false
            }
        }
        return returnbool
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private fun DialogCreator() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Success")
        val inflater = this.layoutInflater
        dialog.setMessage("Proceed To Dashboard??")
        dialog.setPositiveButton("Sure") { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(this@RegisterAccount, DashboardActivity::class.java))
            finish()
        }
        dialog.setNegativeButton("Not Now") { dialog, which ->
            dialog.dismiss()
            mAuth!!.signOut()
            finish()
        }
        dialog.show()
    }

}