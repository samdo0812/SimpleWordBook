package com.sdstudio.simplewordbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.database.WordBookDatabase
import com.sdstudio.simplewordbook.database.WordBookRepo
import kotlinx.coroutines.launch

class WordBookListViewModel(application: Application):AndroidViewModel(application){

    private val repo:WordBookRepo
    val wordBookList:LiveData<List<WordBook>>
    var recentInsertedWordBookId:Long = 0

    init {
        repo = WordBookRepo(WordBookDatabase.getInstance(application)!!.wordBookDAO())
        wordBookList = repo.wordBookList
    }

    fun insert(wordBook: WordBook) = viewModelScope.launch{
        recentInsertedWordBookId = repo.insert(wordBook)
    }

    fun delete(wordBookId: Int) = viewModelScope.launch{
        repo.delete(wordBookId)
    }

    fun deleteAll() = viewModelScope.launch{
        repo.deleteAllWordBook()
    }

    fun countCards(wordBookId: Int):LiveData<Int> = repo.countCards(wordBookId)
}