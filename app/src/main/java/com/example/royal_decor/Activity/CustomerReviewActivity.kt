package com.example.royal_decor.Activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Models.Customers
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

private lateinit var backImg: ImageView
private lateinit var headertext: TextView
private lateinit var btn_dob: ImageButton
private lateinit var btn_submit: Button

private lateinit var cname: TextInputEditText
private lateinit var cemail: TextInputEditText
private lateinit var caddress: TextInputEditText
private lateinit var cmobile: TextInputEditText
private lateinit var cdob: TextInputEditText
private lateinit var csuggestions: TextInputEditText

private lateinit var rg_expectations: RadioGroup
private lateinit var rg_prodshade: RadioGroup
private lateinit var rg_employeeservices: RadioGroup
private lateinit var rg_colorconsultation: RadioGroup


private var expectations: String = ""
private var prodshade: String = ""
private var employeeservices: String = ""
private var colorconsultation: String = ""

private lateinit var ratingbar: RatingBar

private lateinit var dbhandler: DatabaseHelper


class CustomerReviewActivity : AppCompatActivity(), View.OnClickListener {
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

        btn_dob.setOnClickListener(this)
        backImg.setOnClickListener(this)
        btn_submit.setOnClickListener(this)


        rg_prodshade.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                prodshade = radio.text.toString()
            })

        rg_expectations.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                expectations = radio.text.toString()
            })

        rg_employeeservices.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                employeeservices = radio.text.toString()
            })

        rg_colorconsultation.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                colorconsultation = radio.text.toString()
            })

    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = "Customer FeedBack"
        cdob.isEnabled = false
        cdob.resources.getColor(R.color.black)
        dbhandler = DatabaseHelper()
        dbhandler.open()

        csuggestions.setScroller(Scroller(applicationContext));
        csuggestions.isVerticalScrollBarEnabled = true;
    }

    private fun init() {
        backImg = findViewById(R.id.img_back)
        headertext = findViewById(R.id.header_text)
        btn_submit = findViewById(R.id.btn_submit)

        dbhandler = DatabaseHelper()
        dbhandler.open()

        cname = findViewById(R.id.cname)
        cmobile = findViewById(R.id.cmobile)
        cemail = findViewById(R.id.cemail)
        caddress = findViewById(R.id.caddress)
        cdob = findViewById(R.id.et_dateofbirth)
        btn_dob = findViewById(R.id.btn_selectdate)

        ratingbar = findViewById(R.id.ratingbar)

        rg_colorconsultation = findViewById(R.id.rg_colorconsultations)
        rg_employeeservices = findViewById(R.id.rg_employeeservices)
        rg_expectations = findViewById(R.id.rg_expectations)
        rg_prodshade = findViewById(R.id.rg_shadeproduct)


        csuggestions = findViewById(R.id.suggestions)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_selectdate -> {
                datePickerDialog()
            }
            R.id.img_back -> {
                finish()
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            R.id.btn_submit -> {
                if (validation()) {
                    val constant = Constants()
                    val current = Calendar.getInstance()
                    val simpleFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
                    val date = Date(current.timeInMillis)
                    val dateString = simpleFormat.format(date)
                    val obj = Customers(
                        constant.idGenerator(Constants.isCust),
                        dateString,
                        cname.text.toString(),
                        cmobile.text.toString(),
                        caddress.text.toString(),
                        cemail.text.toString(),
                        expectations,
                        prodshade,
                        employeeservices,
                        colorconsultation,
                        ratingbar.rating.toString(),
                        csuggestions.text.toString()
                    )
                    dbhandler.addCustomerfeedback(obj)
                    DialogCreator()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please fill the required details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private fun DialogCreator() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Enter secret Code")
        val inflater = this.layoutInflater
        dialog.setMessage("Thanks for the Review")
        dialog.setPositiveButton("Add Another Review") { dialog, which ->
            dialog.dismiss()
            cleanData()
        }
        dialog.setNegativeButton("Exit") { dialog, which ->
            dialog.dismiss()
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish()
        }
        dialog.show()
    }

    private fun cleanData() {
        cname.text!!.clear()
        caddress.text!!.clear()
        cmobile.text!!.clear()
        cemail.text!!.clear()
        cdob.text!!.clear()
        csuggestions.text!!.clear()
        ratingbar.rating = 1.0f
        rg_colorconsultation.check(rg_colorconsultation.getChildAt(0).id)
        rg_employeeservices.check(rg_employeeservices.getChildAt(0).id)
        rg_expectations.check(rg_expectations.getChildAt(0).id)
        rg_prodshade.check(rg_prodshade.getChildAt(0).id)
    }

    private fun validation(): Boolean {
        var returnbool = true
        var radioStringCheck = ""
        if (cname.text!!.isEmpty()) {
            cname.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (cname.text.toString().length > 30) {
                cname.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (ratingbar.rating == 0f) {
            returnbool = false
            Toast.makeText(applicationContext, "Please rate our services", Toast.LENGTH_SHORT)
                .show()
        }
        if (rg_colorconsultation.checkedRadioButtonId == -1) {
            returnbool = false
        }
        if (rg_employeeservices.checkedRadioButtonId == -1) {
            returnbool = false
        }
        if (rg_expectations.checkedRadioButtonId == -1) {
            returnbool = false
        }
        if (rg_prodshade.checkedRadioButtonId == -1) {
            returnbool = false
        }
        if (cmobile.text!!.isEmpty()) {
            cmobile.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (cmobile.text.toString().length != 10) {
                cmobile.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (caddress.text!!.isEmpty()) {
            caddress.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (caddress.text.toString().length > 100) {
                caddress.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        return returnbool
    }

    private fun datePickerDialog() {


        //setconstraints to restrict dates
        val constraints = CalendarConstraints.Builder()  // 1
        val calendar = Calendar.getInstance()
        //constraints.setStart(calendar.timeInMillis)   //   2
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
            val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val date = Date(it + offsetFromUTC)
            cdob.setText(simpleFormat.format(date))
        }
        picker.show(supportFragmentManager, picker.toString())
    }

}