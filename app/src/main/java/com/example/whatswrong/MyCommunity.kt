package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MyCommunity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_my)

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