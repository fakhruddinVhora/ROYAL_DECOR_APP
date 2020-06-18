package com.example.royal_decor.Utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.royal_decor.R
import com.google.android.material.snackbar.Snackbar

class Constants {

    companion object{
        val ADD_PRODUCT = "Add Product"
        val EVALUATE_CREDITS = "Evaluate Credits"
        val UPDATE_DATA = "Update Data"
        val ADD_PAINTER = "Add Painter"
        val VIEW_PAINTERS_LIST = "View Painters List"
        val VIEW_CUSTOMER_LIST = "View Customer List"
        val VIEW_CREDIT_SCORE = "View Credit Score"
    }



    fun generateSnackBar(c: Context, v: View, text:String) {
        val snack = Snackbar.make(v, text, Snackbar.LENGTH_LONG)
        snack.setAction("Dismiss", View.OnClickListener {
            snack.dismiss()
        })
        snack.setActionTextColor(c.resources.getColor(R.color.icons))
        snack.view.setPadding(0,0,0,0)
        val view: View = snack.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        view.layoutParams = params
        snack.view.setBackgroundColor(c.resources.getColor(R.color.colorPrimary))
        val textView =
            view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 16f
        textView.setTypeface(
            ResourcesCompat.getFont(
                c.applicationContext,
                R.font.josefinsansregular
            )
        )
        snack.show()
    }
}