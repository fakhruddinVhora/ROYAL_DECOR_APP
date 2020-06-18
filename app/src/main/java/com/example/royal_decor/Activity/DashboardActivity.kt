package com.example.royal_decor.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
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


class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var dashboardrv: RecyclerView
    private lateinit var adapter: DashboardRecyclerViewAdapter
    private lateinit var HorizontalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var logoutImg: ImageView
    private lateinit var headertext: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        init()
        initialization()
        setupGraphAdapter()
        setupRecyclerView()


    }

    private fun initialization() {
        backImg.visibility = View.GONE
        logoutImg.visibility = View.VISIBLE
        headertext.text = "DASHBOARD"
    }

    private fun setupRecyclerView() {
        val RecyclerViewLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext)
        dashboardrv.layoutManager = RecyclerViewLayoutManager
        adapter = DashboardRecyclerViewAdapter(fetchMapValues()) {
            val i = Intent(this, DeskBaseActivity::class.java)
            val stringtag = "ItemSelected"
            if (it.text.equals(Constants.ADD_PAINTER)) {
                i.putExtra(stringtag, Constants.ADD_PAINTER)
            }
            if (it.text.equals(Constants.ADD_PRODUCT)) {
                i.putExtra(stringtag, Constants.ADD_PRODUCT)
            }
            startActivity(i)
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
        tempMap.add(DashboardRVObj(Constants.VIEW_CUSTOMER_LIST, R.drawable.view_customer))
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

        backImg = findViewById(R.id.img_back)
        logoutImg = findViewById(R.id.img_logout)
        headertext = findViewById(R.id.header_text)
    }

    override fun onClick(v: View?) {

    }
}