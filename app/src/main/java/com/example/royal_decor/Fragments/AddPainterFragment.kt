package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.DataAddedSuccessCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class AddPainterFragment : Fragment(), View.OnClickListener {

    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var btnAdd: Button
    private lateinit var v: View
    private lateinit var pname: TextInputEditText
    private lateinit var paddress: TextInputEditText
    private lateinit var pdob: TextInputEditText
    private lateinit var pmobilenumber: TextInputEditText
    private lateinit var paadhar: TextInputEditText
    private lateinit var dbhandler: DatabaseHelper
    private lateinit var pb_addpainter: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_add_painter, container, false)
        init()
        initialization()

        btnAdd.setOnClickListener(this)
        backImg.setOnClickListener(this)

        pdob.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= pdob.getRight() - pdob.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    datePickerDialog()
                    return@OnTouchListener true
                }
            }
            false
        })

        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.ADD_PAINTER
        pdob.setShowSoftInputOnFocus(false);
        pdob.isLongClickable = false
        pdob.resources.getColor(R.color.black)
        dbhandler = DatabaseHelper()
        dbhandler.open()
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        btnAdd = v.findViewById(R.id.btn_add)
        pname = v.findViewById(R.id.pname)
        paddress = v.findViewById(R.id.paddress)
        paadhar = v.findViewById(R.id.paadhar)
        pdob = v.findViewById(R.id.et_dateofbirth)
        pmobilenumber = v.findViewById(R.id.pmobile)
        pb_addpainter = v.findViewById(R.id.pb_addpainter)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
                activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }

            R.id.btn_add -> {
                if (validation()) {
                    val constant = Constants()
                    dbhandler.checkPainterDataForDuplicates(pb_addpainter,
                        pmobilenumber.text.toString(),
                        object : DataAddedSuccessCallback {
                            override fun returnIsAddedSuccessfully(isSuccess: Boolean) {
                                if (isSuccess) {
                                    AddingPainter(constant)
                                } else {
                                    constant.generateSnackBar(
                                        activity!!.applicationContext,
                                        v,
                                        "Painter with this mobile number already exists"
                                    )
                                }
                            }
                        })
                }
            }
        }
    }

    private fun AddingPainter(constant: Constants) {
        val pid = constant.idGenerator(Constants.isPainter)

        val painterObj = Painters(
            pid,
            pname.text.toString(),
            pmobilenumber.text.toString(),
            paddress.text.toString(),
            pdob.text.toString(),
            paadhar.text.toString()
        )

        dbhandler.addpainter(
            pb_addpainter,
            painterObj,
            object : DataAddedSuccessCallback {
                override fun returnIsAddedSuccessfully(isSuccess: Boolean) {
                    if (isSuccess) {
                        DialogCreator("Success", "Painter Added Successfully")
                        pname.text!!.clear()
                        pmobilenumber.text!!.clear()
                        paddress.text!!.clear()
                        paadhar.text!!.clear()
                        pdob.text!!.clear()
                        pname.isFocusable = true
                    } else {
                        DialogCreator("Failure", "Failed to Add Painter")
                    }
                }
            })
    }

    private fun validation(): Boolean {
        var returnbool = true
        if (pname.text!!.isEmpty()) {
            pname.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (pname.text.toString().length > 30) {
                pname.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        /*if (pdob.text!!.isEmpty()) {
            pdob.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        }*/

        if (paddress.text!!.isEmpty()) {
            paddress.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (paddress.text.toString().length > 100) {
                paddress.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (pmobilenumber.text!!.isEmpty()) {
            pmobilenumber.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (pmobilenumber.text.toString().length != 10) {
                pmobilenumber.error = "10 Digit Mobile Number Needed.."
                returnbool = false
                returnbool = false
            }
        }
        if (paadhar.text!!.isEmpty()) {
            paadhar.setText("")
        } else {
            if (paadhar.text.toString().length > 16) {
                paadhar.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        return returnbool
    }

    private fun datePickerDialog() {


        //setconstraints to restrict dates
        val constraints = CalendarConstraints.Builder()  // 1
            /* .setValidator(DateValidatorPointForward.from(System.currentTimeMillis()))// 4*/
            .setEnd(System.currentTimeMillis())
            .build()


        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select date")
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        builder.setCalendarConstraints(constraints)
        builder.setSelection(currentTimeInMillis)
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {

            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -18)
            cal.add(Calendar.DATE, -1)
            val dateminus18 = cal.time

            val timeZoneUTC: TimeZone = TimeZone.getDefault()
            val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().time) * -1
            val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val selecteddate = Date(it + offsetFromUTC)
            if (selecteddate.before(Date(Calendar.getInstance().timeInMillis))) {
                if (selecteddate.before(dateminus18)) {
                    pdob.setText(simpleFormat.format(selecteddate))
                } else {
                    pdob.setText("")
                    Toast.makeText(
                        activity,
                        "Age should be greater than 18 years",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                pdob.setText("")
                Toast.makeText(activity, "Future dates not allowed", Toast.LENGTH_SHORT).show()
            }
        }
        picker.show(fragmentManager!!, picker.toString())
    }

    fun onBackPressed() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private fun DialogCreator(title: String, msg: String) {
        val dialog = MaterialAlertDialogBuilder(activity)
        dialog.setTitle(title)
        dialog.setMessage("Add Another Painter?")
        val inflater = this.layoutInflater
        dialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
        }
        dialog.setNegativeButton("No") { dialog, which ->

            activity!!.finish()
            activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
        dialog.show()
    }

}
