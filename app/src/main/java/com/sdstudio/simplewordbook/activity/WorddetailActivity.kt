package com.sdstudio.simplewordbook.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_worddetail.*


class WorddetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worddetail)

        var wordBookId: Int = intent.getIntExtra("wordBookId", 0)
        var wordCardId = intent.getLongExtra("wordCardId",0)
        lateinit  var word: String
        lateinit var mean: String

       var wordCardViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
           override fun <T : ViewModel?> create(modelClass: Class<T>): T {
               return WordCardViewModel(application, wordBookId) as T
           }
       })
           .get(WordCardViewModel::class.java)




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
                   setResult(-1, intent)
                   finish()
               }
           }
       }

       if (wordCardId.toInt() > -1){
           Log.d("a","modify in")
           wordCardViewModel.selectWordCard(wordCardId.toInt()).observe(this, object:Observer<WordCard>{
               override fun onChanged(t: WordCard?) {
                   if(t!=null){
                       addword.setText(t.front)
                       addmean.setText(t.back)
                   }
                   wordCardViewModel.selectWordCard(wordCardId.toInt()).removeObserver(this)
               }
           })

           inputbutton.setOnClickListener {

                word = addword.text.toString()
                mean = addmean.text.toString()

               intent.putExtra("word",word)
               intent.putExtra("mean",mean)
               intent.putExtra("wordCardId",wordCardId)
               intent.putExtra("wordBookId",wordBookId)
               Log.d("test",wordCardId.toString())
               setResult(-1,intent)
               finish()
           }
       }

       addwordCancel.setOnClickListener{
           this.onBackPressed()
       }

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}