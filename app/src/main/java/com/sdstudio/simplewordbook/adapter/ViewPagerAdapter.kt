package com.sdstudio.simplewordbook.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sdstudio.simplewordbook.fragment.Noting2_Frag
import com.sdstudio.simplewordbook.fragment.Noting_Frag
import com.sdstudio.simplewordbook.fragment.WordBookList_Frag
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel


class ViewPagerAdapter(fa: FragmentActivity, val wordBookListViewModel: WordBookListViewModel): FragmentStateAdapter(fa) {

    val PAGE_CNT = 3

    override fun getItemCount(): Int {
        return PAGE_CNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> WordBookList_Frag(wordBookListViewModel)
            1 -> Noting_Frag()
            2 -> Noting2_Frag()
            else -> WordBookList_Frag(wordBookListViewModel)
        }
    }

}