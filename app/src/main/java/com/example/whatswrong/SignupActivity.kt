package com.example.whatswrong

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatswrong.databinding.ActivityMainBinding
import com.example.whatswrong.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    //view binding
    private var mBinding : ActivitySignupBinding? = null
    private val binding get() = mBinding!!

    //FireBase & RealTimeBase connect
    private lateinit var mFirebaseAuth : FirebaseAuth // 파이어베이스 인증처리
    private lateinit var mDatabaseRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding Layout
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Whatswrong")

        //회원가입 버튼 클릭시
        binding.btnSignup.setOnClickListener {
            // varible
            val strEmail : String = binding.etSignEmail.text.toString()
            val strPwd : String = binding.etSignPw.text.toString()
            val strName : String = binding.etSignName.text.toString()
            val strPhone : String = binding.etSignPhone.text.toString()
            val strAddr : String = binding.etSignAddress.text.toString()
            val strUniv : String = binding.etSignUniv.text.toString()
            val strNickname : String = binding.etSignNickname.text.toString()

            //Firebase Auth
            mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful) {
                        var mUser = mFirebaseAuth.currentUser
                        var account = UserAccount()

                        //account 객체 fill
                        account.idToken = mUser?.uid
                        account.strEmail = strEmail
                        account.strName = strName
                        account.strPhone = strPhone
                        account.strAddr = strAddr
                        account.strUniv = strUniv
                        account.strNickname = strNickname

                        mDatabaseRef.child("UserAccount").child(mUser!!.uid).setValue(account)

                        Toast.makeText(this, "succeed", Toast.LENGTH_SHORT).show()

                    }else {
                        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                    }

                }
        }





    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}