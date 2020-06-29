package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.royal_decor.Adapters.PainterListAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class PainterListFragment : Fragment(), View.OnClickListener,
    PainterListAdapter.OnPainterClickListener {


    private lateinit var v: View
    private lateinit var viewPager: ViewPager
    private lateinit var painterlistrv: RecyclerView
    private lateinit var painteradapter: PainterListAdapter
    private lateinit var backImg: ImageView
    private lateinit var searchImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var searchView: SearchView
    private lateinit var pb_painter: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_painter_list, container, false)
        init()
        initialization()


        dbHelper.getpainterdetails(pb_painter, object : PainterCallback {
            override fun returnPainterValues(list: ArrayList<Painters>) {
                settingAdapter(list)
            }
        })

        backImg.setOnClickListener(this)
        searchImg.setOnClickListener(this)
        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        searchImg.visibility = View.VISIBLE
        searchImg.setImageDrawable(resources.getDrawable(R.drawable.ic_search))

        headertext.text = Constants.VIEW_PAINTERS_LIST

        dbHelper = DatabaseHelper()
        dbHelper.open()

        val cancelIcon = searchView.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        val searchIcon = searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        val textView = searchView.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        painterlistrv = v.findViewById(R.id.painterlistrv)
        pb_painter = v.findViewById(R.id.pb_painterdetails)
        searchImg = v.findViewById(R.id.img_logout)

        searchView = v.findViewById(R.id.paintersearch)
    }

    fun settingAdapter(painterListData: ArrayList<Painters>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        painterlistrv.layoutManager = RecyclerViewLayoutManager
        painteradapter = PainterListAdapter(painterListData, this)
        painterlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        painterlistrv.adapter = painteradapter
        painterlistrv.scheduleLayoutAnimation()





        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                painteradapter.filter.filter(newText)
                return false
            }

        })
    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.img_logout -> {
                if (searchView.visibility == View.GONE) {
                    searchView.visibility = View.VISIBLE
                } else {
                    searchView.visibility = View.GONE
                }
            }

            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }

    override fun OnDeleteClick(item: Painters) {
        dbHelper.deletepainter(item, pb_painter, object : PainterCallback {
            override fun returnPainterValues(list: ArrayList<Painters>) {
                painteradapter.updatePainterRV(list)
                painterlistrv.scheduleLayoutAnimation()
            }
        })
    }

    override fun OnEditClick(item: Painters) {
        //  Toast.makeText(activity, "EDIT::$item.name", Toast.LENGTH_SHORT).show()
        DialogCreator(item)
    }


    fun DialogCreator(item: Painters) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("Edit ${item.name}'s Details")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.custom_painter_dialog_layout, null)

        val name = dialogView.findViewById<TextInputEditText>(R.id.name)
        val address = dialogView.findViewById<TextInputEditText>(R.id.address)
        val aadhar = dialogView.findViewById<TextInputEditText>(R.id.aadhar)
        val mobile = dialogView.findViewById<TextInputEditText>(R.id.etmobile)
        val etdob = dialogView.findViewById<TextInputEditText>(R.id.dob)
        val btn_dob = dialogView.findViewById<Button>(R.id.btn_selectdate)


        name.setText(item.name)
        address.setText(item.address)
        aadhar.setText(item.aadhar)
        mobile.setText(item.mobile)
        etdob.setText(item.dateofbirth)
        btn_dob.setOnClickListener {
            dobDialog(etdob)
        }

        dialog.setView(dialogView)
        dialog.setPositiveButton("Update") { dialog, which ->
            if (validation(name, address, mobile, etdob, aadhar)) {
                val editObj = Painters(
                    item.id,
                    name.text.toString(),
                    mobile.text.toString(),
                    address.text.toString(),
                    etdob.text.toString(),
                    aadhar.text.toString(),
                    item.credits
                )
                dbHelper.updatepainterdetails(editObj, pb_painter, object : PainterCallback {
                    override fun returnPainterValues(list: ArrayList<Painters>) {
                        painteradapter.updatePainterRV(list)
                        painterlistrv.scheduleLayoutAnimation()
                    }
                })
            } else {
                Toast.makeText(context, "Not Able to Edit", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }





        dialog.show()
    }

    private fun validation(
        pname: TextInputEditText,
        paddress: TextInputEditText,
        pmobilenumber: TextInputEditText,
        pdob: TextInputEditText,
        paadhar: TextInputEditText
    ): Boolean {
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
        if (pdob.text!!.isEmpty()) {
            pdob.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        }
        if (paddress.text!!.isEmpty()) {
            paddress.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (paddress.text.toString().length > 30) {
                paddress.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
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
            if (pmobilenumber.text.toString().length > 10) {
                pmobilenumber.error = Constants.ERROR_EXCEED_LIMIT
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


    private fun dobDialog(etdob: TextInputEditText) {

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
            etdob.setText(simpleFormat.format(date))
        }
        picker.show(fragmentManager!!, picker.toString())

    }
}