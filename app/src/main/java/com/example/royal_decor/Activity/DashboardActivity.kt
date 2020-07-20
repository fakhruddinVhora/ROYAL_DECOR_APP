package com.example.royal_decor.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.royal_decor.Adapters.DashboardRecyclerViewAdapter
import com.example.royal_decor.Adapters.GraphViewAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Models.DashboardRVObj
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth


class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var dashboardrv: RecyclerView
    private lateinit var adapter: DashboardRecyclerViewAdapter
    private lateinit var HorizontalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var logoutImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dashboardprogressbar: ProgressBar
    private var dbHelper: DatabaseHelper = DatabaseHelper()


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
        //storeDBValuesInConstants()
        setupGraphAdapter()
        setupRecyclerView()



        logoutImg.setOnClickListener(this)
        backImg.setOnClickListener(this)
    }

    private fun storeDBValuesInConstants() {
        dbHelper.open()
        dashboardprogressbar.visibility = View.VISIBLE
        dbHelper.storeDBValuesInConstants(dashboardprogressbar)
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        logoutImg.visibility = View.VISIBLE
        backImg.setImageDrawable(resources.getDrawable(R.drawable.ic_question_answer_black_18dp))
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
            if (it.text.equals(Constants.VIEW_CUSTOMER_LIST)) {
                i.putExtra(stringtag, Constants.VIEW_CUSTOMER_LIST)
            }
            if (it.text.equals(Constants.VIEW_PAINTERS_LIST)) {
                i.putExtra(stringtag, Constants.VIEW_PAINTERS_LIST)
            }
            if (it.text.equals(Constants.EVALUATE_CREDITS)) {
                i.putExtra(stringtag, Constants.EVALUATE_CREDITS)
            }
            if (it.text.equals(Constants.VIEW_CREDIT_SCORE)) {
                i.putExtra(stringtag, Constants.VIEW_CREDIT_SCORE)
            }
            if (it.text.equals(Constants.VIEW_PRODUCT)) {
                i.putExtra(stringtag, Constants.VIEW_PRODUCT)
            }
            if (it.text.equals(Constants.CREDIT_STATEMENT)) {
                i.putExtra(stringtag, Constants.CREDIT_STATEMENT)
            }
            startActivity(i)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);


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
        tempMap.add(DashboardRVObj(Constants.EVALUATE_CREDITS, R.drawable.ic_evaluatecredits))



        tempMap.add(DashboardRVObj(Constants.VIEW_PRODUCT, R.drawable.ic_view_product))
        tempMap.add(DashboardRVObj(Constants.VIEW_CUSTOMER_LIST, R.drawable.view_customer))
        tempMap.add(DashboardRVObj(Constants.VIEW_PAINTERS_LIST, R.drawable.ic_view_list))
        tempMap.add(DashboardRVObj(Constants.VIEW_CREDIT_SCORE, R.drawable.ic_viewcredits))
        tempMap.add(DashboardRVObj(Constants.CREDIT_STATEMENT, R.drawable.ic_credittally))

        return tempMap
    }

    private fun setupGraphAdapter() {

        val PAGES_FOR_SLIDER = 1
        val adapter = GraphViewAdapter(this, supportFragmentManager, PAGES_FOR_SLIDER)
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

        dashboardprogressbar = findViewById(R.id.dashboardprogressbar)
    }

    override fun onBackPressed() {
        DialogCreator()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_logout -> {
                DialogCreator()
            }

            R.id.img_back -> {
                val i = Intent(this, DeskBaseActivity::class.java)
                i.putExtra("ItemSelected", Constants.CUSTOMER_FEEDBACK)
                startActivity(i)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)

            }
        }
    }

    private fun DialogCreator() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Logout..!!")
        dialog.setMessage("Are you sure you want to Logout?")
        val inflater = this.layoutInflater
        dialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish()
        }
        dialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }

}

