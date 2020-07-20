package com.example.royal_decor.Activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.royal_decor.Fragments.*
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


public class DeskBaseActivity : AppCompatActivity() {


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

    public fun closeFragment() {
        finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
            Constants.CUSTOMER_FEEDBACK -> {
                return CustomerFeedBackFragment()
            }
        }
        return null
    }

    override fun onBackPressed() {
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.deskbaselayout)
        val c = supportFragmentManager.backStackEntryCount
        if (currentFragment is AddPainterFragment) {
            (currentFragment as AddPainterFragment).onBackPressed()
        }
        if (currentFragment is AddProductFragment) {
            (currentFragment as AddProductFragment).onBackPressed()
        }
        if (currentFragment is CreditStatementFragment) {
            (currentFragment as CreditStatementFragment).onBackPressed()
        }
        if (currentFragment is CustomerListFragment) {
            (currentFragment as CustomerListFragment).onBackPressed()
        }
        if (currentFragment is EvaluateCreditsFragment) {
            (currentFragment as EvaluateCreditsFragment).onBackPressed()
        }
        if (currentFragment is PainterListFragment) {
            (currentFragment as PainterListFragment).onBackPressed()
        }
        if (currentFragment is ViewCreditFragment) {
            (currentFragment as ViewCreditFragment).onBackPressed()
        }
        if (currentFragment is ViewProductFragment) {
            (currentFragment as ViewProductFragment).onBackPressed()
        }
        if (currentFragment is CustomerFeedBackFragment) {
            (currentFragment as CustomerFeedBackFragment).onBackPressed()
        }
        if (c == 0) {

            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
