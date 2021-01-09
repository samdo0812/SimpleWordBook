package com.sdstudio.simplewordbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.database.WordCard
import kotlinx.android.synthetic.main.item_wordcard.view.*
import java.util.*
import kotlin.math.log


class WordCardAdapter(private val seekBar:SeekBar?, private val type:Int):RecyclerView.Adapter<WordCardAdapter.ViewHolder>(){

    var viewData:List<WordCard> = listOf()
    var flipSet: BitSet = BitSet()  //BitSet Boolean보다 적은 메모리 차지
    var isAllFlip:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = when(type){
            //0 단어카드
            //1 플레이 카드
            0-> LayoutInflater.from(parent.context).inflate(R.layout.item_wordcard,parent,false)
            else ->  LayoutInflater.from(parent.context).inflate(R.layout.dialog_word_add,parent,false) //임시, 수정필요
        }

        val viewHolder = ViewHolder(v,type)
        val curView = viewHolder.cardView   //v.cardview_card
        val curText = viewHolder.textView

        curView.setOnClickListener {
            flipAnimation(curView)
            flipSet[viewHolder.adapterPosition] = !flipSet[viewHolder.adapterPosition]
            if (flipSet[viewHolder.adapterPosition]){
                curText.text = viewData[viewHolder.adapterPosition].back
            }
            else{
                curText.text = viewData[viewHolder.adapterPosition].front
            }

        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return viewData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(type==0)
            holder.itemView.startAnimation(AlphaAnimation(0f,1f).apply{
            duration=300
        })

        Log.d("test",flipSet[position].toString())
        //Log.d("test", position.toString())
        if (flipSet[position]){ //flipSet[0] = true 아니면 false
            holder.textView.text = viewData[holder.adapterPosition].back
            //flipSet[position] = true
        }
        else {
            holder.textView.text = viewData[holder.adapterPosition].front
            //flipSet[position] = false
        }

    }

    class ViewHolder(v: View, type: Int): RecyclerView.ViewHolder(v){
        lateinit var textView: TextView
        lateinit var cardView: CardView

        init {
            when(type) {
                0 -> {  //카드 등록
                    textView = v.textview_card_text
                    cardView = v.cardview_card
                }
                1 -> {  //카드 플레

                }
            }
        }
    }

    fun setCards(wordCardList:List<WordCard>){
        viewData = wordCardList
        flipSet = BitSet(viewData.size)
        notifyDataSetChanged()
    }

    fun flipCard(position: Int){
        if(viewData.isNotEmpty()) {
            flipSet.flip(position)
            notifyItemChanged(position)
        }
    }

    fun flipAllCards(){
        if(viewData.isNotEmpty()) {
            isAllFlip = !isAllFlip
            if (isAllFlip)
                flipSet.set(0, flipSet.size() - 1, true)
            else
                flipSet.set(0, flipSet.size() - 1, false)
            notifyItemRangeChanged(0,viewData.size)
        }
    }

    fun flipAnimation(cardView: CardView){
        cardView.startAnimation(AlphaAnimation(0f,1f).apply { duration=300 })
    }

}
