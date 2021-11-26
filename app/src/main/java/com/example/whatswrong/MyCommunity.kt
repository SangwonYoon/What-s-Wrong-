package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MyCommunity  : AppCompatActivity(){

    private val classes = arrayListOf<String>("모바일 프로그래밍", "객체지향 프로그래밍", "시스템 소프트웨어", "컴퓨터 구조", "논리와 소통", "응용 통계학") //임시 테스트용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_my)

        val classButtonSet : LinearLayout = findViewById(R.id.class_button_set)

        for(i : Int in 0 until classes.size){
            val classButton = Button(this)
            classButton.text = classes[i]
            classButton.setOnClickListener {
                val intent = Intent(this, ClassCommunity::class.java)
                intent.putExtra("과목명", classes[i])
                startActivity(intent)
            }
            classButton.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.button_bg))
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(20,0,20,30)
            classButton.layoutParams = layoutParams
            classButtonSet.addView(classButton)
        }

        findViewById<ImageButton>(R.id.scheduler_button).setOnClickListener {
            val intent = Intent(this, MainCalActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.community_button).setOnClickListener {
            val intent = Intent(this,MyCommunity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.user_button).setOnClickListener {
            val intent = Intent(this,MyInfoActivity::class.java)
            startActivity(intent)
        }
    }
}