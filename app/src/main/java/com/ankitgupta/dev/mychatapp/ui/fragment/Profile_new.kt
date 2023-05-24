package com.ankitgupta.dev.mychatapp.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ankitgupta.dev.mychatapp.databinding.ActivityProfileNewBinding
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.ankitgupta.dev.mychatapp.model.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileNew: AppCompatActivity() {
    private var imageView:de.hdodenhof.circleimageview.CircleImageView? = null
    private var binding :ActivityProfileNewBinding? = null
    private var uri : Uri? = null
    private var database :FirebaseDatabase? = null
    private var name : EditText? = null
    private var emailId : EditText?  = null
    private var description : EditText?  = null


    private var dialog: ProgressDialog? = null
    //private val safeArgs : SetUpProfileArgs by navArgs()
    private var image :de.hdodenhof.circleimageview.CircleImageView? = null
    private var btnn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileNewBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        dialog = ProgressDialog(this)
        database = FirebaseDatabase.getInstance()
name = binding?.enterUserName
        emailId = binding?.enterEmailId
        description = binding?.enterDescription
btnn = binding?.ankitBtnSubmit
        imageView = binding!!.userProfileImage
     imageView!!.setOnClickListener {
         ViewModel(this).extracted()
     }
        val number = intent.getStringExtra("number")
        Log.e("number",number!!)
val uid =   FirebaseAuth.getInstance().currentUser!!.uid
        btnn?.setOnClickListener {
           // Log.e("userInfo","$userName $userDescription  $userEmail")

            if (name!!.text!!.isNotEmpty() && name!!.text!!.length>4 &&
                Patterns.EMAIL_ADDRESS.matcher(emailId!!.text.toString()).matches() && description!!.text!!.isNotEmpty()
            ){
                dialog!!.show()
                    val userObj = UserModel(name!!.text!!.toString(),emailId!!.text.toString(),description!!.text!!.toString(),uri.toString()
                        ,
                        number,uid
                      )
                    Log.e("obj","$userObj")

                    addUser(userObj)
                    Toast.makeText(this,"ok my boy!!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this ,"Something is not right please enter valid detail!! ", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun addUser(user: UserModel) {
        val dRef = database!!.reference.child("User")
        user.uid?.let {
            dRef.child(it).setValue(user).addOnCompleteListener { taskk ->
                if (taskk.isSuccessful){
                    dialog!!.dismiss()
                    val intent = Intent(this, ViewPager::class.java)
                    // intent.putExtra(ViewModel.SEND,uid)
                    startActivity(intent)
                    finish()

                    //Toast.makeText(context,"user created successfully", Toast.LENGTH_SHORT).show()

                }

            }
                .addOnFailureListener {
                    Log.e("e",it.message.toString())
                }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ViewModel.CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                Log.e("bitmap","$imageBitmap")
                uri = ViewModel(this).saveImageToExternalStorage(imageBitmap)

                Log.e("uri", "$uri")
                //imageView!!.setImageBitmap(imageBitmap)
                Glide.with(this)
                    .load(uri)
                    .into(imageView!!)

            }
        }

        if (requestCode == ViewModel.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                Log.e("urie","$imageUri")
                uri = imageUri
                Glide.with(this)
                    .load(imageUri)
                    .into(imageView!!)


            }
        }
    }

}