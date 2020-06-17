package com.example.royal_decor.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.royal_decor.Fragments.BarGraphFragment
import com.example.royal_decor.Fragments.PieChartFragement

class GraphViewAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PieChartFragement()
            }
            1 -> {
                return BarGraphFragment()
            }

            else -> return PieChartFragement()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}