package com.sdstudio.simplewordbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_word_list.*
import java.util.*

class WordListActivity : AppCompatActivity() {

    //private var
    var isEditCard:Boolean = false
    var selectedEditCard:Int = -1

    private lateinit var wordCardViewModel: WordCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        fab_word.setOnClickListener {
            openAddCard(false,0)
        }
    }

    fun openAddCard(isEditCard:Boolean, wordCardId:Int = 0){
        this.isEditCard = isEditCard
        selectedEditCard = wordCardId

        if (this.isEditCard){
          
        }

    }
}