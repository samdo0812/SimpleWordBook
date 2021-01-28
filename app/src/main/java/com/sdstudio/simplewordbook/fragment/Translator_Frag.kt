package com.sdstudio.simplewordbook.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.api.PapagoDTO
import com.sdstudio.simplewordbook.database.WordCard
import kotlinx.android.synthetic.main.dialog_translate.view.*
import kotlinx.android.synthetic.main.fragment_translator_.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.RuntimeException


class Translator_Frag : Fragment() {
    var inputword = input_word?.text?.toString()
    val JSON = MediaType.parse("application/json; charset=utf-8")
    val client = OkHttpClient()
    var url = "https://openapi.naver.com/v1/papago/n2mt"
    var json = JSONObject()
    lateinit var papagoDTO: PapagoDTO
    lateinit var source: String
    lateinit var target: String
    var flg: Int = 0
    lateinit var copyString: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translator_, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lateinit var outputresult: String


        val spinnerfront = spinner_front
        val spinnerback = spinner_back

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_country,
            R.layout.item_spinner
        )
        val adapter_tran_ko = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_ko,
            R.layout.item_spinner
        )
        val adapter_tran_country = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_country,
            R.layout.item_spinner
        )
        val adapter_en = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_en,
            R.layout.item_spinner
        )
        val adapter_ja = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_ja,
            R.layout.item_spinner
        )
        val adapter_zh_CN = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_zh_CN,
            R.layout.item_spinner
        )
        val adapter_zh_TW = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tran_zh_TW,
            R.layout.item_spinner
        )

        spinnerfront.adapter = adapter
        spinnerback.adapter = adapter_tran_country


        spinnerfront.setSelection(12)


        spinnerfront.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerback.adapter = adapter_tran_ko
                when(position){
                    0 ->{
                        source = "de"
                    }
                    1 ->{
                        source = "ru"
                    }
                    2 ->{
                        source = "vi"
                    }
                    3->{
                        source = "es"
                    }
                    4->{
                        spinnerback.adapter = adapter_en
                        source = "en"
                    }
                    5->{
                        source = "id"
                    }
                    6->{
                        source = "it"
                    }
                    7->{
                        spinnerback.adapter = adapter_ja
                        source = "ja"
                    }
                    8->{
                        spinnerback.adapter = adapter_zh_CN
                        source = "zh-CN"
                    }
                    9->{
                        spinnerback.adapter = adapter_zh_TW
                        source = "zh-TW"
                    }
                    10->{
                        source = "th"
                    }
                    11->{
                        source = "fr"
                    }
                    12->{
                        spinnerback.adapter = adapter_tran_country
                        source = "ko"
                    }
                }
            }
        }

        spinnerback.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (spinnerfront.selectedItemPosition == 4) {
                        when (position) {
                            0 -> {
                                target = "ja"
                            }
                            1 -> {
                                target = "zh-CN"
                            }
                            2 -> {
                                target = "zh-TW"
                            }
                            3 -> {
                                target = "fr"
                            }
                            4 -> {
                                target = "ko"
                            }
                        }
                    }
                    else if(spinnerfront.selectedItemPosition == 7){
                        when (position) {
                            0 -> {
                                target = "en"
                            }
                            1 -> {
                                target = "zh-CN"
                            }
                            2 -> {
                                target = "zh-TW"
                            }
                            3 -> {
                                target = "ko"
                            }
                        }
                    }
                    else if(spinnerfront.selectedItemPosition == 8){
                        when (position) {
                            0 -> {
                                target = "en"
                            }
                            1 -> {
                                target = "ja"
                            }
                            2 -> {
                                target = "zh-TW"
                            }
                            3 -> {
                                target = "ko"
                            }
                        }
                    }
                    else if(spinnerfront.selectedItemPosition == 9){
                        when (position) {
                            0 -> {
                                target = "en"
                            }
                            1 -> {
                                target = "ja"
                            }
                            2 -> {
                                target = "zh-CN"
                            }
                            3 -> {
                                target = "ko"
                            }
                        }
                    }
                    else if(spinnerfront.selectedItemPosition == 12){
                        when(position){
                            0 ->{
                                target = "de"
                            }
                            1 ->{
                                target = "ru"
                            }
                            2 ->{
                                target = "vi"
                            }
                            3->{
                                target = "es"
                            }
                            4->{
                                target = "en"
                            }
                            5->{
                                target = "id"
                            }
                            6->{
                                target = "it"
                            }
                            7->{
                                target = "ja"
                            }
                            8->{
                                target = "zh-CN"
                            }
                            9->{
                                target = "zh-TW"
                            }
                            10->{
                                target = "th"
                            }
                            11->{
                                target = "fr"
                            }
                            12->{
                                target = "ko"
                            }
                        }
                    }
                }
            }

        translate_btn.setOnClickListener {

            if (source == "ko" || source == "en" || source == "ja")
            {
                json.put("source", source)
                json.put("target", target)
                json.put("text", input_word?.text?.toString())

                val body = RequestBody.create(JSON, json.toString())
                val request = Request.Builder()
                    .header("KEY","KEYVALUE")
                    .addHeader("KEY", "KEYVALUE")
                    .url(url)
                    .post(body)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {

                        var str = response!!.body()!!.string()
                        papagoDTO = Gson().fromJson<PapagoDTO>(str, PapagoDTO::class.java)
                        outputresult = papagoDTO.message!!.result!!.translatedText.toString()
                        output_word.text = outputresult

                    }
                })
            }
            else
            {
                val mDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_translate, null)
                val mbuilder = AlertDialog.Builder(requireContext()).setView(mDialog)
                val myAlertDialog = mbuilder.show()

                mDialog.translate_ok.setOnClickListener {
                    myAlertDialog.dismiss()
                }
            }
        }


        //copy
        copy_text.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.action ==MotionEvent.ACTION_DOWN){
                    var copy_output = output_word.text.toString()
                    val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    var clipData = ClipData.newPlainText("outputword",copy_output)
                    clipboardManager.setPrimaryClip(clipData)

                    Toast.makeText(context,"복사 되었습니다.",Toast.LENGTH_SHORT).show();

                }
                return true
            }
        })



    }

}

