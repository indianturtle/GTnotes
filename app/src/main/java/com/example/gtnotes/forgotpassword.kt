package com.example.gtnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class forgotpassword : AppCompatActivity() {

    private var mforgotpassword: EditText ?= null
    private var mpasswordrecoverbutton : Button ?= null
    private var mgobacktologin: TextView ?= null

    private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        supportActionBar?.hide()

        mforgotpassword = findViewById(R.id.registeredemail)
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton)
        mgobacktologin= findViewById(R.id.gobacktologin)

        firebaseAuth = FirebaseAuth.getInstance()

        mgobacktologin?.setOnClickListener{
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
        mpasswordrecoverbutton?.setOnClickListener{
            var mail: String = mforgotpassword?.text.toString().trim()
            if(mail.isEmpty()){
                Toast.makeText(this, "enter your email first", Toast.LENGTH_SHORT).show()
            }
            else{
                //we have to send
                firebaseAuth?.sendPasswordResetEmail(mail)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"recovery mail sent,check your email",Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    else
                        Toast.makeText(this,"email is wrong or account does not exist",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}