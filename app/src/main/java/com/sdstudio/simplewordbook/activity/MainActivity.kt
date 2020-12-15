package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.ViewPagerAdapter
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.fragment.Noting2_Frag
import com.sdstudio.simplewordbook.fragment.Noting_Frag
import com.sdstudio.simplewordbook.fragment.WordBookList_Frag
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_new_wordbooklist.view.*

class MainActivity : AppCompatActivity() {

    lateinit var viewPager2: ViewPager2
    lateinit var wordlistFab: FloatingActionButton
    lateinit var wordBookListViewModel: WordBookListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordBookListViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(WordBookListViewModel::class.java)

        val tabTitle = listOf<String>("Word List","Noting", "Noting2")

        viewPager2 = viewPager2_main
        viewPager2.adapter = ViewPagerAdapter(this, wordBookListViewModel)

        TabLayoutMediator(tablayout_main,viewPager2){ tab, position ->
            tab.text = tabTitle[position]
        }.attach()



        wordlistFab = fab_main
        wordlistFab.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_wordbooklist,null)
            dialog.setView(dialogView).setPositiveButton("OK") {
                    dialog,i->
                val wordBook = WordBook(0,
                    dialogView.edittext_new_name.text.toString(),
                    dialogView.edittext_new_desc.text.toString(),0)
                wordBookListViewModel.insert(wordBook)

            }.setNegativeButton("Cancel"){
                    dialog,i->
            }.show()
        }
    }
}