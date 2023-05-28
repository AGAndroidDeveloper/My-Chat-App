package com.ankitgupta.dev.mychatapp.ui.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.adapter.ContactListAdapter
import com.ankitgupta.dev.mychatapp.databinding.ActivityChatWithUserBinding
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatActivityWithUser : AppCompatActivity() {
    private var sharedElementView: View? = null
    private var db: FirebaseDatabase? = null
    private lateinit var profileList: ArrayList<UserModel>
    private var uId: String? = null
    private var recyclerView: RecyclerView? = null
    private var dialog: ProgressDialog? = null
    private  var adapter: ContactListAdapter? = null
    private var userImage:ImageView? = null
  private var toolbar :Toolbar? = null
    private lateinit var image :ImageView


    private var binding: ActivityChatWithUserBinding? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatWithUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)
     toolbar = findViewById(R.id.toolbar1_layout)
        setSupportActionBar(toolbar)

       val back :ImageView = findViewById(R.id.back_navigation)

                     back.setOnClickListener {
                         onBackPressed()

                     }
        image = findViewById(R.id.user_profilr_image)

        image.setOnClickListener {
            val intent = Intent(this,EditProfile::class.java)
            startActivity(intent)
        }
       // userImage = binding?.appbarLayout!!.findViewById<ImageView>(R.id.user_profilr_image)


        dialog = ProgressDialog(this)
        dialog?.setTitle("fetching profile data")
        dialog?.show()
        recyclerView = binding?.userListRecyclerView

        db = FirebaseDatabase.getInstance()
        profileList = arrayListOf()
        adapter = ContactListAdapter(this,profileList)
        fetchUserData()
    }
    private fun fetchUserData() {

        val dRef = FirebaseDatabase.getInstance().reference.child("User")
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileList.clear()
                for (i in snapshot.children){
                    Log.e("i","$i")
                    val a = i.getValue(UserModel::class.java)
                    Log.e("UserModelObj","$a")
                    val id = i.key
                    if (!id.equals(FirebaseAuth.getInstance().uid)){
                        val a1 = a!!.name
                        val a2 = a.uid
                        uId = a2
                        val a3 = a.description
                        val a4 = a.email
                        val a5 = a.imageUrl
                        val a6 = a.phNumber
                        val ok = "$a1 $a2 $a3 $a4 $a5"
                        Log.e("ok",ok)
                        val obj = UserModel(a1,a4,a3,a5,a6!!,a2)
                        Log.e("obj2", obj.toString())

                        dialog!!.dismiss()
                        profileList.add(a)
                        Log.e("profileList","$profileList")
                        adapter!!.notifyDataSetChanged()

                        recyclerView!!.adapter = adapter
                    }else{
                        val a1 = a!!.name
                        val a2 = a.uid
                        uId = a2
                        val a3 = a.description
                        val a4 = a.email
                        val a5 = a.imageUrl
                        val a6 = a.phNumber
                        val ok = "$a1 $a2 $a3 $a4 $a5"
                        Log.e("ok",ok)
                        val obj = UserModel(a1,a4,a3,a5,a6!!,a2)
                        val requestOptions = RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Optional: Set caching options


                     image.let {
                            Glide.with(this@ChatActivityWithUser)
                                .load(a5)
                                .apply(requestOptions)
                                .into(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("e",error.toString())
            }

        })
    }

}