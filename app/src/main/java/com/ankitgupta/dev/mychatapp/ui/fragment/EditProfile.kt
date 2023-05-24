package com.ankitgupta.dev.mychatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ankitgupta.dev.mychatapp.databinding.ActivityEditProfileBinding
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfile : AppCompatActivity() {
    private var binding :ActivityEditProfileBinding? = null
    private var image :ImageView? = null
    private var name :EditText? = null
    private var email :EditText? = null
    private var description :EditText? = null
    private var submitBtn  :Button? = null
    private var logOutBtn :Button? = null
    private var imageUri :String? = null
    private var phoneNumber :String? = null
    private val db  = FirebaseDatabase.getInstance()
   var uid1 =  FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        image = binding?.profileImageEdit
        name = binding?.enterNameEdit
        email = binding?.enterEmailIdEdit
        description = binding?.enterDescriptionEdit
        submitBtn = binding?.ankitBtnEdit
        logOutBtn = binding?.logOut



        setSupportActionBar(binding?.toolbarEdit)
        binding?.toolbarEdit!!.setNavigationOnClickListener {
            onBackPressed()
        }
        logOutBtn!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }

        submitBtn!!.setOnClickListener {
        if (name!!.text.isNotEmpty()&& email!!.text.isNotEmpty()&&description!!.text.isNotEmpty()) {
            updateProfile(name!!.text.toString(),email!!.text.toString(),description!!.text.toString(),imageUri!!,
                uid1,phoneNumber!!)
        }



        }

        val ref = db.reference.child("User")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (i in snapshot.children){
                    if (i.key==FirebaseAuth.getInstance().currentUser!!.uid){
                        val obj = i.getValue(UserModel::class.java)
                        Log.e("hello","$obj")
                        name!!.setText(obj!!.name)
                        email!!.setText(obj.email)
                        description!!.setText(obj.description)
                        imageUri = obj.imageUrl
                        uid1 = obj.uid.toString()
                        phoneNumber = obj.phNumber
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@EditProfile, error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProfile(a: String, b: String, c: String,uri:String,d:String,e:String) {
        val obj = UserModel(a,b,c,uri,d,e)
        val ref = db.reference.child("User")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(obj).addOnSuccessListener {
                Toast.makeText(this,"Profile updated Successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"some error occoured",Toast.LENGTH_SHORT).show()
            }


    }
}