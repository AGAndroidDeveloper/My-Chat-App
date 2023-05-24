package com.ankitgupta.dev.mychatapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.databinding.ConntactProfileBinding
import com.ankitgupta.dev.mychatapp.model.Message
import com.ankitgupta.dev.mychatapp.model.UserModel
import com.ankitgupta.dev.mychatapp.ui.fragment.ChatWithFriend
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


//[hallo, habhai, kya haal h, kaha ja rhe , hello@gmail.com in ,
// helloas, ha bhai kya haal chal , hii man whatsapp, hello bro ,
// you are doing something  excited, ankit gupta kya haal chal h bhai ,
// hello bro kya haal chal , android developerf🍑🚗🚘🚘🚘, ankit gupta ,
// kya haal h bro #292F3F]
class ContactListAdapter(
    val context: Context,
    private val list: ArrayList<UserModel>,

    )
    :RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {
    lateinit var a  : ArrayList<String?>
    private lateinit var mList :ArrayList<String>
    private lateinit var itemClickListener: AdapterView.OnItemClickListener
    private var db  = FirebaseDatabase.getInstance()
    private var status :String? = null

    class ContactViewHolder(binding: ConntactProfileBinding) :RecyclerView.ViewHolder(binding.root) {
        val profileImg = binding.profileImage
        val phnNomber = binding.phoneNumber
        val name = binding.Name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConntactProfileBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        mList = arrayListOf()
       val data = list[position]
        Glide.with(context)
            .load(data.imageUrl!!)
            .placeholder(R.drawable.man)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(holder.profileImg)

        holder.profileImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.man))
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