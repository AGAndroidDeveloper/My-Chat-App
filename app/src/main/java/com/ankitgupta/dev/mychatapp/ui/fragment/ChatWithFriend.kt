package com.ankitgupta.dev.mychatapp.ui.fragment

import ChatAdapter
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.databinding.ActivityChatWithFriendBinding
import com.ankitgupta.dev.mychatapp.model.Message
import com.ankitgupta.dev.mychatapp.model.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Date


class ChatWithFriend : AppCompatActivity() {
    private var binding : ActivityChatWithFriendBinding? = null
    private var receiverId :String? = null
    private var senderId :String? = null
    private var messageList :ArrayList<Message>? = null
    private var recyclerView :RecyclerView? = null
    private var adapter :ChatAdapter? = null
    private var receiverRoom :String? = null
    private var senderRoom :String? = null
    private var db :FirebaseDatabase? = null
private  var status1 :TextView? = null
    private lateinit var dialog : ProgressDialog
    private lateinit var uri : Uri
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        containerTransform()
        super.onCreate(savedInstanceState)
        binding = ActivityChatWithFriendBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        db = FirebaseDatabase.getInstance()
        setSupportActionBar(binding?.toolbar)
        val customView: View = layoutInflater.inflate(R.layout.toolbar_layout, binding?.toolbar, false)



      binding?.toolbar!!.addView(customView)

status1 = customView.findViewById(R.id.status)

        customView.findViewById<ImageView>(R.id.backNavigation).setOnClickListener {
            onBackPressed()
        }


        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        messageList = arrayListOf()
        recyclerView = binding?.recyclerView

        val listener = object :ChatAdapter.MessageAdapterListener{
            override fun onMessageLongPressed() {
            }

        }
        adapter = ChatAdapter(this@ChatWithFriend, messageList!!,listener,binding?.toolbar)

        //fetchDataFromDataBase()
         // fetchDataFromDataBase()

        val userName = customView.findViewById<TextView>(com.ankitgupta.dev.mychatapp.R.id.userName)
     userName.text = intent.getStringExtra("code")
            intent.getStringExtra("code")?.let { Log.e("text", it) }

  //  val hello  = intent.getStringExtra("status")
        //Log.e("sta",hello!!)


            receiverId = intent.getStringExtra("uid")
            //Log.e("reId", receiverId!!)
fetchStatusData(receiverId)
        val date = Date()
        val now: Calendar = Calendar.getInstance()

