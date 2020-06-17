package com.example.royal_decor.Activity

import android.R.attr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.royal_decor.Fragments.AddPainterFragment
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class DeskBaseActivity : AppCompatActivity() {

    var sectionName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_base)
        sectionName = intent.getStringExtra("ItemSelected")
        getFragment(sectionName)?.let { loadFragment(it) }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.deskbaselayout, fragment).commit()

    }

    private fun getFragment(sectionName: String): Fragment? {
        when(sectionName){
            Constants.ADD_PAINTER->{
                return  AddPainterFragment()
            }
        }
        return null
    }
}
