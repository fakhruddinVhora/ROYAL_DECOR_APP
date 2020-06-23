package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.textfield.TextInputEditText


class AddPainterFragment : Fragment(), View.OnClickListener {

    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var btnAdd: Button
    private lateinit var v: View
    private lateinit var pname: TextInputEditText
    private lateinit var paddress: TextInputEditText
    private lateinit var pmobilenumber: TextInputEditText
    private lateinit var paadhar: TextInputEditText
    private lateinit var dbhandler: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_add_painter, container, false)
        init()
        initialization()


        btnAdd.setOnClickListener(this)
        backImg.setOnClickListener(this)
        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.ADD_PAINTER
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        btnAdd = v.findViewById(R.id.btn_add)
        pname = v.findViewById(R.id.pname)
        paddress = v.findViewById(R.id.paddress)
        paadhar = v.findViewById(R.id.paadhar)
        pmobilenumber = v.findViewById(R.id.pmobile)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_add -> {

                if (validation()) {
                    val constant = Constants()
                    val pid = constant.idGenerator(Constants.isPainter)
                    dbhandler = DatabaseHelper()
                    dbhandler.open()
                    val painterObj = Painters(
                        pid,
                        pname.text.toString(),
                        pmobilenumber.text.toString(),
                        paddress.text.toString(),
                        paadhar.text.toString()
                    )
                    val response = dbhandler.addpainter(painterObj)
                    if (response) {
                        constant.generateSnackBar(
                            activity!!.applicationContext,
                            v,
                            "Painter added successfully"
                        )
                    } else {
                        constant.generateSnackBar(
                            activity!!.applicationContext,
                            v,
                            "Painter not added successfully"
                        )
                    }
                    pname.text!!.clear()
                    pmobilenumber.text!!.clear()
                    paddress.text!!.clear()
                    paadhar.text!!.clear()
                }

            }
        }
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


}
