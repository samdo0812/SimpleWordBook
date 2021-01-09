package com.sdstudio.simplewordbook.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordBookListAdapter
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.fragment_word_book_list_.view.*

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

        //viewManager = LinearLayoutManager(activity)
        viewManager = GridLayoutManager(activity,2)

        recyclerView = view.recyclerview_wordList.apply {
            setHasFixedSize(true)
            adapter = wordBookListAdapter
            layoutManager = viewManager
            //addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        return view
    }
}