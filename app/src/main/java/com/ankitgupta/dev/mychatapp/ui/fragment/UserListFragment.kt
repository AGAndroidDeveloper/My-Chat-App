package com.ankitgupta.dev.mychatapp.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.adapter.ContactListAdapter
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserListFragment : Fragment() {
    private var sharedElementView: View? = null
    private var db: FirebaseDatabase? = null
    private lateinit var profileList: ArrayList<UserModel>
    private var uId: String? = null
    private var recyclerView: RecyclerView? = null
    private var dialog: ProgressDialog? = null
    private  var adapter: ContactListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment1, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ProgressDialog(context)
        dialog?.setTitle("fetching profile data")
        dialog?.show()
        recyclerView = view.findViewById(R.id.userListRecyclerView)
        db = FirebaseDatabase.getInstance()
        profileList = arrayListOf()
        adapter = ContactListAdapter(view.context,profileList)
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
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("e",error.toString())
            }

        })
    }

}