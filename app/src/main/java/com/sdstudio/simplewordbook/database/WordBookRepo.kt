package com.sdstudio.simplewordbook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WordBookRepo(val wordBookDAO: WordBookDAO){

    val wordBookList: LiveData<List<WordBook>> = wordBookDAO.getAll()
    //var recentInsertedWordBookId:MutaleLiveData<Long> = MutableLiveData()

    fun countCards(wordBookId:Int): LiveData<Int> = wordBookDAO.countCards(wordBookId)

    suspend fun insert(wordBook: WordBook):Long{
        return wordBookDAO.insertWordBook(wordBook)
    }

    suspend fun delete(wordBookId: Int){
        wordBookDAO.deleteWordBookById(wordBookId)
    }

    suspend fun deleteAllWordBook(){
        wordBookDAO.deleteAllWordBook()
    }
}