package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.ViewPagerAdapter
import com.sdstudio.simplewordbook.fragment.Noting2_Frag
import com.sdstudio.simplewordbook.fragment.Noting_Frag
import com.sdstudio.simplewordbook.fragment.WordBookList_Frag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ViewPagerAdapter(this)
        val fragments = listOf<Fragment>(WordBookList_Frag(), Noting_Frag(), Noting2_Frag())

        adapter.fragments.addAll(fragments)
        viewPager2 = viewPager2_main
        viewPager2.adapter = adapter

        val tabTitle = listOf<String>("Word List","Noting", "Noting2")
        TabLayoutMediator(tablayout_main,viewPager2){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }
}