package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.ViewPagerAdapter
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.fragment.Noting2_Frag
import com.sdstudio.simplewordbook.fragment.Noting_Frag
import com.sdstudio.simplewordbook.fragment.WordBookList_Frag
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_new_wordbooklist.view.*
import java.security.acl.Owner

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tablayout: TabLayout
    private lateinit var wordlistFab: FloatingActionButton
    private lateinit var wordBookListViewModel: WordBookListViewModel
    private lateinit var wordCardViewModel: WordCardViewModel

    private val tabLayoutTextArray = listOf<String>("WordBook List","Translate", "setting")
    private val tabLayoutIconArray = arrayOf(R.drawable.ic_baseline_library_books_24, R.drawable.ic_baseline_g_translate_24,R.drawable.ic_baseline_settings_24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordBookListViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(WordBookListViewModel::class.java)
        wordCardViewModel = ViewModelProvider(this, object:ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application,-1) as T
            }
        }).get(WordCardViewModel::class.java)

        tablayout = tablayout_main
        tablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab==null)
                    fab_main.hide()
                else
                    if(tab.position==0)
                        fab_main.show()
                    else
                        fab_main.hide()
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })


        viewPager2 = viewPager2_main
        viewPager2.adapter = ViewPagerAdapter(this, wordBookListViewModel)

        TabLayoutMediator(tablayout_main, viewPager2){ tab, position ->
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconArray[position])
        }.attach()


        //버튼
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