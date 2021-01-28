package com.sdstudio.simplewordbook.database

import androidx.lifecycle.LiveData

class WordCardRepo(val wordCardDao: WordCardDAO, wordBookId:Int){

    val wordCardList: LiveData<List<WordCard>>

    init {
        if (wordBookId != -1){
            wordCardList = wordCardDao.getCardsFromBook(wordBookId)
        }
        else{
            wordCardList = wordCardDao.getAll()
        }
    }

    suspend fun insert(wordCard:WordCard){
        wordCardDao.insertWordCard(wordCard)
    }

    fun getCardFromBook(wordBookId: Int): LiveData<List<WordCard>>{
        return wordCardDao.getCardsFromBook(wordBookId)
    }

    suspend fun updateWordCard(wordCardId:Int, front:String, back:String){
        wordCardDao.updateCard(wordCardId,front,back)
    }

    fun selectWordCard(wordCardId: Int): LiveData<WordCard>{
        return wordCardDao.selectWordCardById(wordCardId)
    }

    suspend fun delete(wordCardId: Int){
        wordCardDao.deleteCardById(wordCardId)
    }
}