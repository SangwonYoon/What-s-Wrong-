package com.example.whatswrong

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class PostActivity  : AppCompatActivity(){

    private val commentList = arrayListOf<Comments>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val intent = intent

        val className = intent.getStringExtra("과목명")
        val postTitle = intent.getStringExtra("title")
        val postContent = intent.getStringExtra("content")
        val postUser = intent.getStringExtra("user")
        val postTime = intent.getStringExtra("time")
        val postKey = intent.getStringExtra("key")

        findViewById<TextView>(R.id.board_name).text = className
        findViewById<TextView>(R.id.board_title).text = postTitle
        findViewById<TextView>(R.id.board_content).text = postContent
        findViewById<TextView>(R.id.board_user).text = postUser
        findViewById<TextView>(R.id.board_time).text = postTime

        // 댓글 표시
        val rv_comment : RecyclerView = findViewById(R.id.comment)

        rv_comment.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        rv_comment.setHasFixedSize(true)

        val mAdapter = CommentAdapter(commentList)
        rv_comment.adapter = mAdapter

        // 데이터베이스에서 데이터 가져오기
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Whatswrong/Community").child(className.toString()).child(postKey.toString()).child("comment")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                commentList.clear()
                for(data in p0.children) {
                    val temp_comment = data.getValue(Comments::class.java) // Comments class에 기본 생성자가 있어야 함
                    val comment = Comments(temp_comment?.comment_user.toString(),temp_comment?.comment_content.toString(),temp_comment?.comment_time.toString())
                    commentList.add(comment)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@PostActivity,"인터넷 연결이 불안정합니다.",Toast.LENGTH_SHORT).show()
            }
        })

        // 댓글 쓰기 버튼 누르면 dialog 팝업 & 댓글 추가 기능
        val addComment : Button = findViewById(R.id.comment_button)
        addComment.setOnClickListener {
            val dialog = layoutInflater.inflate(R.layout.comment_dialog,null)
            val commentContent : EditText = dialog.findViewById(R.id.write_comment)
            val checkBox : CheckBox = dialog.findViewById(R.id.comment_anony)

            var commentUser : String

            val builder = AlertDialog.Builder(this)
            builder.setView(dialog)

            builder.setPositiveButton("완료", DialogInterface.OnClickListener { dialogInterface, i ->
                if(checkBox.isChecked){
                    commentUser = "익명"
                } else{
                    commentUser = "user9999" // 임시 테스트용
                }

                val now = System.currentTimeMillis();
                val date = Date(now)
                val dateFormat = SimpleDateFormat("MM-dd hh:mm")
                val timezone = TimeZone.getTimeZone("Asia/Seoul")
                dateFormat.timeZone = timezone
                val commentTime = dateFormat.format(date)

                val comment = Comments(commentUser, commentContent.text.toString(), commentTime)
                commentList.add(comment)
                myRef.setValue(commentList)
            })
            builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->  })
            builder.setCancelable(false)
            builder.show()
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