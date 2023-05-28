package com.ankitgupta.dev.mychatapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.model.Message
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.ankitgupta.dev.mychatapp.ui.fragment.ChatWithFriend
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ContactListAdapter(
    val context: Context,
    private val list: ArrayList<UserModel>,

    )
    :RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {
    lateinit var a  : ArrayList<String?>
    private lateinit var mList :ArrayList<String>
    private var db  = FirebaseDatabase.getInstance()


    class ContactViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        val profileImg :ImageView = view.findViewById(R.id.profileImage)
        val phnNomber: TextView = view.findViewById<TextView>(R.id.phoneNumber)
        val name = view.findViewById<TextView>(R.id.Name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_profle, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        mList = arrayListOf()
       val data = list[position]
        Glide.with(context)
            .load(data.imageUrl!!.toUri())
            .override(500, 500)
            .encodeFormat(Bitmap.CompressFormat.JPEG) // Specify compression format
            .encodeQuality(80) // Specify compression quality (0-100)
            .into(holder.profileImg)

        //holder.profileImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.man))
        holder.name.text = data.name
        val senderRoom = FirebaseAuth.getInstance().currentUser!!.uid+data.uid
        Log.e("as",FirebaseAuth.getInstance().currentUser!!.uid)
        Log.e("senderRoom",senderRoom)
        returnLastMessage(senderRoom,data,holder)
        //holder.phnNomber.text = mList[mList.size-1]
        Log.e("Hlist", mList.toString())
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatWithFriend::class.java)
            intent.putExtra(CODE,data.name)
            intent.putExtra(UID,data.uid)
          //  val asdasd = fetchStatusData(data.uid)
//            Log.d("asdads",asdasd!!)
//            if (asdasd!=null
//            ){
//                intent.putExtra(STATUS,asdasd)
//            }

            context.startActivity(intent)
        }
    }

    private fun returnLastMessage(senderRoom: String, data: UserModel, holder: ContactViewHolder){
        a = arrayListOf()
        val ref = db.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for(i in snapshot.children){
                        if (i.exists()){
                            val message = i.getValue(Message::class.java)
                            if (message!!.senderId==data.uid){
                          //  a   = arrayListOf()
                                holder.phnNomber.text = message.content
                            //    a.add(message.content)
                                Log.e("${data.uid} = ${message.senderId}","$a")

                            }
                            Log.e("aList","$a")
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
    interface OnItemClickListener {
        fun onItemClick(a:String,b:String)
    }

    companion object{
        const val CODE = "code"
        const val UID = "uid"
    const val STATUS = "status"

    }
}