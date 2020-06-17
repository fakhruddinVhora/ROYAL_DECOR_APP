package com.example.royal_decor.Activity

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.royal_decor.Adapters.DashboardRecyclerViewAdapter
import com.example.royal_decor.Adapters.GraphViewAdapter
import com.example.royal_decor.Models.DashboardRVObj
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.tabs.TabLayout


class DashboardActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var dashboardrv: RecyclerView
    private lateinit var adapter: DashboardRecyclerViewAdapter
    private lateinit var HorizontalLayout: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        init()
        setupGraphAdapter()
        setupRecyclerView()


    }

    private fun setupRecyclerView() {
        val RecyclerViewLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext)
        dashboardrv.layoutManager = RecyclerViewLayoutManager
        adapter = DashboardRecyclerViewAdapter(fetchMapValues()) {
            Toast.makeText(applicationContext, it.text, Toast.LENGTH_SHORT).show()
        }
        HorizontalLayout =
            LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
        dashboardrv.layoutManager = HorizontalLayout
        dashboardrv.adapter = adapter
    }

    private fun fetchMapValues(): ArrayList<DashboardRVObj> {
        val tempMap = ArrayList<DashboardRVObj>()
        tempMap.add(DashboardRVObj(Constants.ADD_PAINTER, R.drawable.ic_addpainter))
        tempMap.add(DashboardRVObj(Constants.ADD_PRODUCT, R.drawable.ic_addproduct))
        tempMap.add(DashboardRVObj(Constants.UPDATE_DATA, R.drawable.ic_updatecredits))
        tempMap.add(DashboardRVObj(Constants.EVALUATE_CREDITS, R.drawable.ic_evaluatecredits))
        tempMap.add(DashboardRVObj(Constants.VIEW_CUSTOMER_LIST, R.drawable.ic_view_list))
        tempMap.add(DashboardRVObj(Constants.VIEW_PAINTERS_LIST, R.drawable.ic_view_list))
        tempMap.add(DashboardRVObj(Constants.VIEW_CREDIT_SCORE, R.drawable.ic_viewcredits))




        return tempMap
    }

    private fun setupGraphAdapter() {
        val adapter = GraphViewAdapter(this, supportFragmentManager, 2)
        tablayout.setupWithViewPager(viewPager)
        viewPager.adapter = adapter
    }


    private fun init() {
        tablayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.graph_viewpager)
        dashboardrv = findViewById(R.id.dashboardrv)
    }


    fun getFunctionalityList(): List<Pair<String, Int>> {
        return listOf(
            Pair(Constants.ADD_PAINTER, Color.parseColor("#CD5C5C")),
            Pair(Constants.ADD_PRODUCT, Color.parseColor("#F08080")),
            Pair(Constants.EVALUATE_CREDITS, Color.parseColor("#FA8072")),
            Pair(Constants.UPDATE_DATA, Color.parseColor("#E9967A")),
            Pair(Constants.VIEW_CREDIT_SCORE, Color.parseColor("#FA8072")),
            Pair(Constants.VIEW_CUSTOMER_LIST, Color.parseColor("#FA8082")),
            Pair(Constants.VIEW_PAINTERS_LIST, Color.parseColor("#E9967A"))
        )
    }
}
