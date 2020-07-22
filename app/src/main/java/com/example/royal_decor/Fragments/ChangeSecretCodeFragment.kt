package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class ChangeSecretCodeFragment : Fragment() {
    private lateinit var v: View
    private lateinit var et_oldcode: TextInputEditText
    private lateinit var et_newcode: TextInputEditText
    private lateinit var et_confirnewcode: TextInputEditText
    private lateinit var pb_registeraccount: ProgressBar
    private var mAuth: FirebaseAuth? = null

    private lateinit var btn_ChangeCOde: Button
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_secret_code, container, false)
        init()
        initialization()


        btn_ChangeCOde.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (validation()) {
                    if (et_oldcode.text.toString().equals(Constants.SECRET_CODE)) {
                        Constants.SECRET_CODE = et_newcode.text.toString()
                        et_oldcode.text!!.clear()
                        et_confirnewcode.text!!.clear()
                        et_newcode.text!!.clear()
                        DialogCreator()
                    } else {
                        et_oldcode.error = "Old Code doesnt Match"
                    }
                }
            }
        })


        backImg.setOnClickListener {
            EXIT()
        }
        return v
    }

    private fun validation(): Boolean {
        var returnBool = true
        if (et_oldcode.text.toString().equals("")) {
            et_oldcode.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (et_confirnewcode.text.toString().equals("")) {
            et_confirnewcode.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (et_newcode.text.toString().equals("")) {
            et_newcode.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (!et_newcode.text!!.isEmpty() && !et_confirnewcode.text!!.isEmpty()) {
            if (!et_newcode.text.toString().equals(et_confirnewcode.text.toString())) {
                et_confirnewcode.error = "Password doesnt match"
                returnBool = false
            }
        }

        return returnBool
    }

    private fun initialization() {
        headertext.setText(Constants.CHANGE_SECRET_CODE)
    }

    private fun init() {

        mAuth = FirebaseAuth.getInstance()


        et_oldcode = v.findViewById(R.id.oldcode)
        et_newcode = v.findViewById(R.id.newcode)
        et_confirnewcode = v.findViewById(R.id.confirmnewcode)
        btn_ChangeCOde = v.findViewById(R.id.btn_changecode)

        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)

        pb_registeraccount = v.findViewById(R.id.pb_changesecretcode)
    }

    fun onBackPressed() {
        EXIT()
    }

    private fun EXIT() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    private fun DialogCreator() {
        val dialog = MaterialAlertDialogBuilder(activity)
        dialog.setTitle("Success..")
        val inflater = this.layoutInflater
        dialog.setMessage("Secret Code Changed")
        dialog.setPositiveButton("Exit") { d, _ ->
            d.dismiss()
            EXIT()
        }
        dialog.setNegativeButton("Cancel") { d, _ ->
            d.dismiss()
        }
        dialog.show()
    }

}