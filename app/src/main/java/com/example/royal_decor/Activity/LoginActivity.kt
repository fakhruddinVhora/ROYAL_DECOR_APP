package com.example.royal_decor.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
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
    private lateinit var loginprogressbar: ProgressBar
    private lateinit var createnewaccount: MaterialTextView
    private lateinit var reviewus: MaterialTextView
    private var mAuth: FirebaseAuth? = null
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    /* public override fun onStart() {
         super.onStart()

         //If user is signed in.. go to signed in screen
         if(mAuth!!.currentUser != null){
             val dbHelper = DatabaseHelper()
             dbHelper.open()
             dbHelper.fetchDataforPieChart(loginprogressbar)
             if(loginprogressbar.visibility==View.GONE){
                 startActivity(Intent(applicationContext,DashboardActivity::class.java))
                 finish()
             }
         }
     }
 */

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
                } else {
                    //granted
                }
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
        BTNlogin = findViewById(R.id.btn_login)
        loginprogressbar = findViewById(R.id.loginprogressbar)
        createnewaccount = findViewById(R.id.createnewaccount)
        reviewus = findViewById(R.id.reviewus)


        mAuth = FirebaseAuth.getInstance()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                //   loginprogressbar.visibility == View.VISIBLE

                if (loginprogressbar.visibility == View.GONE) {
                    // Login()
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                }
            }

            R.id.createnewaccount -> {
                DialogCreator()
            }

            R.id.reviewus -> {
                startActivity(Intent(applicationContext, CustomerReviewActivity::class.java))
            }
        }

    }

    private fun Login() {

        loginprogressbar.visibility = View.VISIBLE

        mAuth!!.signInWithEmailAndPassword("username", "password")
            .addOnCompleteListener { task ->
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (task.isSuccessful) {
                    /*val dbHelper = DatabaseHelper()
                    dbHelper.open()
                    dbHelper.fetchDataforPieChart(loginprogressbar)*/
                    if (loginprogressbar.visibility == View.GONE) {
                        Login()
                        startActivity(Intent(applicationContext, DashboardActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Authentication Failed", Toast.LENGTH_LONG)
                        .show()

                }
            }
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
