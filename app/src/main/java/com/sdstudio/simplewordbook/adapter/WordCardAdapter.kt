package com.sdstudio.simplewordbook.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.*
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.activity.WordListActivity
import com.sdstudio.simplewordbook.activity.WorddetailActivity
import com.sdstudio.simplewordbook.database.WordCard
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.item_wordcard.*
import kotlinx.android.synthetic.main.item_wordcard.view.*
import kotlinx.android.synthetic.main.item_wordplay.view.*
import java.lang.Exception
import java.security.acl.Owner
import java.util.*


//class WordCardAdapter(private val type: Int, private val update:(Boolean,Int,String,String)->Unit, private val delete:(Boolean,Int)->Unit) :
//    RecyclerView.Adapter<WordCardAdapter.ViewHolder>() {

class WordCardAdapter(private val type: Int) :
    RecyclerView.Adapter<WordCardAdapter.ViewHolder>() {

    companion object {
        var viewData: List<WordCard> = listOf()
        lateinit var tts: TextToSpeech
    }

    var flipSet: BitSet = BitSet()  //BitSet Boolean보다 적은 메모리 차지
    var isAllFlip: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = when (type) {
            //0 단어카드
            //1 플레이 카드
            0 -> LayoutInflater.from(parent.context).inflate(R.layout.item_wordcard, parent, false)
            1 -> LayoutInflater.from(parent.context).inflate(R.layout.item_wordplay, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wordplay, parent, false)
        }

        val viewHolder = ViewHolder(v, type)
        val curView = viewHolder.cardView   //v.cardview_card
        val curText = viewHolder.textView
        val curTts = viewHolder.ttsBtn

        when (type) {
            //터치시 단어 앞면 뒷면 회전
            0 -> {
                curView.setOnClickListener {
                    flipAnimation(curView)
                    flipSet[viewHolder.adapterPosition] = !flipSet[viewHolder.adapterPosition]
                    if (flipSet[viewHolder.adapterPosition]) {
                        curText.text = viewData[viewHolder.adapterPosition].back
                    } else {
                        curText.text = viewData[viewHolder.adapterPosition].front
                    }
                }
                curTts.setOnClickListener {
                    Log.d("hi","tts")
                    val tospeak = curText.text.toString()
                    tts.speak(tospeak, TextToSpeech.QUEUE_FLUSH,null)
                }

            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return viewData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (type == 0)
            holder.itemView.startAnimation(AlphaAnimation(0f, 1f).apply {
                duration = 300
            })

        if (flipSet[position]) { //flipSet[0] = true 아니면 false
            holder.textView.text = viewData[holder.adapterPosition].back
        } else {
            holder.textView.text = viewData[holder.adapterPosition].front
            //holder.cardNumber.text = viewData[holder.adapterPosition].id.toString()
        }
    }


    class ViewHolder(v: View, type: Int) : RecyclerView.ViewHolder(v),
        View.OnCreateContextMenuListener {
        lateinit var textView: TextView
        lateinit var cardView: CardView
        lateinit var ttsBtn:ImageButton


        companion object {
            var wordCardId: Long = 0
        }

        init {
            when (type) {
                0 -> {  //카드 등록
                    textView = v.textview_card_text
                    cardView = v.cardview_card
                    ttsBtn = v.tts
                    cardView.setOnCreateContextMenuListener(this)

                }
                1 -> {  //카드 플레이
                    textView = v.textview_play_card_text
                    cardView = v.cardview_play_card
                    ttsBtn = v.tts_no
                }
            }
            tts = TextToSpeech(v.context, TextToSpeech.OnInitListener {
                if (it != TextToSpeech.ERROR) {
                    tts.language = Locale.UK
                }
            })
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {

            menu?.add(this.adapterPosition, 101, 0, "Modify")
            menu?.add(this.adapterPosition, 102, 1, "Delete")
            wordCardId = viewData[adapterPosition].id
        }
    }


    fun setCards(wordCardList: List<WordCard>) {
        viewData = wordCardList
        flipSet = BitSet(viewData.size)
        notifyDataSetChanged()
    }

    fun flipCard(position: Int) {
        if (viewData.isNotEmpty()) {
            flipSet.flip(position)
            notifyItemChanged(position)
        }
    }

    fun flipAllCards() {
        if (viewData.isNotEmpty()) {
            isAllFlip = !isAllFlip
            if (isAllFlip)
                flipSet.set(0, flipSet.size() - 1, true)
            else
                flipSet.set(0, flipSet.size() - 1, false)
            notifyItemRangeChanged(0, viewData.size)
        }
    }

    fun flipAnimation(cardView: CardView) {
        cardView.startAnimation(AlphaAnimation(0f, 1f).apply { duration = 300 })
    }

    fun suffleCards() {

        val dataArray = mutableListOf<WordCard>()
        viewData.forEach {
            dataArray.add(it)
        }
        var piv = 1
        for (i in 0 until viewData.size - 1) {
            var rand = (piv until viewData.size).random()
            dataArray[i] = dataArray[rand].also {
                dataArray[rand] = dataArray[i]
            }
            flipSet[i] = flipSet[rand].also { flipSet[rand] = flipSet[i] }
            piv++
        }
        viewData = dataArray.toList()
        notifyItemRangeChanged(0, viewData.size)

    }

    fun cardRemove(position: Int) {
        var viewData2: MutableList<WordCard> = viewData as MutableList<WordCard>
        viewData2.removeAt(position)
        notifyItemRemoved(position)
    }

}