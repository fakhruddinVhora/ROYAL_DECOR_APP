package com.example.royal_decor.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class AddProductFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var addBtn : Button

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
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        addBtn = v.findViewById(R.id.btn_product)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_product -> {
                val constant = Constants()
                constant.generateSnackBar(
                    activity!!.applicationContext,
                    v,
                    "Product Added Successfully"
                )
            }
        }

    }


}
