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
import com.example.royal_decor.Models.Product
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.textfield.TextInputEditText


class AddProductFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var addBtn: Button

    private lateinit var et_prodname: TextInputEditText
    private lateinit var et_prodcode: TextInputEditText
    private lateinit var et_prodcredits: TextInputEditText

    private lateinit var dbHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_add_product, container, false)

        init()
        initialization()
        backImg.setOnClickListener(this)
        addBtn.setOnClickListener(this)
        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.ADD_PRODUCT
        dbHelper = DatabaseHelper()
        dbHelper.open()
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        addBtn = v.findViewById(R.id.btn_addproduct)

        et_prodcode = v.findViewById(R.id.prod_code)
        et_prodcredits = v.findViewById(R.id.prod_credits)
        et_prodname = v.findViewById(R.id.prod_name)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_addproduct -> {
                if (validation()) {
                    val constant = Constants()
                    val id = constant.idGenerator(Constants.isProduct)
                    val obj = Product(
                        id,
                        et_prodcode.text.toString(),
                        et_prodname.text.toString(),
                        et_prodcredits.text.toString()
                    )
                    val returnbool = dbHelper.addproduct(obj)
                    if (returnbool) {
                        constant.generateSnackBar(
                            activity!!.applicationContext,
                            v,
                            "Product Added Successfully"
                        )

                        et_prodname.setText("")
                        et_prodcredits.setText("")
                        et_prodcode.setText("")
                    }
                }


            }
        }

    }

    private fun validation(): Boolean {
        var returnbool = true
        if (et_prodname.text!!.isEmpty()) {
            et_prodname.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_prodname.text.toString().length > 50) {
                et_prodname.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }

        if (et_prodcredits.text!!.isEmpty()) {
            et_prodcredits.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_prodcredits.text.toString().length > 3) {
                et_prodcredits.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (et_prodcode.text!!.isEmpty()) {
            et_prodcode.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_prodcode.text.toString().length > 20) {
                et_prodcode.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }

        return returnbool
    }

}
