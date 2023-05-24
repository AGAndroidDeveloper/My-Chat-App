package com.ankitgupta.dev.mychatapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ankitgupta.dev.mychatapp.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
//        Toast.makeText(requireContext(),"user is already registered ${FirebaseAuth.getInstance().currentUser!!.phoneNumber}",
//            Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_enterNumber)

            },3000L)


    }



}