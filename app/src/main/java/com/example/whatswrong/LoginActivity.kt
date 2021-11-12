package com.example.whatswrong

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btn_register : Button = findViewById(R.id.btn_register)


        btn_register.setOnClickListener {
            val intent_reg  = Intent(this, SignupActivity::class.java)
            startActivity(intent_reg)
        }
    }

}