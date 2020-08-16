package com.example.royal_decor.Fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CreditStatementAdapter
import com.example.royal_decor.Adapters.PainterATVAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.CredStmtCallback
import com.example.royal_decor.Interface.DataDeletedSuccessCallback
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.TallyLog
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


class CreditStatementFragment : Fragment(), View.OnClickListener,
    CreditStatementAdapter.credStmtOnclickListener {

    private lateinit var v: View
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var creditstmtlistrv: RecyclerView
    private lateinit var creditstmtadapter: CreditStatementAdapter
    private lateinit var backImg: ImageView
    private lateinit var FilterImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var clearFilters: TextView
    private lateinit var pb_creditstmt: ProgressBar
    private lateinit var dbhandler: DatabaseHelper

    private lateinit var FilterLayout: LinearLayout


    private lateinit var et_startdate: TextInputEditText
    private lateinit var et_enddate: TextInputEditText


    private var PainterList: ArrayList<Painters> = ArrayList()
    private var CreditLogList: ArrayList<TallyLog> = ArrayList()

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
                CreditLogList.clear()
                CreditLogList = list
                sortArrayBasedOnDate(list)
                settingAdapter(list)
            }

        })

        backImg.setOnClickListener(this)


        et_startdate.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_startdate.getRight() - et_startdate.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    datePickerDialog(true)
                    return@OnTouchListener true
                }
            }
            false
        })

        et_enddate.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_enddate.getRight() - et_enddate.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    datePickerDialog(false)
                    return@OnTouchListener true
                }
            }
            false
        })



        FilterImg.setOnClickListener {
            if (FilterLayout.visibility == View.GONE) {
                FilterLayout.visibility = View.VISIBLE
            } else {
                FilterLayout.visibility = View.GONE
            }
        }


        clearFilters.setOnClickListener {
            creditstmtadapter.updateData(CreditLogList)
            creditstmtlistrv.scheduleLayoutAnimation()

            et_enddate.setText("")
            et_startdate.setText("")
            atvPainter.setText("")
            sEndDate = ""
            sStartDate = ""
            PainterObj == null
        }

        return v
    }

    private fun settingPainterAdapter() {
        val adapter = PainterATVAdapter(v.context, R.layout.list_item, PainterList)
        atvPainter.setAdapter(adapter)
        atvPainter.threshold = 1

        atvPainter.setOnItemClickListener { parent, _, position, _ ->
            val selectedPainter = parent.adapter.getItem(position) as Painters
            atvPainter.setText(selectedPainter.name)
            atvPainter.setSelection(atvPainter.text.length)
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
            DateEnd = SimpleDateFormat("dd/MM/yyyy").parse(sEndDate)
            val currentdate = Date(Calendar.getInstance().timeInMillis)
            if (DateEnd.equals(currentdate)) {
                DateEnd = currentdate
            }
            if (DateStart.after(DateEnd)) {
                checkBool = false
                Toast.makeText(activity, "Please select proper dates", Toast.LENGTH_SHORT).show()
                et_enddate.text!!.clear();
            }
        }
        if (checkBool) {

            for (element in CreditLogList) {
                if (element.painterid == PainterObj!!.id) {
                    val d = SimpleDateFormat("dd/MM/yyyy").parse(element.date)
                    if (DateStart.compareTo(d) * d!!.compareTo(DateEnd) >= 0) {
                        TempcustomerList.add(element)
                    }
                }
            }
            sortArrayBasedOnDate(TempcustomerList)
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
        et_startdate.isLongClickable = false
        et_enddate.isLongClickable = false
        et_startdate.showSoftInputOnFocus = false
        et_enddate.showSoftInputOnFocus = false
        et_enddate.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        et_startdate.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        clearFilters.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))


        FilterImg.setImageDrawable(resources.getDrawable(R.drawable.ic_filter))
        val underlinedString = SpannableString(clearFilters.text.toString())
        underlinedString.setSpan(UnderlineSpan(), 0, underlinedString.length, 0)
        clearFilters.setText(underlinedString)
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
        clearFilters = v.findViewById(R.id.clearfilters)


        FilterLayout = v.findViewById(R.id.filterlayout)



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
                activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

        }
    }


    fun ViewCreditInfoDialog(item: TallyLog) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("Tally of ${item.paintername}'s Total of ${item.totalPoints}")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.custom_statement_dialog_layout, null)


        val expectations = dialogView.findViewById<MaterialTextView>(R.id.tally)

        var tallyString = ""
        for (m in item.productMap) {
            tallyString = tallyString + "${m.key} : Qt = ${m.value} " + System.lineSeparator()
        }

        expectations.setText(tallyString)

        dialog.setView(dialogView)
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
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


    override fun OnInfoClick(item: TallyLog) {
        ViewCreditInfoDialog(item)
    }

    override fun OnDeleteClick(item: TallyLog, position: Int) {
        DeleteCreditInfoDialog(item, position)
    }

    private fun DeleteCreditInfoDialog(
        item: TallyLog,
        position: Int
    ) {
        val dialog = MaterialAlertDialogBuilder(activity)
        dialog.setTitle("Warning")
        dialog.setMessage("Are you sure you want to delete?")
        val inflater = this.layoutInflater
        dialog.setPositiveButton("Delete") { dialog, which ->
            dbHelper.deletecredStmt(item, pb_creditstmt, object : DataDeletedSuccessCallback {

                override fun returnIsDataDeletd(isDeleted: Boolean) {
                    if (isDeleted) {
                        creditstmtadapter.deleteData(position)
                        CreditLogList.remove(item)
                    } else {
                        Toast.makeText(
                            activity!!.applicationContext,
                            "Cannot Delete. Please try later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        }
        dialog.setNegativeButton("Cancel") { dialog, which ->

            dialog.dismiss()
        }
        dialog.show()
    }

    fun sortArrayBasedOnDate(list: java.util.ArrayList<TallyLog>) {
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

    fun onBackPressed() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}