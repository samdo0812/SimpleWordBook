package com.sdstudio.simplewordbook.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.sdstudio.simplewordbook.R
import com.sdstudio.simplewordbook.activity.ImexportActivity
import com.sdstudio.simplewordbook.viewmodel.WordBookListViewModel
import com.sdstudio.simplewordbook.viewmodel.WordCardViewModel
import kotlinx.android.synthetic.main.fragment_setting_.*

class Setting_Frag(val wordBookListViewModel: WordBookListViewModel, val wordCardViewModel: WordCardViewModel) : Fragment() {

    /*override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        data_imexport.setOnClickListener {
            val intent = Intent(getActivity(), ImexportActivity::class.java)
            startActivity(intent)
        }



        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_empty, null, false)
        val dialogBuilder = AlertDialog.Builder(context)
        val dialog = dialogBuilder.setView(dialogView).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alldelete.setOnClickListener {
            AwesomeDialog.build(it.context as Activity)
                .position(AwesomeDialog.POSITIONS.CENTER)
                .title("WARING")
                .body("Are you sure you want to delete?")
                // .icon(R.drawable.ic_congrts)
                .onPositive("DELETE") {
                    wordBookListViewModel.deleteAll()
                    dialog.dismiss()

                }
                .onNegative("CANCEL") {
                    dialog.dismiss()
                }
        }



        feedback.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("sdstudioFAQ@gamil.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            startActivity(email)
        }

    }


}