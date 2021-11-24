package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ClassCommunity  : AppCompatActivity(){

    private val postList = ArrayList<Posts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_class)

        // 과목명 정보 받아오기 & 데이터 베이스에서 post 데이터 가져와서 postList에 넣기
        //val intent = intent
        //val className = intent.getStringExtra("과목명") // 과목명 받아오기
        val className = "모바일 프로그래밍" // 임시 테스트용
        findViewById<TextView>(R.id.class_name).text = className

        // recycler view adapter 세팅
        val rv_post : RecyclerView = findViewById(R.id.post)

        rv_post.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        rv_post.setHasFixedSize(true)

        val mAdapter = PostAdapter(this, postList, className)
        rv_post.adapter = mAdapter

        //
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Whatswrong/Community").child(className)

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                postList.clear()
                for(data in p0.children) {
                    val temp_post = data.getValue(Posts::class.java) // Posts class에 기본 생성자가 있어야 함
                    val post = Posts(temp_post?.title.toString(),temp_post?.content.toString(),temp_post?.user.toString(),temp_post?.time.toString(),temp_post?.key.toString())
                    postList.add(post)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ClassCommunity,"인터넷 연결이 불안정합니다.",Toast.LENGTH_SHORT).show()
            }
        })

        // 새 글 쓰기 버튼 onClick 이벤트 처리
        val postButton : ImageView = findViewById(R.id.post_button)
        postButton.setOnClickListener{
            Log.d("test", "버튼 눌림")

            val intent = Intent(this@ClassCommunity, WritePost::class.java)
            intent.putExtra("과목명", className)
            startActivity(intent)
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