package com.example.royal_decor.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.royal_decor.R
import com.github.mikephil.charting.data.PieEntry

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var BTNlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        BTNlogin.setOnClickListener(this)
    }

    private fun init() {
        BTNlogin = findViewById(R.id.btn_login) as Button
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun newsetuppiechart(map: ArrayList<HashMap<String, Int>>) {
        val NoOfEmp = ArrayList<PieEntry>()


        val result: Map<String, Int> = map
            .fold(mapOf()) { accMap, map ->
                accMap.merge(map, Int::plus)
            }

        println(result)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun <T, V> Map<T, V>.merge(another: Map<T, V>, mergeFunction: (V, V) -> V): Map<T, V> =
        toMutableMap()
            .apply {
                another.forEach { (key, value) ->
                    merge(key, value, mergeFunction)
                }
            }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_login -> {
                /* val dbHelper = DatabaseHelper()
                 dbHelper.open()
                 dbHelper.fetchDataforPieChart()*/
                startActivity(Intent(applicationContext, DashboardActivity::class.java))
            }
        }

    }
}
