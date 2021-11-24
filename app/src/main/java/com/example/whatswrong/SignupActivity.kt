package com.example.whatswrong

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.whatswrong.databinding.ActivityMainBinding
import com.example.whatswrong.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

  //view binding
  private var mBinding : ActivitySignupBinding? = null
  private val binding get() = mBinding!!

  //FireBase & RealTimeBase connect
  private lateinit var mFirebaseAuth : FirebaseAuth // 파이어베이스 인증처리
  private lateinit var mDatabaseRef : DatabaseReference
  private lateinit var emailRef : DatabaseReference



  var isvaildPassword = false
  var isvaildID = false //아이디중복검사 처리 미구현.

  //정규식 (숫자,문자,특수문자중, 2가지 포함 6~12)
  fun isPasswordFormat(password: String): Boolean {
    return password.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{6,12}\$".toRegex())
  }

  fun isPhoneNumberFormat(phoneNumber : String) : Boolean {
    return phoneNumber.matches("^01([0|1|6|7|8|9]?)-([0-9]{3,4})-([0-9]{4})\$".toRegex())
  }

  override fun onRestart() {
    super.onRestart()
    isvaildID = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //binding Layout
    mBinding = ActivitySignupBinding.inflate(layoutInflater)
    setContentView(binding.root)

    mFirebaseAuth = FirebaseAuth.getInstance()
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Whatswrong")


    //PW check
    binding.etSignPw.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.tvPwMessage.text = "0 / 12"
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var counter = binding.etSignPw.text.toString()
        binding.tvPwMessage.text =  counter.length.toString() +  " / 12"

        if( 6<= counter.length && counter.length <=12) {
          isvaildPassword = false
          binding.tvPwMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
        } else {
          isvaildPassword = false
          binding.tvPwMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
        }
      }

      override fun afterTextChanged(p0: Editable?) {
        var counter = binding.etSignPw.text.toString()
        binding.tvPwMessage.text =  counter.length.toString() +  " / 12"
        if( 6<= counter.length && counter.length <=12) {
          if(isPasswordFormat(binding.etSignPw.text.toString())){
            isvaildPassword = true
            binding.tvPwMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.green))
          } else {
            isvaildPassword = false
            binding.tvPwMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
          }
        } else {
          isvaildPassword = false
          binding.tvPwMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
        }
      }
    })

    binding.etSignPhone.addTextChangedListener(object : TextWatcher{
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.tvPhNumber.text = "'-' 포함하여 입력해주세요"
        binding.tvPhNumber.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.black))
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var myPhone = binding.etSignPhone.text.toString()
        if(isPhoneNumberFormat(myPhone)){
          binding.tvPhNumber.text = "유효한 입력값."
          binding.tvPhNumber.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.green))
        }else {
          binding.tvPhNumber.text = "형식에 맞게 입력해주세요"
          binding.tvPhNumber.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
        }
      }

      override fun afterTextChanged(p0: Editable?) {
        var myPhone = binding.etSignPhone.text.toString()
        if(isPhoneNumberFormat(myPhone)){
          binding.tvPhNumber.text = "유효한 입력값."
          binding.tvPhNumber.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.green))
        }else {
          binding.tvPhNumber.text = "형식에 맞게 입력해주세요"
          binding.tvPhNumber.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
        }
      }

    })




    //아이디중복검사
    emailRef = FirebaseDatabase.getInstance().getReference("Whatswrong/Email-List")
    binding.btnCheckId.setOnClickListener {
      val myEmail : String = binding.etSignEmail.text.toString()
      emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
          TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
          for (item in snapshot.children){
            val temp = item.getValue(Email::class.java)
            val getEmail = temp?.Email
            Log.e("ITEM :: ",getEmail.toString())
            if(getEmail.toString().equals(myEmail)){
              isvaildID = false
              break
            }else {
              isvaildID = true
            }
          }
          if(isvaildID && myEmail.isNotEmpty()){
            binding.tvIdMessage.text = "사용가능한 이메일입니다."
            binding.tvIdMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.green))

          }else if(myEmail.isEmpty()){
            binding.tvIdMessage.text = "이메일을 입력해주세요."
            binding.tvIdMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
          }else if(!snapshot.exists()){
            isvaildID = true

          }else {
            binding.tvIdMessage.text = "이미 존재하는 이메일입니다."
            binding.tvIdMessage.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.red))
          }
        }



      })


    }



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


      if(strEmail.isNotEmpty() && strPwd.isNotEmpty() && strName.isNotEmpty() && strPhone.isNotEmpty()
        && strAddr.isNotEmpty() && strUniv.isNotEmpty() && strNickname.isNotEmpty()){
        if(binding.radioAccept.isChecked){
          if (isPhoneNumberFormat(binding.etSignPhone.text.toString())){
            if(isvaildPassword){
              if(isvaildID){
                //Firebase Auth
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd)
                  .addOnCompleteListener(this){task ->
                    if(task.isSuccessful) {
                      var mUser = mFirebaseAuth.currentUser
                      var account = UserAccount()
                      var myEmail = Email()

                      //myEmail 객체 fill.
                      myEmail.Email = strEmail

                      //account 객체 fill
                      account.idToken = mUser?.uid
                      account.strEmail = strEmail
                      account.strName = strName
                      account.strPhone = strPhone
                      account.strAddr = strAddr
                      account.strUniv = strUniv
                      account.strNickname = strNickname

                      mDatabaseRef.child("UserAccount").child(mUser!!.uid).setValue(account)
//                      mDatabaseRef.child("Email-List").push().setValue(myEmail)
                      mDatabaseRef.child("Email-List").child(mUser!!.uid).setValue(myEmail)
                      Toast.makeText(this, "회원가입완료", Toast.LENGTH_SHORT).show()
                      finish()

                    }else {
                      Toast.makeText(this, "중복확인 및 이메일을 재검토해주세요.", Toast.LENGTH_SHORT).show()
                    }
                  }
              }else {
                Toast.makeText(this, "아이디 중복검사를 해주세요.", Toast.LENGTH_SHORT).show()
              }
            }else {
              Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
          }else {
            Toast.makeText(this, "형식에 맞는 핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
          }
        }else {
          Toast.makeText(this, "약관에 동의해주세요.", Toast.LENGTH_SHORT).show()
        }
      }else {
        Toast.makeText(this, "모든값을 입력해주세요.", Toast.LENGTH_SHORT).show()
      }


    }




  }




  override fun onDestroy() {
    mBinding = null
    super.onDestroy()
  }
}