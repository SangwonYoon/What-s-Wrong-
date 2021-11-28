package com.example.whatswrong

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

class ChgNicNameDialog(context: Context) {
  private val diaglog = Dialog(context)


  fun myDialog(){
    diaglog.setContentView(R.layout.chgnicname_dialog)

    //dialog setting
    diaglog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
    diaglog.setCancelable(true)

    val nickname = diaglog.findViewById<EditText>(R.id.input_nickname)
    val btnDone = diaglog.findViewById<Button>(R.id.btn_correct)
    val btnCancel = diaglog.findViewById<Button>(R.id.btn_cancel)


    btnDone.setOnClickListener {
      if(nickname.text.toString().isNotBlank()){
        onClickedListener.onClicked(nickname.text.toString())
        diaglog.dismiss()
      }
    }

    btnCancel.setOnClickListener {
      diaglog.dismiss()
    }


    diaglog.show()
  }

  interface ButtonClickListener {
    fun onClicked(nickname: String)
  }

  private lateinit var onClickedListener: ButtonClickListener

  fun setOnClickedListener(listener: ButtonClickListener) {
    onClickedListener = listener
  }

}