        val hour: Int = now.get(Calendar.HOUR_OF_DAY)
        var minute: Int = now.get(Calendar.MINUTE)
        var second: Int = now.get(Calendar.SECOND)
        var millis: Int = now.get(Calendar.MILLISECOND)
        receiverRoom = receiverId + senderId
        senderRoom = senderId + receiverId
        fetchDataFromDataBase(senderRoom!!)
   val a : Date = date
        binding?.send!!.setOnClickListener {
            val message = binding?.inputMessage!!.text.toString()
            val messageObj = Message(senderId, message, System.currentTimeMillis().toString())
            if (message.isNotEmpty()) {
                addToFireBase(messageObj, senderRoom!!, receiverRoom)
                binding?.inputMessage!!.text!!.clear()
            }
        }
    }
    private fun deleteSelectedItem() {
        TODO("Not yet implemented")
    }


    private fun fetchDataFromDataBase(senderRoom1: String) {
        val messagesRef = FirebaseDatabase.getInstance().reference.child("chats").child(senderRoom1)
            .child("message")
        Log.e("reObj", messagesRef.toString())
        messagesRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("DefaultLocale", "SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList!!.clear()
                val newMessageList = mutableListOf<Message>() // Create a new list to store the updated messages
                for (dataSnapshot in snapshot.children) {
                  //  Log.e("snap", dataSnapshot.toString())
                    val message = dataSnapshot.getValue(Message::class.java)
                    val a = message?.content
                    val b = message?.senderId
                    val c = message?.timestamp
                    val obj = c?.let { Message(b,a, it.toString()) }

                   

                    Log.e("time","$c")
                    if (obj != null) {
                        newMessageList.add(obj) // Add the message to the new list
//                        Log.e("asdwe", newMessageList.toString())
//                        Log.e("obj","4$obj")
//                        Log.e("message", message.toString())
                        message.content?.let { Log.e("a", it) }
                       // Log.e("b","${message!!.senderId}")
                        message.content?.let { Log.e("asd", it) }
                    }
                }

                messageList!!.addAll(newMessageList) // Add all the new messages to the list

                adapter?.notifyDataSetChanged()
                 recyclerView!!.adapter = adapter
            // Notify the adapter of the data changes
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("fetch error", error.message)
            }
        })
    }



    private fun addToFireBase(message: Message, sender: String, receiver: String?) {
val ref = db!!.reference.child("chats").child(sender).child("message").push()
        ref.setValue(message).addOnSuccessListener {

            db!!.reference.child("chats").child(receiver!!).child("message").push().setValue(message)
        }
            .addOnFailureListener {

            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(com.ankitgupta.dev.mychatapp.R.menu.menu1,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            com.ankitgupta.dev.mychatapp.R.id.setting ->{
                val intent = Intent(this,Setting::class.java)
                startActivity(intent)
            }
            com.ankitgupta.dev.mychatapp.R.id.backGround ->{
                Toast.makeText(this,"set backGround ",Toast.LENGTH_SHORT).show()
            }
        }


return true
    }
    private fun fetchStatusData(uid: String?) {
        val ref = FirebaseDatabase.getInstance().reference.child("Status")
            .child(uid!!).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (i in snapshot.children){
                        Log.i("i","$i")
                        if (i .exists()){
                            val status = i.getValue(String::class.java)
                            Log.e("status","$status")
                            //status.text = "${i.value}"
status1!!.text = status.toString()
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatWithFriend,"kuch toh hua h ",Toast.LENGTH_SHORT).show()
                    //  status = ""
                }

            })


    }
    private fun addStatusTOdatabase(status :String){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        Log.e("uid",uid)  // JAxV7GXuJJa8h95OoB0m51T3HB13
        val ref = FirebaseDatabase.getInstance().reference.child("Status")
            .child(uid).push()

        ref.setValue(status).addOnSuccessListener {task ->
            // Toast.makeText(context, "ankit = $task", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this,"exception -> ${it.message}", Toast.LENGTH_SHORT).show()

            }


    }
    override fun onResume() {
        super.onResume()
        addStatusTOdatabase("Online")
    }


    override fun onPause() {
        super.onPause()
        addStatusTOdatabase("Offline")
    }

    private fun containerTransform() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS) // enter through transition

        //onTransformationEndContainer(intent.getParcelableExtra("TransformationParams"))
        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 500L
            pathMotion = MaterialArcMotion()

        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
            pathMotion = MaterialArcMotion()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addStatusTOdatabase("Offline")
        if (binding!=null){
            binding = null
        }
        adapter = null
receiverId = null
    senderId= null
messageList = null
recyclerView  = null
 adapter = null
 receiverRoom  = null
 senderRoom = null
 db = null

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ViewModel.CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                uri = ViewModel( this).saveImageToExternalStorage(imageBitmap)!!

               // Log.e("uri", "$uri")
                //   binding?.profileImage!!.setImageBitmap(imageBitmap)
              //  binding!!.ankitChat.background = ContextCompat
              //  val uri: Uri = ViewModel(FirebaseDatabase.getInstance(), this, dialog).saveImageToExternalStorage(imageBitmap)!!

                Glide.with(this)
                    .load(uri)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            binding!!.ankitChat.background = resource
                        }
                    })

            }
        }

        if (requestCode == ViewModel.GALLERY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                if (imageUri != null) {
                    uri = imageUri
                }
                // image!!.setImageURI(imageUri)


            }
        }
    }
}
