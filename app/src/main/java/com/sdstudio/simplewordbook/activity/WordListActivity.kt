package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordCardAdapter
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_word_list.*
import kotlinx.android.synthetic.main.dialog_word_add.view.*

class WordListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordCardViewModel: WordCardViewModel
    private var wordBookId: Int = 0
    private lateinit var wordCardAdapter:WordCardAdapter
    private lateinit var viewManager:RecyclerView.LayoutManager
    private var selectedEditCardId:Int = -1
    var isAddCardOpen:Boolean = false
    var wordCardId:Long = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //if(!isAddCardOpen)
        menuInflater.inflate(R.menu.menu_card_list_flip,menu)
        menuInflater.inflate(R.menu.menu_card_list_update,menu)
        menuInflater.inflate(R.menu.menu_card_list_delete,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_card_list_flip -> {
                wordCardAdapter.flipAllCards()
                return true
            }
            R.id.menu_card_list_update ->{
                return false
            }
            R.id.menu_card_list_delete ->{
                return false
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        setSupportActionBar(toolbar_card_list)
        supportActionBar?.title = "My Word"
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_flip_24)

        wordBookId = intent.getIntExtra("wordBookId",0)

        fab_card.setOnClickListener {
            val mDialog = LayoutInflater.from(this).inflate(R.layout.dialog_word_add, null)
            val mbuilder = AlertDialog.Builder(this).setView(mDialog)
            val myAlertDialog = mbuilder.show()

                mDialog.addwordButton.setOnClickListener{
                    var addword = mDialog.addword.text.toString()
                    var addmean = mDialog.addmean.text.toString()

                    if (addword.isNotEmpty() && addmean.isNotEmpty()) {
                        wordCardViewModel.insert(WordCard(0, addword, addmean, wordBookId))
                    }
                    myAlertDialog.dismiss()
                }
                mDialog.addwordCancel.setOnClickListener {
                    myAlertDialog.dismiss()
                }

        }

        wordCardViewModel = ViewModelProvider(this,object:ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application,wordBookId) as T
            }
        })
            .get(WordCardViewModel::class.java)

        wordCardAdapter = WordCardAdapter(null,0)
        //viewManager = GridLayoutManager(applicationContext,2)
        viewManager = LinearLayoutManager(applicationContext)

        wordCardViewModel.cardList.observe(this,
        Observer {
            cards-> wordCardAdapter.setCards(cards)
        })

        recyclerView = wordcard_list.apply {
            setHasFixedSize(true)
            adapter = wordCardAdapter
            layoutManager = viewManager
        }
    }
}