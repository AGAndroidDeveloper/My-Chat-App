package com.ankitgupta.dev.mychatapp.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.ankitgupta.dev.mychatapp.model.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SetUpProfile : Fragment() {
    private var database = FirebaseDatabase.getInstance()
    private var name :AppCompatEditText? = null
    private var emailId :AppCompatEditText?  = null
    private var description :AppCompatEditText?  = null
 private var mName :String? = null
    private var mEmailId :String?  = null
    private var mDescription :String?  = null
    private var uri :Uri? = null
    private var dialog: ProgressDialog? = null
//private val safeArgs : SetUpProfileArgs by navArgs()
private var image :de.hdodenhof.circleimageview.CircleImageView? = null
    private var btnn :Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_up_profile, container, false)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ProgressDialog(view.context)
        image = view.findViewById(R.id.userProfileImage)
        btnn = view.findViewById(R.id.ankitBtnSubmit)
        name = view.findViewById(R.id.enterName)
        emailId = view.findViewById(R.id.enterEmailId)
        description = view.findViewById(R.id.enterDescription)


       image!!.setOnClickListener {
           val viewmodel = ViewModel(requireContext())

     viewmodel.extracted()
        }
        val userName = name!!.text.toString().trim()
        val userEmail = emailId!!.text.toString().trim()
        val userDescription  = description!!.text!!.toString().trim()
       btnn?.setOnClickListener {
           Log.e("userInfo","$userName $userDescription  $userEmail")

          // val number =   safeArgs.number
           if (name!!.text!!.isNotEmpty() && name!!.text!!.length>4 &&
               Patterns.EMAIL_ADDRESS.matcher(emailId!!.text.toString()).matches() && description!!.text!!.isNotEmpty()
           ){
               if (uri!!.isAbsolute){
                   val userObj = UserModel(name!!.text!!.toString(),emailId!!.text.toString(),description!!.text!!.toString(),uri.toString()
                       ,"",FirebaseAuth.getInstance().currentUser!!.uid)
                   Log.e("obj","$userObj")
                   addUser(userObj)
                   Toast.makeText(context,"ok my boy!!",Toast.LENGTH_SHORT).show()

               }
                      }
           else{


               Toast.makeText(context,"Something is not right please enter valid detail!! ",Toast.LENGTH_SHORT).show()
           }



        }

    }


    private fun addUser(user: UserModel) {
        val dRef = FirebaseDatabase.getInstance().reference.child("User")
        dRef.child(user.uid!!).setValue(user).addOnCompleteListener { taskk ->
            if (taskk.isSuccessful){
                dialog!!.dismiss()
                val intent = Intent(context, ChatActivityWithUser::class.java)
                 // intent.putExtra(ViewModel.SEND,uid)
               startActivity(intent)

                //Toast.makeText(context,"user created successfully", Toast.LENGTH_SHORT).show()

            }

        }
            .addOnFailureListener {
                 Log.e("e",it.message.toString())
            }
    }
    @SuppressLint("SuspiciousIndentation", "UseRequireInsteadOfGet")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ViewModel.CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                Log.e("bitmap","$imageBitmap")
                uri = ViewModel(requireContext()).saveImageToExternalStorage(imageBitmap)

                Log.e("uri", "$uri")
                 view!!.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.userProfileImage).setImageBitmap(imageBitmap)
                Glide.with(this)
                    .load(uri)
                    .into(image!!)

            }
        }

        if (requestCode == ViewModel.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                Log.e("urie","$imageUri")
                uri = imageUri
                Glide.with(this)
                    .load(imageUri)
                    .into(image!!)


            }
        }
    }


}