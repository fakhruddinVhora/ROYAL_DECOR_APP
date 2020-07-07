package com.example.royal_decor.Fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CustomerATVAdapter
import com.example.royal_decor.Adapters.CustomerListAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.CustomerCallback
import com.example.royal_decor.Models.Customers
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomerListFragment : Fragment(), View.OnClickListener,
    CustomerListAdapter.customerOnclickListener {

    private lateinit var v: View
    private lateinit var custlistrv: RecyclerView
    private lateinit var custadapter: CustomerListAdapter
    private lateinit var VerticalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var pb_customer: ProgressBar


    private lateinit var FilterImg: ImageView
    private lateinit var clearFilters: TextView
    private lateinit var FilterLayout: LinearLayout

    private lateinit var btn_startdate: ImageButton
    private lateinit var btn_enddate: ImageButton
    private lateinit var et_startdate: TextInputEditText
    private lateinit var et_enddate: TextInputEditText

    private var CustomerList: ArrayList<Customers> = ArrayList()

    private lateinit var atvCustomer: AutoCompleteTextView

    private var CustomerObj: Customers? = null
    private var sStartDate: String = ""
    private var sEndDate: String = ""


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
                CustomerList.clear()
                sortArrayBasedOnDate(list)
                CustomerList = list
                settingAdapter(list)
                settingCustomerATVAdapter(list)
            }

        })


        clearFilters.setOnClickListener {
            custadapter.updateData(CustomerList)
            custlistrv.scheduleLayoutAnimation()

            et_enddate.setText("")
            et_startdate.setText("")
            atvCustomer.setText("")
            sEndDate = ""
            sStartDate = ""
            CustomerObj == null
        }

        FilterImg.setOnClickListener {
            if (FilterLayout.visibility == View.GONE) {
                FilterLayout.visibility = View.VISIBLE
            } else {
                FilterLayout.visibility = View.GONE
            }
        }

        backImg.setOnClickListener(this)
        btn_startdate.setOnClickListener(this)
        btn_enddate.setOnClickListener(this)
        return v


    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CUSTOMER_LIST


        FilterImg.visibility = View.VISIBLE
        et_startdate.isEnabled = false
        et_enddate.isEnabled = false
        et_enddate.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        et_startdate.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        clearFilters.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))


        FilterImg.setImageDrawable(resources.getDrawable(R.drawable.ic_filter))
        val underlinedString = SpannableString(clearFilters.text.toString())
        underlinedString.setSpan(UnderlineSpan(), 0, underlinedString.length, 0)
        clearFilters.setText(underlinedString)
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        custlistrv = v.findViewById(R.id.customerlistrv)
        pb_customer = v.findViewById(R.id.pb_customerdetails)

        FilterImg = v.findViewById(R.id.img_logout)

        FilterLayout = v.findViewById(R.id.filterlayout)
        atvCustomer = v.findViewById(R.id.atv_customer)
        clearFilters = v.findViewById(R.id.clearfilters)

        btn_enddate = v.findViewById(R.id.btn_enddate)
        btn_startdate = v.findViewById(R.id.btn_startdate)
        et_enddate = v.findViewById(R.id.et_enddate)
        et_startdate = v.findViewById(R.id.et_startdate)
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
                activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }
    }

    private fun settingCustomerATVAdapter(list: ArrayList<Customers>) {
        val adapter = CustomerATVAdapter(v.context, R.layout.list_item, list)
        atvCustomer.setAdapter(adapter)
        atvCustomer.threshold = 1

        atvCustomer.setOnItemClickListener { parent, _, position, _ ->
            val selectedCustomer = parent.adapter.getItem(position) as Customers
            atvCustomer.setText(selectedCustomer.name)
            atvCustomer.setSelection(atvCustomer.text.length)
            CustomerObj = Customers()
            CustomerObj = selectedCustomer
            FilterResults()
        }
    }

    private fun FilterResults() {
        var checkBool = true
        var DateStart = Date()
        var DateEnd = Date()
        var TempcustomerList: ArrayList<Customers> = ArrayList()


        if (CustomerObj == null) {
            checkBool = false
        }
        if (sStartDate == "") {
            checkBool = false
        }
        if (sEndDate == "") {
            checkBool = false
        }
        if (sStartDate != "" && sEndDate != "") {
            DateStart = SimpleDateFormat("dd/MM/yyyy").parse(sStartDate)
            DateEnd = SimpleDateFormat("dd/MM/yyyy").parse(sEndDate)
            val currentdate = Date(Calendar.getInstance().timeInMillis)
            if (DateEnd.equals(currentdate)) {
                DateEnd = currentdate
            }
            if (DateStart.after(DateEnd)) {
                checkBool = false
                Toast.makeText(activity, "Please select proper dates", Toast.LENGTH_SHORT).show()
            }
        }
        if (checkBool) {

            for (element in CustomerList) {
                if (element.id.equals(CustomerObj!!.id)) {
                    val d = SimpleDateFormat("dd/MM/yyyy").parse(element.date)
                    if (DateStart.compareTo(d) * d.compareTo(DateEnd) >= 0) {
                        TempcustomerList.add(element)
                    }
                }
            }
            sortArrayBasedOnDate(TempcustomerList)
            custadapter.updateData(TempcustomerList)
            custlistrv.scheduleLayoutAnimation()
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

    fun sortArrayBasedOnDate(list: java.util.ArrayList<Customers>) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //your own date format

        Collections.sort(list, object : Comparator<Customers> {
            override fun compare(p0: Customers, p1: Customers): Int {
                try {
                    return simpleDateFormat.parse(p1.date)
                        .compareTo(simpleDateFormat.parse(p0.date));
                } catch (e: ParseException) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    return 0
                }
            }

        });
    }

    private fun datePickerDialog(isStartDate: Boolean) {


        //setconstraints to restrict dates
        val constraints = CalendarConstraints.Builder()  // 1
            .setEnd(Calendar.getInstance().timeInMillis)  // 4

        val builder =
            MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select date")
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        builder.setSelection(currentTimeInMillis)
        builder.setCalendarConstraints(constraints.build())
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
                val simpleFormatSelectDates = SimpleDateFormat("dd/MM/yyyy", Locale.US)

                if (isStartDate) {
                    sStartDate = simpleFormat.format(selecteddate)
                    et_startdate.setText(simpleFormat.format(selecteddate))
                    et_startdate.setError(null)
                } else {
                    sEndDate = simpleFormatSelectDates.format(selecteddate)
                    et_enddate.setText(simpleFormat.format(selecteddate))
                    et_enddate.setError(null)
                }
                FilterResults()
            }

        }
        picker.show(fragmentManager!!, picker.toString())
    }

    fun onBackPressed() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


}
