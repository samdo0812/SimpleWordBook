package com.sdstudio.simplewordbook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordBookDAO{
    @Query("SELECT * FROM wordbook_db")
    fun getAll(): LiveData<List<WordBook>>

    @Query("SELECT * FROM wordbook_db WHERE id = :wordBookId")
    fun selectWordBookByID(wordBookId:Int):WordBook

    @Query("SELECT COUNT(*) FROM wordcard_db WHERE wordBookId = :wordBookId")
    fun countCards(wordBookId: Int):LiveData<Int>

    @Query("DELETE FROM wordbook_db WHERE id = :wordBookId")
    suspend fun deleteWordBookById(wordBookId: Int)

    @Query("DELETE FROM wordbook_db")
    suspend fun deleteAllWordBook()
    @Insert
    suspend fun insertWordBook(wordBook: WordBook):Long
}