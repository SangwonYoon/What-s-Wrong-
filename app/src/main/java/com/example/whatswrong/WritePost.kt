package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val mFirebaseAuth = FirebaseAuth.getInstance()
        val myUid = mFirebaseAuth.uid
        val nickRef = FirebaseDatabase.getInstance().getReference("Whatswrong")

        findViewById<Button>(R.id.post_cancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.post_complete).setOnClickListener {
            val postTitle = findViewById<EditText>(R.id.write_title).text.toString()
            val postContent = findViewById<EditText>(R.id.write_content).text.toString()
            var postUser : String = "dummy"
            val checkBox : CheckBox = findViewById(R.id.anony)

            val now = System.currentTimeMillis();
            Log.d("test", now.toString())
            val date = Date(now)
            val dateFormat = SimpleDateFormat("MM-dd hh:mm")
            val timezone = TimeZone.getTimeZone("Asia/Seoul")
            dateFormat.timeZone = timezone
            val postTime = dateFormat.format(date)

            Log.d("test", postTime)

            if(checkBox.isChecked){
                postUser = "익명"

                val post = Posts(postTitle, postContent, postUser, postTime,  now.toString())

                myRef.child(now.toString()).setValue(post)
            } else {
                nickRef.child("UserAccount").child(myUid!!).get().addOnSuccessListener {
                    val temp = it.getValue(UserAccount::class.java)
                    postUser = temp!!.strNickname.toString()
                    Log.d("nickname", postUser)

                    val post = Posts(postTitle, postContent, postUser, postTime, now.toString())

                    myRef.child(now.toString()).setValue(post)
                }
            }

            finish()
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