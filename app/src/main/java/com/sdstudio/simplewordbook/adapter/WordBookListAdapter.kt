package com.sdstudio.simplewordbook.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.activity.WordListActivity
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.dialog_new_wordbooklist.view.*
import kotlinx.android.synthetic.main.dialog_wordlist_click.view.*
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


        viewHolder.wordBookBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(parent.context)
            val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_wordlist_click,parent,false)
            val data = viewData[viewHolder.adapterPosition]

            dialogView.textview_name.text = data.name
            dialogView.textview_desc.text = data.description
            viewModel.countCards(data.id).observe(lifecycleOwner, Observer {
                dialogView.textview_size.text = it.toString()
            })

            val dialog = dialogBuilder.setView(dialogView).create()

            //단어장삭제
            dialogView.button_delete.setOnClickListener {
                dialog.hide()
                val secondDialogBuilder = AlertDialog.Builder(parent.context)
                val secondDialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_wordbook_delete,parent,false)
                val secondDialog = secondDialogBuilder.setView(secondDialogView).setPositiveButton("OK"){
                    dialog, i->
                        viewModel.delete(data.id)
                        dialog.dismiss()
                }.setNegativeButton("Cancle"){
                    dialog, i ->
                }.show()
            }

            //단어추가
            dialogView.button_edit.setOnClickListener {
                val intent = Intent(parent.context, WordListActivity::class.java)
                parent.context.startActivity(intent.putExtra("wordBookId",data.id))
            }

            dialog.show()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return viewData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.worBookBtn.text = viewData[position].name
        holder.wordBookBtn.text = viewData[position].name
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        //val worBookBtn: Button = v.button_wordBook
        val wordBookBtn = v.button_wordBook
    }

    fun setWordBook(wordBookList:List<WordBook>){
        viewData = wordBookList
        notifyDataSetChanged()
    }
}