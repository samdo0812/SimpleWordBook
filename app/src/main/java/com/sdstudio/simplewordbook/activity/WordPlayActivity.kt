package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.WordCardAdapter
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_word_list.*
import kotlinx.android.synthetic.main.activity_word_play.*
import kotlinx.android.synthetic.main.activity_word_play.view.*
import kotlinx.android.synthetic.main.activity_word_play.viewpager2_deck_play
import java.nio.file.Files.delete

class WordPlayActivity : AppCompatActivity() {

    private lateinit var wordCardViewModel: WordCardViewModel
    private lateinit var wordCardAdapter: WordCardAdapter
    var isAllFlip = false


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_play)
        setSupportActionBar(toolbar_wordcard_play)
        supportActionBar?.title = "My Card Play"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_left)


        val wordBookId = intent.getIntExtra("wordBookId",0)
        val wordBookName = intent.getStringExtra("wordBookName")
        supportActionBar?.title = wordBookName

        //wordCardAdapter = WordCardAdapter(seekbar_deck_play,1)
        wordCardAdapter = WordCardAdapter(1,0)

        wordCardViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application, wordBookId) as T
            }
        }).get(WordCardViewModel::class.java)

        wordCardViewModel.cardList.observe(this,
            Observer {
                cards -> wordCardAdapter.setCards(cards)
            })

        //카드
        viewpager2_deck_play.apply {
            adapter = wordCardAdapter
            clipToPadding = false
            offscreenPageLimit = 1

            registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

 /*       //바
        seekbar_deck_play.apply {
            max = 0
            setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewpager2_deck_play.currentItem = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }*/

        //왼쪽버튼
        button_bottom_left.setOnClickListener {
            viewpager2_deck_play.currentItem--
        }

        //오른쪽버튼
        button_bottom_right.setOnClickListener{
            viewpager2_deck_play.currentItem++
        }

        //플립
        button_bottom_flip.setOnClickListener {
            wordCardAdapter.flipCard(viewpager2_deck_play.currentItem)
        }

        //전체플립
        button_flip_all.setOnClickListener {
            if (wordCardAdapter.itemCount!=0){
                wordCardAdapter.flipAllCards()
            }
            isAllFlip = !isAllFlip
            if (isAllFlip){
                button_flip_all.setText(R.string.play_front)
            }
            else{
                button_flip_all.setText(R.string.play_back)
            }

        }

        //랜덤
        button_random.setOnClickListener {
            if (wordCardAdapter.itemCount != 0){
                var pos = viewpager2_deck_play.currentItem
                while(pos==viewpager2_deck_play.currentItem)
                    pos = (0 until wordCardAdapter.itemCount).random()
                viewpager2_deck_play.currentItem = pos
            }
        }

        //셔플
        button_shuffle.setOnClickListener {
            wordCardAdapter.suffleCards()
        }

    }
}