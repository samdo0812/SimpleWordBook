package com.sdstudio.simplewordbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sdstudio.simplewordbook.database.WordBookDatabase
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.database.WordCardRepo
import kotlinx.coroutines.launch

class WordCardViewModel(application: Application, wordBookId:Int):AndroidViewModel(application){

    private val repo: WordCardRepo
    val cardList: LiveData<List<WordCard>>

    init {
        repo = WordCardRepo(WordBookDatabase.getInstance(application)!!.wordCardDAO(),wordBookId)
        cardList = repo.wordCardList
    }

    fun getCardFromBook(wordBookId: Int):LiveData<List<WordCard>>{
        return repo.getCardFromBook(wordBookId)
    }

    fun selectWordCard(wordCardId:Int):LiveData<WordCard>{
        return repo.selectWordCard(wordCardId)
    }

    fun insert(wordCard: WordCard) = viewModelScope.launch {
        repo.insert(wordCard)
    }

    fun delete(wordCardId: Int) = viewModelScope.launch {
        repo.delete(wordCardId)
    }
}