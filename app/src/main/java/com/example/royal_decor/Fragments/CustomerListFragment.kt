package com.example.royal_decor.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.royal_decor.R

class CustomerListFragment : Fragment() {

    private lateinit var v : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.fragment_customer_list, container, false)


        return v


    }


}
