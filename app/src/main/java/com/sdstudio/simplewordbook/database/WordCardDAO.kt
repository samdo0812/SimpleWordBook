package com.sdstudio.simplewordbook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordCardDAO{
    @Query("SELECT *FROM wordcard_db")
    fun getAll():LiveData<List<WordCard>>

    @Query("SELECT *FROM wordcard_db WHERE id = :wordcardId")
    fun selectWordCardById(wordcardId:Int): LiveData<WordCard>

    @Query("SELECT *FROM wordcard_db WHERE wordBookId = :wordBookId")
    fun getCardsFromBook(wordBookId:Int):LiveData<List<WordCard>>

    @Query("DELETE FROM wordcard_db WHERE id = :wordcardId")
    suspend fun deleteCardById(wordcardId: Int)

    @Query("UPDATE wordcard_db SET front = :front, back = :back WHERE id = :id")
    suspend fun updateCard(id:Int, front:String, back:String)

    @Insert
    suspend fun insertWordCard(wordcard:WordCard)
}