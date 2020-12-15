package com.sdstudio.simplewordbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.item_wordbooklist.view.*
import java.lang.Exception

class WordBookListAdapter(val activity: FragmentActivity?, val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<WordBookListAdapter.ViewHolder>(){

    private var viewData = listOf<WordBook>()
    private var viewModel: WordBookListViewModel

    init {
        viewModel = activity?.run {
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(WordBookListViewModel::class.java)
        }?: throw Exception("invalid")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_wordbooklist,parent,false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return viewData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.worBookBtn.text = viewData[position].name
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        val worBookBtn: Button = v.button_wordBook
    }

    fun setWordBook(wordBookList:List<WordBook>){
        viewData = wordBookList
        notifyDataSetChanged()
    }
}