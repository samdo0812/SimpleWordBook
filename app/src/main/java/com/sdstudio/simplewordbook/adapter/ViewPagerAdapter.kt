package com.sdstudio.simplewordbook.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sdstudio.simplewordbook.fragment.MainPreference
import com.sdstudio.simplewordbook.fragment.Setting_Frag
import com.sdstudio.simplewordbook.fragment.Translator_Frag
import com.sdstudio.simplewordbook.fragment.WordBookList_Frag
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel


class ViewPagerAdapter(fa: FragmentActivity, val wordBookListViewModel: WordBookListViewModel, val wordCardViewModel: WordCardViewModel): FragmentStateAdapter(fa) {

    val PAGE_CNT = 3

    override fun getItemCount(): Int {
        return PAGE_CNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> WordBookList_Frag(wordBookListViewModel)
            1 -> Translator_Frag()
            //2 -> MainPreference()
            2->Setting_Frag(wordBookListViewModel, wordCardViewModel)
            else -> WordBookList_Frag(wordBookListViewModel)
        }
    }

}