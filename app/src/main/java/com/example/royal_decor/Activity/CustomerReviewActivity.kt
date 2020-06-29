package com.example.royal_decor.Activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Scroller
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.R
import com.google.android.material.textfield.TextInputEditText

private lateinit var backImg: ImageView
private lateinit var headertext: TextView
private lateinit var cdob: TextInputEditText

private lateinit var csuggestions: TextInputEditText


private lateinit var dbhandler: DatabaseHelper


class CustomerReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_review)


        init()
        initialization()
    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = "Customer FeedBack"
        cdob.isEnabled = false
        cdob.resources.getColor(R.color.black)
        dbhandler = DatabaseHelper()
        dbhandler.open()

        csuggestions.setScroller(Scroller(getApplicationContext()));
        csuggestions.setVerticalScrollBarEnabled(true);
    }

    private fun init() {
        backImg = findViewById(R.id.img_back)
        headertext = findViewById(R.id.header_text)

        cdob = findViewById(R.id.et_dateofbirth)
        csuggestions = findViewById(R.id.suggestions)


    }
}