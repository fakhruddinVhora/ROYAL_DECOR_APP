package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class AddPainterFragment : Fragment(), View.OnClickListener {

    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var btnAdd: Button
    private lateinit var v: View

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
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.img_back -> {activity!!.finish()}
            R.id.btn_add -> {
                val constant = Constants()
                val t = constant.idGenerator(Constants.isPainter)
                constant.generateSnackBar(
                    activity!!.applicationContext,
                    v,
                    "Painter added successfully::$t"
                )
            }
        }
    }


}
