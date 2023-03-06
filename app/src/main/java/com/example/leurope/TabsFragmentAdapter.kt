package com.example.leurope

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsFragmentAdapter(fragmentManager: FragmentManager,behavior:Int): FragmentPagerAdapter(fragmentManager,behavior) {
    val listFragment:MutableList<Fragment> = ArrayList()
    val titleList:MutableList<String> = ArrayList()

    fun addItem(fragment: Fragment,titulo:String){
        listFragment.add(fragment)
        titleList.add(titulo)
    }

    override fun getCount(): Int {
       return listFragment.size
    }
    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}