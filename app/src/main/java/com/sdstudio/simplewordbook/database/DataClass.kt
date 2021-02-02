package com.sdstudio.simplewordbook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "wordbook_db")
data class WordBook(@PrimaryKey(autoGenerate = true) val id:Int,
                    @ColumnInfo var name:String,
                    @ColumnInfo var description:String,
                    @ColumnInfo var size:Int)

@Entity(tableName = "wordcard_db",
    foreignKeys = arrayOf(ForeignKey(entity = WordBook::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("wordBookId"),
        onDelete = CASCADE)
    )
)

data class WordCard(@PrimaryKey(autoGenerate = true) val id:Long,
                @ColumnInfo(name = "front") var front:String,
                @ColumnInfo(name = "back") var back:String,
                val wordBookId:Int)

data class background(val image:Int){

}