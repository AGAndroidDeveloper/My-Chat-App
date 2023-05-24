package com.ankitgupta.dev.mychatapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ankitgupta.dev.mychatapp.BlankFragment
import com.ankitgupta.dev.mychatapp.BlankFragment3
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.adapter.MyAdapter
import com.ankitgupta.dev.mychatapp.databinding.ActivityViewPagerBinding
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.ankitgupta.dev.mychatapp.model.ViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewPager : AppCompatActivity() {
    private var binding :ActivityViewPagerBinding? = null
     private var tabLayout : TabLayout? = null
    private   var  viewPager :ViewPager2? = null


    private var editImage :ImageView? = null
    private      lateinit var profileImageView :ImageView
    private       lateinit var profileNameView :TextView
    private       lateinit var profileEmailView :TextView
    private lateinit var profileList: ArrayList<UserModel>
    private val a :ArrayList<String> = arrayListOf("chats","status","calls")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        tabLayout =binding?.tabLayout
        viewPager = binding?.viewPager
        binding?.navigationView1!!.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener true
        }
        val list :ArrayList<Fragment> = arrayListOf(UserListFragment(),
            BlankFragment(),
            BlankFragment3()
        )
        val adapter  = MyAdapter(this,list)
        setSupportActionBar(binding?.toolbar)

        supportActionBar!!.title = "WhatsApp"
        binding?.toolbar?.navigationIcon = ContextCompat.getDrawable(this,
            R.drawable.baseline_menu_24
        )
        binding?.toolbar!!.setNavigationOnClickListener {
           binding?.drawerLayout!!.open()
        }
// Inflate the header layout
       // val headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_layout, navigationView, false)
// In your activity or fragment
        val navigationView = findViewById<NavigationView>(R.id.navigationView1)
        val headerView = navigationView.getHeaderView(0) // Get the first header view

 profileImageView  = headerView.findViewById(R.id.myImage)
      profileNameView = headerView.findViewById(R.id.myName)
profileEmailView = headerView.findViewById(R.id.myEmail)
        editImage = headerView.findViewById(R.id.editPhoto)
        editImage!!.setOnClickListener {
            val intent = Intent(this,EditProfile::class.java)
            startActivity(intent)
        }

fetchCurrentUserProfileData()

        viewPager!!.adapter = adapter
        viewPager!!.background = ContextCompat.getDrawable(this, R.color.primaryContainer)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = a[position]
        }.attach()
        profileList = arrayListOf()
    }
    private fun fetchCurrentUserProfileData() {

        val dRef = FirebaseDatabase.getInstance().reference.child("User")
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (i in snapshot.children){
                    Log.e("i","$i")
                    val a = i.getValue(UserModel::class.java)
                    Log.e("UserModelObj","$a")
                    val id = i.key
                    Log.e("asde","$id")
                    Log.e("myauth", FirebaseAuth.getInstance().currentUser!!.uid)
                    if (id.equals(FirebaseAuth.getInstance().uid)){
                        val a1 = a!!.name
                        Log.e("asdw",a1!!)
                        Log.e("PROFILE","${a1} ${a.email}")
//
profileNameView.text = a1
                        profileEmailView.text = a.email
                        Glide.with(this@ViewPager)
                            .load(a.imageUrl!!.toUri())
                            .into(profileImageView)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("e",error.toString())
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu123,menu)

    return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.editProfile -> {
                val intent = Intent(this, EditProfile::class.java)
                startActivity(intent)
            }
            R.id.backGround ->{
                ViewModel(this).extracted()
                Toast.makeText(this,"set backGround ", Toast.LENGTH_SHORT).show()
            }
        }

return true
    }

}