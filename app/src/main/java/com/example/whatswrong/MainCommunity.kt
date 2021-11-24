package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainCommunity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) { // 사용 X
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_main)

        val myCommunityButton : Button = findViewById(R.id.my_community)
        myCommunityButton.setOnClickListener {
            val intent = Intent(this,MyCommunity::class.java)
            startActivity(intent)
        }
    }
}