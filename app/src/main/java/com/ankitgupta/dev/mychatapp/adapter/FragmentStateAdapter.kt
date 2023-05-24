package com.ankitgupta.dev.mychatapp.adapter
import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

   class MyAdapter(fm:FragmentActivity,private val list :ArrayList<Fragment>) :FragmentStateAdapter(fm){
       private var a :ArrayList<Fragment> = list
       override fun getItemCount(): Int {
           Log.e("size","${a.size}")
       return a.size
    }

    override fun createFragment(position: Int): Fragment {
        return a[position]
    }

       @SuppressLint("NotifyDataSetChanged")
       fun setFragments(fragmentList: List<Fragment>) {
          a.clear()
     a.addAll(fragmentList)
           notifyDataSetChanged()
       }


}

