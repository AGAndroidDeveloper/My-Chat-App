package com.ankitgupta.dev.mychatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs


class BlankFragment3 : Fragment() {

    //private val safeArgs :BlankFragment3Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank3, container, false)
    }


//    val obj1 = safeArgs.data1
//    val obj2 = safeArgs.data2




}