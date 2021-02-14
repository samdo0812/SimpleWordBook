package com.sdstudio.simplewordbook.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.adapter.ExtractListAdapter
import com.sdstudio.simplewordbook.database.WordBook
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.activity_imexport.*
import kotlinx.android.synthetic.main.activity_imexport.*
import kotlinx.android.synthetic.main.dialog_new_wordbooklist.view.*
import kotlinx.android.synthetic.main.dialog_new_wordbooklist.view.button_cancle
import kotlinx.android.synthetic.main.dialog_share.view.*
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class ImexportActivity() : AppCompatActivity() {

    var WordBookList: List<WordBook> = listOf()
    val cardList = mutableListOf<WordCard>()
    var selectedWordBookId: Int = -1

    var cardListFromWordBook = listOf<WordCard>()
    private lateinit var wordBookListViewModel: WordBookListViewModel
    private lateinit var wordCardViewModel: WordCardViewModel
    lateinit var extractListAdapter: ExtractListAdapter

    lateinit var writeUri: Uri
    lateinit var wordbookUri: Uri

    var currentSelectedEncoding: String = "UTF8"
    var delimiterForWrite: String = " "

    var isTypeSeleted: Boolean = false
    var isFileSelected: Boolean = false
    var isWordBookSelected: Boolean = false

    var chooseType: Int = 0


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            data?.data?.also {
                wordbookUri = it
                isFileSelected = true
                //showFileName(it)
            }
        } else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            data?.data?.also {
                writeUri = it
                //isWriteUriSet = true
                writeFile()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imexport)

        setSupportActionBar(toolbar_imexport)
        supportActionBar?.title = "IMPORT / EXPORT"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_left)

        wordBookListViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(WordBookListViewModel::class.java)
        wordCardViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WordCardViewModel(application, -1) as T
            }
        }).get(WordCardViewModel::class.java)
        wordBookListViewModel.wordBookList.observe(this, androidx.lifecycle.Observer {
            WordBookList = it
        })

        button_clear.setOnClickListener {
            clearAll()
        }

        //단어장선택
        button_select_wordbook.setOnClickListener {
            if (isTypeSeleted) {
                if (chooseType == 0) {
                    openCardListFile()
                } else if (chooseType == 1) {
                    openWordBookList()
                }
            } else {
                val dialogBuilder =
                    AlertDialog.Builder(this).setItems(R.array.choose,
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                0 -> {
                                    isTypeSeleted = true
                                    chooseType = 0
                                    openCardListFile()
                                    button_imexport.text = "IMPORT"
                                    button_select_wordbook.text = "Select File"
                                }
                                1 -> {
                                    isTypeSeleted = true
                                    chooseType = 1
                                    openWordBookList()
                                    button_imexport.text = "EXPORT"
                                    button_select_wordbook.text = "Select WordBook"
                                }
                            }
                        }).show()
            }


        }

        //추출버튼
        button_extract.setOnClickListener {
            val cardListSet: List<WordCard>
            val str: String
            val delimiter: Char

            //import
            if (isFileSelected && chooseType == 0) {
                delimiter = '\n'
                str = getStringFromUri(wordbookUri)
                cardListSet = getCardListFromString(str, delimiter)
                extractListAdapter.setExtractList(cardListSet)
            }
            //export
            else if (isWordBookSelected) {
                cardListFromWordBook.forEach {
                    cardList.add(it)
                }

                cardListSet = cardList
                extractListAdapter.setExtractList(cardListSet)
            }
        }


        //맨밑버튼
        button_imexport.setOnClickListener {
            if (isTypeSeleted == true) {
                if (chooseType == 0) {
                    val mDialog =
                        LayoutInflater.from(this).inflate(R.layout.dialog_new_wordbooklist, null)
                    val mbuilder = AlertDialog.Builder(this).setView(mDialog)
                    val myAlertDialog = mbuilder.show()
                    myAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    mDialog.button_add.setOnClickListener {
                        val wordBook = WordBook(
                            0,
                            mDialog.edittext_new_name.text.toString(),
                            mDialog.edittext_new_desc.text.toString(), 0
                        )

                        var wordBookId: Long = 0
                        wordBookListViewModel.insert(wordBook).invokeOnCompletion {
                            wordBookId = wordBookListViewModel.recentInsertedWordBookId
                            cardList.forEach {
                                wordCardViewModel.insert(
                                    WordCard(
                                        0,
                                        it.front,
                                        it.back,
                                        wordBookId.toInt()
                                    )
                                )
                            }
                        }

                        myAlertDialog.dismiss()
                    }

                    mDialog.button_cancle.setOnClickListener {
                        myAlertDialog.dismiss()
                    }

                } else if (chooseType == 1) {
                    delimiterForWrite = "\n"
                    createOutputFile()
                }
            }
        }


        extractListAdapter = ExtractListAdapter()
        recyclerview_imexport_text.apply {
            setHasFixedSize(true)
            adapter = extractListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }


    //import
    fun openWordBookList() {
        val deckNameList = mutableListOf<String>()
        WordBookList.forEach {
            deckNameList.add(it.name)
        }
        if (deckNameList.isEmpty()) {
            Toast.makeText(this, "Wordbook is empty!", Toast.LENGTH_SHORT).show()
            return
        }
        val dialogBuilder =
            AlertDialog.Builder(this).setItems(
                deckNameList.toTypedArray()
            ) { dialog, which ->
                button_select_wordbook.text = WordBookList[which].name
                selectedWordBookId = WordBookList[which].id
                isWordBookSelected = true
                wordCardViewModel.getCardFromBook(selectedWordBookId)
                    .observe(this, androidx.lifecycle.Observer {
                        cardListFromWordBook = it
                    })

            }.show()
    }

    fun openCardListFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent, 3)
    }

    fun getStringFromUri(uri: Uri): String {
        val strBuilder = StringBuilder()
        contentResolver?.openInputStream(uri).use {
            BufferedReader(InputStreamReader(it, currentSelectedEncoding)).use { bufferedReader ->
                var currentLine = bufferedReader.readLine()
                while (currentLine != null) {
                    strBuilder.append(currentLine)
                    strBuilder.append('\n')
                    currentLine = bufferedReader.readLine()
                }
                if (strBuilder.isNotEmpty())
                    strBuilder.deleteCharAt(strBuilder.length - 1)
            }
        }
        return strBuilder.toString()
    }

    fun getCardListFromString(str: String, delimiter: Char): List<WordCard> {
        var isFront: Boolean = true
        var front: String? = null
        var back: String? = null

        var currentWord = StringBuilder()
        str.forEach {
            if (it == delimiter) {
                if (isFront) {
                    isFront = !isFront
                    front = currentWord.toString()
                } else {
                    isFront = !isFront
                    back = currentWord.toString()
                    cardList.add(WordCard(0, front ?: "", back ?: "", 0))
                    front = null
                    back = null
                }
                currentWord.clear()
            } else
                currentWord.append(it)
        }
        if (currentWord.isNotEmpty())
            if (front != null)
                back = currentWord.toString()
            else
                front = currentWord.toString()

        cardList.add(WordCard(0, front ?: "", back ?: "", 0))


        return cardList
    }


    //export
    fun createOutputFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent, 4)
    }


    fun writeFile() {
        contentResolver?.openFileDescriptor(writeUri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use { file ->
                cardList.forEach {
                    file.write(it.front.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(delimiterForWrite.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(it.back.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(delimiterForWrite.toByteArray(Charset.forName(currentSelectedEncoding)))
                }
            }
        }
        Toast.makeText(this, "Export Success!", Toast.LENGTH_SHORT).show()


        //share
        val mDialog = LayoutInflater.from(this).inflate(R.layout.dialog_share, null)
        val mbuilder = AlertDialog.Builder(this).setView(mDialog)
        val myAlertDialog = mbuilder.show()

        mDialog.button_share.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, writeUri)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "SimpleWordBook_data"))
            myAlertDialog.dismiss()
        }
        mDialog.button_cancle.setOnClickListener {
            myAlertDialog.dismiss()
        }
    }


    fun clearAll() {
        cardList.clear()
        extractListAdapter.setExtractList(cardList)
        isTypeSeleted = false
        isWordBookSelected = false
        isFileSelected = false
    }

}

