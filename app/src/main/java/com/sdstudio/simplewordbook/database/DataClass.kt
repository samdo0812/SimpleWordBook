package com.sdstudio.simplewordbook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordbook_db")
data class WordBook(@PrimaryKey(autoGenerate = true) val id:Int,
                    @ColumnInfo var name:String,
                    @ColumnInfo var description:String,
                    @ColumnInfo var size:Int)

@Entity(tableName = "card_db")
data class Card(@PrimaryKey(autoGenerate = true) val id:Long,
                @ColumnInfo(name = "front") var front:String,
                @ColumnInfo(name = "back") var back:String,
                val wordBookId:Int)