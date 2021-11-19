package com.example.whatswrong

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class WritePost  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_write)

        val intent = intent
        val className = intent.getStringExtra("과목명").toString()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Whatswrong/Community").child(className)

        findViewById<Button>(R.id.post_cancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.post_complete).setOnClickListener {
            val postTitle = findViewById<EditText>(R.id.write_title).text.toString()
            val postContent = findViewById<EditText>(R.id.write_content).text.toString()
            val postUser : String
            val checkBox : CheckBox = findViewById(R.id.anony)

            if(checkBox.isChecked){
                postUser = "익명"
            } else{
                postUser = "유저9999" // 임시 테스트용
            }

            val now = System.currentTimeMillis();
            Log.d("test", now.toString())
            val date = Date(now)
            val dateFormat = SimpleDateFormat("MM-dd hh:mm")
            val timezone = TimeZone.getTimeZone("Asia/Seoul")
            dateFormat.timeZone = timezone
            val postTime = dateFormat.format(date)

            Log.d("test", postTime)

            val post = Posts(postTitle, postContent, postUser, postTime,  now.toString())

            myRef.child(now.toString()).setValue(post)

            finish()
        }
    }
}