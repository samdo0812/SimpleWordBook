package com.sdstudio.simplewordbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(WordBook::class, WordCard::class),version = 1)
abstract class WordBookDatabase:RoomDatabase(){
    abstract fun wordBookDAO(): WordBookDAO
    abstract fun wordCardDAO(): WordCardDAO

    companion object{

        var INSTANCE : WordBookDatabase? = null

        fun getInstance(context: Context): WordBookDatabase?{
            if (INSTANCE == null){
                synchronized(WordBookDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    WordBookDatabase::class.java,"wordbook_db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}