package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CustomerListAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.CustomerCallback
import com.example.royal_decor.Models.Customers
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView

class CustomerListFragment : Fragment(), View.OnClickListener,
    CustomerListAdapter.customerOnclickListener {

    private lateinit var v: View
    private lateinit var custlistrv: RecyclerView
    private lateinit var custadapter: CustomerListAdapter
    private lateinit var VerticalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var pb_customer: ProgressBar

    private lateinit var dbhandler: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_customer_list, container, false)
        init()
        initialization()

        dbhandler.getcustomerdetails(pb_customer, object : CustomerCallback {
            override fun returnCustomerValues(list: ArrayList<Customers>) {
                settingAdapter(list)
            }

        })



        backImg.setOnClickListener(this)
        return v


    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CUSTOMER_LIST
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        custlistrv = v.findViewById(R.id.customerlistrv)
        pb_customer = v.findViewById(R.id.pb_customerdetails)

        dbhandler = DatabaseHelper()
        dbhandler.open()

    }

    private fun settingAdapter(custListData: ArrayList<Customers>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        custlistrv.layoutManager = RecyclerViewLayoutManager
        custadapter = CustomerListAdapter(custListData, this)
        custlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        custlistrv.adapter = custadapter
        custlistrv.scheduleLayoutAnimation()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }

    override fun OnDeleteClick(item: Customers) {
        dbhandler.deletecustomer(item, pb_customer, object : CustomerCallback {
            override fun returnCustomerValues(list: ArrayList<Customers>) {
                custadapter.updateData(list)
                custlistrv.scheduleLayoutAnimation()
            }
        })
    }

    override fun OnInfoClick(item: Customers) {
        DialogCreator(item)
    }

    fun DialogCreator(item: Customers) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("${item.name}'s Feedback")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.custom_customer_dialog_layout, null)

        val expectations = dialogView.findViewById<MaterialTextView>(R.id.expectations)
        val productshade = dialogView.findViewById<MaterialTextView>(R.id.productshade)
        val employees = dialogView.findViewById<MaterialTextView>(R.id.employeeservices)
        val overallstars = dialogView.findViewById<MaterialTextView>(R.id.overallrating)
        val colorconsulatation = dialogView.findViewById<MaterialTextView>(R.id.colorselection)
        val suggestions = dialogView.findViewById<MaterialTextView>(R.id.suggestions)


        expectations.setText(expectations.text.toString().replace("XXXE", item.expectations))
        productshade.setText(
            productshade.text.toString().replace("XXXPS", item.productshadequality)
        )
        employees.setText(employees.text.toString().replace("XXXES", item.employeeservicerating))
        overallstars.setText(overallstars.text.toString().replace("XXXS", item.overallratingstars))
        colorconsulatation.setText(
            colorconsulatation.text.toString().replace("XXXCSC", item.colorselectionconsultation)
        )
        if (item.othersuggestions != "") {
            suggestions.setText(item.othersuggestions)

        }



        dialog.setView(dialogView)
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
    }


}
