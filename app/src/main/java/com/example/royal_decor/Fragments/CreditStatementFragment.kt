package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CreditStatementAdapter
import com.example.royal_decor.Adapters.PainterATVAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.CredStmtCallback
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CreditStatementFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var creditstmtlistrv: RecyclerView
    private lateinit var creditstmtadapter: CreditStatementAdapter
    private lateinit var VerticalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var FilterImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var pb_creditstmt: ProgressBar
    private lateinit var dbhandler: DatabaseHelper

    private lateinit var FilterLayout: LinearLayout

    private lateinit var btn_startdate: Button
    private lateinit var btn_enddate: Button
    private lateinit var et_startdate: TextInputEditText
    private lateinit var et_enddate: TextInputEditText

    private var PainterList: ArrayList<Painters> = ArrayList()
    private var CustomerList: ArrayList<TallyLog> = ArrayList()

    private lateinit var atvPainter: AutoCompleteTextView

    private var PainterObj: Painters? = null
    private var sStartDate: String = ""
    private var sEndDate: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_credit_statement, container, false)
        init()
        initialization()
        feedValuesinSpinner()

        dbhandler.getCredStmt(pb_creditstmt, object : CredStmtCallback {
            override fun returnCredStmtrValues(list: ArrayList<TallyLog>) {
                CustomerList.clear()
                CustomerList = list
                sortArray(list)
                settingAdapter(list)
            }
        })

        backImg.setOnClickListener(this)
        btn_startdate.setOnClickListener(this)
        btn_enddate.setOnClickListener(this)


        FilterImg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (FilterLayout.visibility == View.GONE) {
                    FilterLayout.visibility = View.VISIBLE
                } else {
                    FilterLayout.visibility = View.GONE
                }
            }
        })

        return v
    }

    private fun settingPainterAdapter() {
        val adapter = PainterATVAdapter(v.context, R.layout.list_item, PainterList)
        atvPainter.setAdapter(adapter)
        atvPainter.threshold = 1

        atvPainter.setOnItemClickListener { parent, _, position, _ ->
            val selectedPainter = parent.adapter.getItem(position) as Painters
            atvPainter.setText(selectedPainter.name)
            PainterObj = Painters()
            PainterObj = selectedPainter
            FilterResults()
        }
    }

    private fun FilterResults() {
        var checkBool = true
        var DateStart = Date()
        var DateEnd = Date()
        var TempcustomerList: ArrayList<TallyLog> = ArrayList()


        if (PainterObj == null) {
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
            DateEnd = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(sEndDate)
            if (DateStart.after(DateEnd)) {
                checkBool = false
                Toast.makeText(activity, "Please select proper dates", Toast.LENGTH_SHORT).show()
            }
        }
        if (checkBool) {

            for (element in CustomerList) {
                if (element.painterid.equals(PainterObj!!.id)) {
                    val d = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(element.date)
                    if (DateStart.compareTo(d) * d.compareTo(DateEnd) > 0) {
                        TempcustomerList.add(element)
                    }
                }
            }
            sortArray(TempcustomerList)
            creditstmtadapter.updateData(TempcustomerList)
            creditstmtlistrv.scheduleLayoutAnimation()
        }
    }


    private fun feedValuesinSpinner() {
        dbHelper.getpainterdetails(pb_creditstmt, object : PainterCallback {
            override fun returnPainterValues(list: ArrayList<Painters>) {
                PainterList = list
                settingPainterAdapter()
            }
        })
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.CREDIT_STATEMENT
        FilterImg.visibility = View.VISIBLE
        FilterImg.setImageDrawable(resources.getDrawable(R.drawable.ic_filter))

    }

    private fun init() {

        dbHelper = DatabaseHelper()
        dbHelper.open()
        backImg = v.findViewById(R.id.img_back)
        FilterImg = v.findViewById(R.id.img_logout)
        headertext = v.findViewById(R.id.header_text)
        creditstmtlistrv = v.findViewById(R.id.credstmtlistrv)
        pb_creditstmt = v.findViewById(R.id.pb_credstmt)
        atvPainter = v.findViewById(R.id.atv_painter)


        FilterLayout = v.findViewById(R.id.filterlayout)


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
                val simpleFormatSelectDates = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)

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

    private fun sortArray(list: ArrayList<TallyLog>) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //your own date format

        Collections.sort(list, object : Comparator<TallyLog> {
            override fun compare(p0: TallyLog, p1: TallyLog): Int {
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

}