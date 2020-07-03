package com.example.royal_decor.Activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.royal_decor.Fragments.*
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class DeskBaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_base)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sectionName = intent.getStringExtra("ItemSelected")
        getFragment(sectionName!!)?.let { loadFragment(it) }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.deskbaselayout, fragment).commit()

    }

    private fun getFragment(sectionName: String): Fragment? {
        when (sectionName) {
            Constants.ADD_PAINTER -> {
                return AddPainterFragment()
            }
            Constants.ADD_PRODUCT -> {
                return AddProductFragment()
            }
            Constants.VIEW_CUSTOMER_LIST -> {
                return CustomerListFragment()
            }
            Constants.VIEW_PAINTERS_LIST -> {
                return PainterListFragment()
            }
            Constants.EVALUATE_CREDITS -> {
                return EvaluateCreditsFragment()
            }
            Constants.VIEW_CREDIT_SCORE -> {
                return ViewCreditFragment()
            }
            Constants.VIEW_PRODUCT -> {
                return ViewProductFragment()
            }
            Constants.CREDIT_STATEMENT -> {
                return CreditStatementFragment()
            }
        }
        return null
    }
}
