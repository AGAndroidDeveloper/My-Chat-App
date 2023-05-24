package com.ankitgupta.dev.mychatapp.ui.fragment

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.databinding.ActivitySettingBinding
import com.ankitgupta.dev.mychatapp.model.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase

class Setting : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
private lateinit var database :FirebaseDatabase
private lateinit var dialog :ProgressDialog
private lateinit var uri :Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
dialog = ProgressDialog(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.changeBackGround.setOnClickListener {
            ViewModel(this).extracted()
        }


    }


}