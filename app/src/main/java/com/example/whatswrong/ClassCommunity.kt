package com.example.whatswrong

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ClassCommunity  : AppCompatActivity(){

    private val postList = ArrayList<Posts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_class)

        // recycler view adapter 세팅
        val rv_post : RecyclerView = findViewById(R.id.post)

        rv_post.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        rv_post.setHasFixedSize(true)

        val mAdapter = PostAdapter(postList)
        rv_post.adapter = mAdapter

        // 과목명 정보 받아오기 & 데이터 베이스에서 post 데이터 가져와서 postList에 넣기
        //val intent = intent
        //val className = intent.getStringExtra("과목명") // 과목명 받아오기
        val className = "모바일 프로그래밍" // 임시 테스트용
        findViewById<TextView>(R.id.class_name).text = className

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Whatswrong/Community").child(className)

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children) {
                    val temp_post = data.getValue(Posts::class.java) // Posts class에 기본 생성자가 있어야 함
                    val post = Posts(temp_post?.title.toString(),temp_post?.content.toString(),temp_post?.user.toString(),temp_post?.time.toString())
                    postList.add(post)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        //val postList = arrayListOf(
            //Posts("개인 과제 다 하신 분?", "hello", "익명", "12:00"),
            //Posts("안녕하세요!", "hi!!!", "익명", "13:00"),
            //Posts("반갑습니다~~", "처음뵙겠습니다!", "익명", "14:00")
        //)

        // 새 글 쓰기 버튼 onClick 이벤트 처리
        val postButton : ImageView = findViewById(R.id.post_button)
        postButton.setOnClickListener{
            Log.d("test", "버튼 눌림")

            // intent에서 넘어온 데이터에서 title, content, user, time을 추출

            val post = Posts(
            "안녕하세요!!",
            "hi",
            "익명",
                "12:30"
            )

            myRef.push().setValue(post)
        }
    }
}