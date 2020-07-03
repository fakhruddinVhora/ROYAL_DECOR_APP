package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CreditStatementAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.CredStmtCallback
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class CreditStatementFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var creditstmtlistrv: RecyclerView
    private lateinit var creditstmtadapter: CreditStatementAdapter
    private lateinit var VerticalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var pb_creditstmt: ProgressBar
    private lateinit var dbhandler: DatabaseHelper

    private lateinit var btn_startdate: Button
    private lateinit var btn_enddate: Button
    private lateinit var et_startdate: TextInputEditText
    private lateinit var et_enddate: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_credit_statement, container, false)
        init()
        initialization()

        dbhandler.getCredStmt(pb_creditstmt, object : CredStmtCallback {
            override fun returnCredStmtrValues(list: ArrayList<TallyLog>) {
                settingAdapter(list)
            }
        })

        backImg.setOnClickListener(this)
        btn_startdate.setOnClickListener(this)
        btn_enddate.setOnClickListener(this)


        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CUSTOMER_LIST
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        creditstmtlistrv = v.findViewById(R.id.credstmtlistrv)
        pb_creditstmt = v.findViewById(R.id.pb_credstmt)

        btn_enddate = v.findViewById(R.id.btn_enddate)
        btn_startdate = v.findViewById(R.id.btn_startdate)
        et_enddate = v.findViewById(R.id.et_enddate)
        et_startdate = v.findViewById(R.id.et_startdate)

        dbhandler = DatabaseHelper()
        dbhandler.open()

    }

    private fun settingAdapter(custListData: ArrayList<TallyLog>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        creditstmtlistrv.layoutManager = RecyclerViewLayoutManager
        creditstmtadapter = CreditStatementAdapter(custListData, this)
        creditstmtlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        creditstmtlistrv.adapter = creditstmtadapter
        creditstmtlistrv.scheduleLayoutAnimation()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_startdate -> {
                datePickerDialog(true)
            }
            R.id.btn_enddate -> {
                datePickerDialog(false)
            }
        }
    }


    fun OnInfoClick(item: TallyLog) {
        // DialogCreator(item)
    }

/*
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
*/

    private fun datePickerDialog(isStartDate: Boolean) {


        //setconstraints to restrict dates
        val constraints = CalendarConstraints.Builder()  // 1
        val calendar = Calendar.getInstance()
        constraints.setStart(calendar.timeInMillis)   //   2
        //calendar.roll(Calendar.YEAR, -60)   //   3
        constraints.setEnd(calendar.timeInMillis)   // 4

        val builder =
            MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select date")
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        builder.setSelection(currentTimeInMillis)
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            val timeZoneUTC: TimeZone = TimeZone.getDefault()
            val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().time) * -1

            val currentdate = Date(Calendar.getInstance().timeInMillis)
            val selecteddate = Date(it + offsetFromUTC)
            if (selecteddate.after(currentdate)) {
                if (isStartDate) {
                    et_startdate.error = "No future dates allowed"
                    et_startdate.text!!.clear()
                    picker.dismiss()
                } else {
                    et_enddate.error = "No future dates allowed"
                    picker.dismiss()
                    et_enddate.text!!.clear()
                }
            } else {
                val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                if (isStartDate) {
                    et_startdate.setText(simpleFormat.format(selecteddate))
                    et_startdate.setError(null)
                } else {
                    et_enddate.setText(simpleFormat.format(selecteddate))
                }
            }

        }
        picker.show(fragmentManager!!, picker.toString())
    }

}