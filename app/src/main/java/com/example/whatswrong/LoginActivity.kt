package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatswrong.databinding.ActivityLoginBinding
import com.example.whatswrong.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    //view binding
    private var mBinding : ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    //FireBase & RealTimeBase connect
    private lateinit var mFirebaseAuth : FirebaseAuth // 파이어베이스 인증처리
    private lateinit var mDatabaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding Layout
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //login
        binding.btnLogin.setOnClickListener {
            mFirebaseAuth = FirebaseAuth.getInstance()
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Whatswrong")

            val myEmail = binding.etId.text.toString()
            val myPw  = binding.etPw.text.toString()

            mFirebaseAuth.signInWithEmailAndPassword(myEmail,myPw)
                .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val intent_main_cal = Intent(this, MainCalActivity::class.java)
                    Toast.makeText(this, "환영합니다.", Toast.LENGTH_SHORT).show()
                    startActivity(intent_main_cal)
                    finish()
                    
                }else {
                    Toast.makeText(this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }


        //regsiter
        binding.btnRegister.setOnClickListener {
            val intent_reg  = Intent(this, SignupActivity::class.java)
            startActivity(intent_reg)
        }
    }



    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

}
