package com.example.whatswrong

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class ClassCommunity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_class)

        val postButton : ImageView = findViewById(R.id.post_button)
        postButton.setOnClickListener{
            Log.d("test", "버튼 눌림")
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Whatswrong/Community/모바일 프로그래밍")

            val post = HashMap<String, String>()
            post["title"] = "개인 과제 다 하신 분?"
            post["content"] = "hello"
            post["time"] = "12:00"
            post["user"] = "익명"

            myRef.push().setValue(post)
        }
    }
}