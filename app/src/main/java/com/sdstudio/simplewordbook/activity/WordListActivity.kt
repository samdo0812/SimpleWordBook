package com.sdstudio.simplewordbook.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordCardAdapter
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_word_list.*


class WordListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordCardViewModel: WordCardViewModel
    private var wordBookId: Int = 0
    private lateinit var wordCardAdapter: WordCardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    //var wordCardId: Long = 0
    var ttsIntFlg: Int = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_card_list_add, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_card_list_update -> {
                var intent = Intent(applicationContext, WorddetailActivity::class.java)
                intent.putExtra("wordBookId", wordBookId)
                startActivityForResult(intent, 100)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        setSupportActionBar(toolbar_card_list)
        supportActionBar?.title = "My Word"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_left)
        // fab_card.attachToRecyclerView(wordcard_list)


        setting()
        wordBookId = intent.getIntExtra("wordBookId", 0)
        val wordBookName = intent.getStringExtra("wordBookName")
        supportActionBar?.title = wordBookName

        wordCardViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application, wordBookId) as T
            }
        })
            .get(WordCardViewModel::class.java)

        wordCardAdapter = WordCardAdapter(0)
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


    }

    fun setting() {
        val t = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val ttsFlg = t.getString("language_tts", "KOREAN")

            when (ttsFlg) {
                "KOREAN" -> {
                    ttsIntFlg = 0
                }
                "ENGLISH(UK)" -> {
                    ttsIntFlg = 1
                }
                "ENGLISH(US)" -> {
                    ttsIntFlg = 2
                }
                "JAPANESE" -> {
                    ttsIntFlg = 3
                }
                "CHINESE" -> {
                    ttsIntFlg = 4
                }
            }

        Log.d("sss",TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL.toString())



           /* val installTTSIntent = Intent()
            installTTSIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            startActivity(installTTSIntent)*/



    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var wordCardId = WordCardAdapter.ViewHolder.wordCardId

        when (item.itemId) {
            101 -> {
                var intent = Intent(applicationContext, WorddetailActivity::class.java)
                intent.putExtra("wordBookId", wordBookId)
                intent.putExtra("wordCardId", wordCardId)
                startActivityForResult(intent, 101)
                return true
            }
            102 -> {
                Log.d("wordCardId", "activity")
                Log.d("wordCardId", wordCardId.toInt().toString())
                wordCardViewModel.delete(wordCardId.toInt())
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                //추가
                100 -> {
                    var word = data!!.getStringExtra("word")
                    var mean = data!!.getStringExtra("mean")
                    wordCardViewModel.insert(WordCard(0, word, mean, wordBookId))
                }
                //수정
                101 -> {
                    var word = data!!.getStringExtra("word")
                    var mean = data!!.getStringExtra("mean")
                    var wordCardId = data!!.getLongExtra("wordCardId", 0)

                    wordCardViewModel.update(wordCardId.toInt(), word, mean)
                }
            }
        }
    }
}