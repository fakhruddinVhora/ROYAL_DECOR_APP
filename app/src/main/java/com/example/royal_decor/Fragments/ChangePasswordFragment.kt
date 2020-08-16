package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ChangePasswordFragment : Fragment() {

    private lateinit var v: View;

    private lateinit var et_emailid: TextInputEditText
    private lateinit var et_oldpassword: TextInputEditText
    private lateinit var et_newpassword: TextInputEditText
    private lateinit var currentuser: FirebaseUser
    private lateinit var et_confirmpassword: TextInputEditText


    private var mAuth: FirebaseAuth? = null

    private lateinit var pb_changepassword: ProgressBar

    private lateinit var btn_changepassword: Button
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_password, container, false)

        init()
        initialization()


        backImg.setOnClickListener {
            EXIT()
        }
        btn_changepassword.setOnClickListener {
            if (validation()) {
                ChangePassword()
            }
        }

        return v
    }

    private fun ChangePassword() {
        pb_changepassword.visibility = View.VISIBLE
        val credentials: AuthCredential = EmailAuthProvider.getCredential(
            et_emailid.text.toString(),
            et_oldpassword.text.toString()
        )


        currentuser.reauthenticate(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                currentuser.updatePassword(et_confirmpassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            pb_changepassword.visibility = View.GONE
                            DialogCreator()
                        } else {
                            pb_changepassword.visibility = View.GONE
                            Toast.makeText(
                                activity!!.applicationContext,
                                "Something went wrong.. try again later",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                pb_changepassword.visibility = View.GONE
                Toast.makeText(
                    activity!!.applicationContext,
                    "Authentication Issues.. Please check current password once.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onBackPressed() {
        EXIT()
    }

    private fun validation(): Boolean {
        var returnBool = true
        if (et_emailid.text.toString().equals("")) {
            et_emailid.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (et_oldpassword.text.toString().equals("")) {
            et_oldpassword.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (et_newpassword.text.toString().equals("")) {
            et_newpassword.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (et_confirmpassword.text.toString().equals("")) {
            et_confirmpassword.error = Constants.ERROR_FILL_DETAILS;
            returnBool = false
        }
        if (!et_newpassword.text!!.isEmpty() && !et_confirmpassword.text!!.isEmpty()) {
            if (!et_newpassword.text.toString().equals(et_confirmpassword.text.toString())) {
                et_confirmpassword.error = "Password doesnt match"
                returnBool = false
            }
        }

        return returnBool
    }


    private fun init() {
        mAuth = FirebaseAuth.getInstance()


        et_emailid = v.findViewById(R.id.et_emailid)
        et_confirmpassword = v.findViewById(R.id.confirmnewpassword)
        et_oldpassword = v.findViewById(R.id.et_currentpassword)
        et_newpassword = v.findViewById(R.id.et_newpassword)
        btn_changepassword = v.findViewById(R.id.btn_changepassword)

        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)

        pb_changepassword = v.findViewById(R.id.pb_changepassword)
    }

    private fun initialization() {
        currentuser = mAuth?.currentUser!!
        headertext.setText(Constants.CHANGE_PASSWORD)
        et_emailid.setText(currentuser.email)
    }

    private fun EXIT() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    private fun DialogCreator() {
        val dialog = MaterialAlertDialogBuilder(activity)
        dialog.setTitle("Success..")
        val inflater = this.layoutInflater
        dialog.setMessage("Password Changed Successfully")
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