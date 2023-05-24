package com.ankitgupta.dev.mychatapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ankitgupta.dev.mychatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker

class EnterNumber : Fragment() {
    private lateinit var auth : FirebaseAuth

    private var cCode :Int = 91

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_enter_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

val input :EditText = view.findViewById(R.id.ankit)
        auth = FirebaseAuth.getInstance()

                val a =  view.findViewById<CountryCodePicker>(R.id.country_code)
   a .setOnCountryChangeListener {
            val countryCode = a.selectedCountryCode
            cCode = countryCode.toInt()
        }
        val btn :AppCompatButton = view.findViewById(R.id.sendOtp)
       btn.setOnClickListener {
            val completeNumber = "+$cCode${input.text}"
            Log.e("c",completeNumber)
            Log.e("completeNumber", completeNumber)


            val action = EnterNumberDirections.actionEnterNumberToVerification(completeNumber)
            findNavController().navigate(action)

        }


    }

    override fun onDestroy() {
        super.onDestroy()

       if (auth!=null
       ){
           auth==null
       }

    }
}