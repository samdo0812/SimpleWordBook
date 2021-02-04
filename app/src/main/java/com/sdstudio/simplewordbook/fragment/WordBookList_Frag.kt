package com.sdstudio.simplewordbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordBookListAdapter
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.fragment_word_book_list_.*
import kotlinx.android.synthetic.main.fragment_word_book_list_.view.*
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

class WordBookList_Frag(val wordBookViewModel: WordBookListViewModel) : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var wordBookListAdapter: WordBookListAdapter
    lateinit var viewManager:RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_book_list_, container, false)

        wordBookListAdapter = WordBookListAdapter(activity,this)
        wordBookViewModel.wordBookList.observe(this, Observer {
            wordBookListAdapter.setWordBook(it)
        })


        //viewManager = GridLayoutManager(activity,2)
        //viewManager = LinearLayoutManager(activity)
        //viewManager = SkidRightLayoutManager(5F, 1F)

        val linearLayoutManager = ZoomRecyclerLayout(view.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        viewManager = linearLayoutManager

      //  val content = wordbooklist_title.text.toString()


        recyclerView = view.recyclerview_wordList.apply {
            setHasFixedSize(true)
            adapter = wordBookListAdapter
            layoutManager = viewManager
            //addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }




        return view
    }
}