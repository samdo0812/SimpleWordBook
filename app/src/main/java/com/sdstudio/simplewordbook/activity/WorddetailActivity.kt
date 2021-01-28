package com.sdstudio.simplewordbook.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_worddetail.*


class WorddetailActivity : AppCompatActivity() {

   // lateinit var wordCardViewModel: WordCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worddetail)

        var wordBookId: Int = intent.getIntExtra("wordBookId", 0)
        var wordCardId = intent.getLongExtra("wordCardId",0)


       //단어 추가
       if (wordCardId.toInt() == 0 && wordBookId != 0) {
           inputbutton.setOnClickListener {
               var word = addword.text.toString()
               var mean = addmean.text.toString()
               if (addword != null && addmean != null) {
                   var intent = Intent(applicationContext, WordListActivity::class.java)
                   var data: String

                   intent.putExtra("word", word)
                   intent.putExtra("mean", mean)
                   intent.putExtra("wordBookId", wordBookId)
                   setResult(100, intent)
                   finish()
               }
           }
       }

       if (wordCardId > -1){
           Log.d("test","I'm here")
           inputbutton.setOnClickListener {
               var word = addword.text.toString()
               var mean = addmean.text.toString()

               intent.putExtra("word",word)
               intent.putExtra("mean",mean)
               intent.putExtra("wordCardId",wordCardId)
               intent.putExtra("wordBookId",wordBookId)
               Log.d("test",wordCardId.toString())
               setResult(101,intent)
               finish()
           }
       }

    }
}