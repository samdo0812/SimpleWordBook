package com.sdstudio.simplewordbook.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import com.google.android.material.dialog.MaterialDialogs
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.activity.WordListActivity
import com.sdstudio.simplewordbook.activity.WordPlayActivity
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import kotlinx.android.synthetic.main.dialog_wordlist_click.view.*
import kotlinx.android.synthetic.main.item_wordbooklist.view.*


class WordBookListAdapter(val activity: FragmentActivity?, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<WordBookListAdapter.ViewHolder>() {

    private var viewData = listOf<WordBook>()
    private var viewModel: WordBookListViewModel
    var bg = arrayListOf<Int>(R.drawable.wordbook_bg_1,R.drawable.wordbook_bg_2,R.drawable.wordbook_bg_3,R.drawable.wordbook_bg_4)

    init {
        viewModel = activity?.run {
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )
                .get(WordBookListViewModel::class.java)
        } ?: throw Exception("invalid")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_wordbooklist, parent, false)


        val viewHolder = ViewHolder(v)


        viewHolder.wordBookTitle.setOnClickListener {


            val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_wordlist_click, parent, false)
            val dialogBuilder = AlertDialog.Builder(parent.context)
            val data = viewData[viewHolder.adapterPosition]


           // val mDialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_wordbooklist, null)
           // val mbuilder = androidx.appcompat.app.AlertDialog.Builder(this).setView(mDialog)
            //val myAlertDialog = mbuilder.show()
            //myAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



            dialogView.textview_name.text = data.name
            dialogView.textview_desc.text = data.description
            viewModel.countCards(data.id).observe(lifecycleOwner, Observer {
                dialogView.textview_size.text = it.toString()
            })

            val dialog = dialogBuilder.setView(dialogView).create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //단어장삭제
            dialogView.button_delete.setOnClickListener {
                dialog.hide()


                AwesomeDialog.build(parent.context as Activity)
                    .position(AwesomeDialog.POSITIONS.CENTER)
                    .title("WARING")
                    .body("Are you sure you want to delete?")
                    // .icon(R.drawable.ic_congrts)
                    .onPositive("DELETE") {
                        viewModel.delete(data.id)
                        dialog.dismiss()
                    }
                    .onNegative("CANCEL") {
                        dialog.dismiss()
                    }
            }

            //단어추가
            dialogView.button_edit.setOnClickListener {
                val intent = Intent(parent.context, WordListActivity::class.java)
                parent.context.startActivity(
                    intent.putExtra("wordBookId", data.id).putExtra("wordBookName", data.name)
                    )
            }

            dialog.show()

            dialogView.button_play.setOnClickListener {
                val intent = Intent(parent.context, WordPlayActivity::class.java)
                parent.context.startActivity(
                    intent.putExtra("wordBookId", data.id).putExtra("wordBookName", data.name)
                )
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
        holder.wordBookTitle.text = viewData[position].name
        //holder.wordBookBG.setBackgroundResource(bg[position % 4])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        //val worBookBtn: Button = v.button_wordBook
        val wordBookBtn = v.button_wordBook
        val wordBookTitle = v.button_wordBookTitle
        //val wordBookBG = v.wordbook_bg

    }

    fun setWordBook(wordBookList: List<WordBook>) {
        viewData = wordBookList
        notifyDataSetChanged()
    }


}