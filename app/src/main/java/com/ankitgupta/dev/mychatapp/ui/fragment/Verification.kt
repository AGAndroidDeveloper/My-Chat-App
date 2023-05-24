package com.ankitgupta.dev.mychatapp.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ankitgupta.dev.mychatapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

// TODO:
class Verification : Fragment() {
    var auth : FirebaseAuth?=null
    private var dialog : ProgressDialog? = null
     private var verificationId :String? = null
    private val safeArgs  : VerificationArgs by navArgs()
    private var btn :Button? = null
    private var eText :EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val number = safeArgs.phoneNumber
        Log.e("nbv",number)
        btn = view.findViewById(R.id.verifyOtp)
        eText = view.findViewById(R.id.EnterPhoneOtp)
        dialog = ProgressDialog(context)
        dialog!!.setMessage("sending otp..")
        dialog!!.show()
//        val phoneNumber : String = intent.getStringExtra("number")
//        Log.e("phone",phoneNumber!!)
//        // return phone number
//        Log.e("a","$phoneNumber")
//
        val message :TextView= view.findViewById(R.id.message)
   message.text = buildString {
            append("Enter Otp sent to ")
            append(number)
        }

        auth = FirebaseAuth.getInstance()
          btn!!.setOnClickListener {
            Log.e("id",verificationId.toString())
            val credential = PhoneAuthProvider.getCredential(verificationId!!,eText!!.text.toString())
            signInWithPhoneAuthCredential(credential, number)
        }
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as Activity) // Activity (for callback binding)
            .setCallbacks(
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Log.d("vComplete", "onVerificationCompleted:$credential")
                        signInWithPhoneAuthCredential(credential,number)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.w("vFailed", "onVerificationFailed", e)

                        when (e) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.e("e",e.message.toString())

                            }

                            is FirebaseTooManyRequestsException -> {
                                Log.e("e",e.message.toString())

                            }

                            is FirebaseAuthMissingActivityForRecaptchaException -> {
                                Log.e("e",e.message.toString())
                            }
                        }

                        // Show a message and update the UI
                    }

                    override fun onCodeSent(
                        verify: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.e("codeSent", "onCodeSent:$verify")
                       // Toast.makeText(context as Activity, verify, Toast.LENGTH_SHORT).show()
                        // Save verification ID and resending token so we can use them later
                        dialog!!.dismiss()
                        verificationId = verify

                    }
                }

            )
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, phoneNumber :String) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                dialog!!.dismiss()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signIn", "signInWithCredential:success")
                  //  val action  = VerificationDirections.actionVerificationToSetUpProfile(phoneNumber)
                //  findNavController().navigate(action)
                    val intent = Intent(context, ProfileNew::class.java)
                    intent.putExtra(NUMBER,phoneNumber)
                    startActivity(intent)
                   // (context as Activity).finish()
                    val user = task.result?.user
                    Log.e("user","${user!!.phoneNumber}")
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.e("signInF", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e("fberror","${task.exception}")
                    }
                    // Update UI
                }
            }
    }
    companion object{
        const val NUMBER = "number"
    }


}