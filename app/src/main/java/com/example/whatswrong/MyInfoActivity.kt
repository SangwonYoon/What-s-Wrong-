package com.example.whatswrong

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatswrong.databinding.ActivityMypageBinding
import com.example.whatswrong.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.lang.Exception

class MyInfoActivity : AppCompatActivity() {

  //view binding
  private var mBinding : ActivityMypageBinding? = null
  private val binding get() = mBinding!!

  //FireBase & RealTimeBase connect
  private lateinit var mFirebaseAuth : FirebaseAuth // 파이어베이스 인증처리
  private lateinit var mDatabaseRef : DatabaseReference


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //view binding
    mBinding = ActivityMypageBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //connect Firebase
    mFirebaseAuth = FirebaseAuth.getInstance()
    val myUid = mFirebaseAuth.uid
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Whatswrong")

//    mDatabaseRef.child(myUid!!).addValueEventListener(object : ValueEventListener{
//      override fun onDataChange(snapshot: DataSnapshot) {
//        Log.e("DataSnap :: ",snapshot.toString())
//        val temp = snapshot.getValue(UserAccount::class.java)
//        binding.tvUsername.text = temp!!.strName
//        binding.tvSchoolname.text = temp!!.strUniv
//        binding.tvMajor.text = temp!!.strNickname
//      }
//
//      override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
//      }
//
//    })

    mDatabaseRef.child("UserAccount").child(myUid!!).get().addOnSuccessListener {
      Log.e("DataSnap :: ",it.toString())
        val temp = it.getValue(UserAccount::class.java)
        binding.tvUsername.text = temp!!.strName
        binding.tvSchoolname.text = temp!!.strUniv
        binding.tvMajor.text = temp!!.strNickname

    }.addOnFailureListener{
      Log.e("firebase", "Error getting data", it)
    }


    binding.btnLogout.setOnClickListener {

      val builder = AlertDialog.Builder(this)
      builder.setTitle("로그아웃")
      builder.setMessage("로그아웃 하시겠습니까?")
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
        mFirebaseAuth.signOut()
        val intent_login = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "로그아웃완료", Toast.LENGTH_SHORT).show()
        startActivity(intent_login)
      }
      builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
      }
      builder.show()
    }

    binding.btnMyInfo.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle("개인정보취급방침")
      builder.setMessage(resources.getString(R.string.Info))
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
      }
      builder.show()

    }

    binding.btnVersion.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle("버전")
      builder.setMessage("Version.1.0.1")
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
      }
      builder.show()
    }

    binding.btnDeluser.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle("회원 탈퇴")
      builder.setMessage("회원을 탈퇴하면 계정을 복구 할 수 없습니다.")
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
        mDatabaseRef.child("UserAccount").child(myUid!!).removeValue()
        mDatabaseRef.child("Email-List").child(myUid!!).removeValue()
        mFirebaseAuth.currentUser!!.delete()
        val intent_login = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "회원탈퇴완료", Toast.LENGTH_SHORT).show()
        startActivity(intent_login)
      }
      builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->

      }
      builder.show()
    }



    binding.btnFaq.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle("FaQ")
      builder.setMessage(resources.getString(R.string.FaQ))
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
      }
      builder.show()
    }

    binding.btnHelp.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle("도움말")
      builder.setMessage(resources.getString(R.string.InfoHelp))
      builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
      }
      builder.show()
    }

    binding.btnChgnickname.setOnClickListener {
      val dialog = ChgNicNameDialog(this)
      dialog.myDialog()
      dialog.setOnClickedListener(object : ChgNicNameDialog.ButtonClickListener{
        override fun onClicked(nickname: String) {
          mDatabaseRef.child("UserAccount").child(myUid!!).get().addOnSuccessListener {
            Log.e("DataSnap :: ",it.toString())
            val temp = it.getValue(UserAccount::class.java)
            temp!!.strNickname = nickname
            mDatabaseRef.child("UserAccount").child(myUid!!).setValue(temp)
            try {
              val intent = intent
              finish() //현재 액티비티 종료 실시
              overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
              startActivity(intent) //현재 액티비티 재실행 실시
              overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
          }
        }

      })
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


  override fun onDestroy() {
    mBinding = null
    super.onDestroy()
  }
}


