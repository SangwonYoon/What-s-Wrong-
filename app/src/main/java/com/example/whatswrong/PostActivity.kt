package com.example.whatswrong

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostActivity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val intent = intent

        val postTitle = intent.getStringExtra("title")
        val postContent = intent.getStringExtra("content")
        val postUser = intent.getStringExtra("user")
        val postTime = intent.getStringExtra("time")

        findViewById<TextView>(R.id.board_title).text = postTitle
        findViewById<TextView>(R.id.board_content).text = postContent
        findViewById<TextView>(R.id.board_user).text = postUser
        findViewById<TextView>(R.id.board_time).text = postTime
    }
}