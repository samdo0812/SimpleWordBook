package com.sdstudio.simplewordbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordCardAdapter
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_word_list.*
import kotlinx.android.synthetic.main.item_wordcard.*


class WordListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordCardViewModel: WordCardViewModel
    private var wordBookId: Int = 0
    private lateinit var wordCardAdapter: WordCardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    //var wordCardId: Long = 0



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //if(!isAddCardOpen)
        menuInflater.inflate(R.menu.menu_card_list_flip, menu)
        menuInflater.inflate(R.menu.menu_card_list_update, menu)
        menuInflater.inflate(R.menu.menu_card_list_delete, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_card_list_flip -> {
                wordCardAdapter.flipAllCards()
                return true
            }
            R.id.menu_card_list_update -> {
                return false
            }
            R.id.menu_card_list_delete -> {
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


        wordBookId = intent.getIntExtra("wordBookId", 0)

        wordCardViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application, wordBookId) as T
            }
        })
            .get(WordCardViewModel::class.java)

        wordCardAdapter = WordCardAdapter(0, wordBookId)
        viewManager = LinearLayoutManager(applicationContext)

        wordCardViewModel.cardList.observe(this,
            Observer { cards ->
                wordCardAdapter.setCards(cards)
            })

        recyclerView = wordcard_list.apply {
            setHasFixedSize(true)
            adapter = wordCardAdapter
            layoutManager = viewManager

        }

        fab_card.setOnClickListener {
            var intent = Intent(applicationContext, WorddetailActivity::class.java)
            intent.putExtra("wordBookId", wordBookId)
            startActivityForResult(intent, 100)

        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

       var wordCardId =  WordCardAdapter.ViewHolder.wordCardId
        Log.d("wordCardId",wordCardId.toString())

        when (item.itemId) {
            101 -> {
                var intent = Intent(applicationContext, WorddetailActivity::class.java)
                intent.putExtra("wordBookId", wordBookId)
                intent.putExtra("wordCardId",wordCardId)
                startActivityForResult(intent, 101)
                return true
            }
            102 -> {
                Log.d("wordCardId","activity")
                Log.d("wordCardId",wordCardId.toInt().toString())
                wordCardViewModel.delete(wordCardId.toInt())
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

                when (requestCode) {
                //추가
                100->{
                    var word = data!!.getStringExtra("word")
                    var mean = data!!.getStringExtra("mean")
                    wordCardViewModel.insert(WordCard(0, word, mean, wordBookId))
                }
                    //수정
                101->{
                    var word = data!!.getStringExtra("word")
                    var mean = data!!.getStringExtra("mean")
                    var wordCardId = data!!.getLongExtra("wordCardId",0)

                    wordCardViewModel.update(wordCardId.toInt(),word,mean)
                }
            }
    }


}