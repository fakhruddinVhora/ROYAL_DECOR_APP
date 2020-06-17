package com.example.royal_decor.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.example.royal_decor.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var BTNlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        BTNlogin.setOnClickListener(this)
    }

    private fun init() {
        BTNlogin = findViewById(R.id.btn_login) as Button
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                startActivity(Intent(applicationContext, DashboardActivity::class.java))
            }
        }

    }
}
