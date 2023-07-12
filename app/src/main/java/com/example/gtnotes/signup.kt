package com.example.gtnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class signup : AppCompatActivity() {
    private var msignupemail : EditText ?= null
    private var msignuppassword : EditText ?= null
    private var msignup : Button ?= null
    private var mgotologin : TextView ?= null

    private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        msignupemail=findViewById(R.id.signupemail)
        msignuppassword = findViewById(R.id.signuppassword)
        msignup = findViewById(R.id.signup)
        mgotologin= findViewById(R.id.gotologin)

        firebaseAuth = FirebaseAuth.getInstance()

        mgotologin?.setOnClickListener{
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
        msignup?.setOnClickListener{
            var mail: String = msignupemail?.text.toString().trim()
            var password: String = msignuppassword?.text.toString().trim()
            if(mail.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show()
            }
            else if (password.length<7){
                Toast.makeText(this, "Atleast 7 character required for password", Toast.LENGTH_SHORT).show()
            }
            else
            {
                //register the user to firebase
                firebaseAuth?.createUserWithEmailAndPassword(mail,password)?.addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show()
                        sendEmailVerification()
                    }
                    else
                        Toast.makeText(this, "Error occurred, failed to register", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
    private fun sendEmailVerification(){
        var firebaseUser: FirebaseUser ?= firebaseAuth?.currentUser
        if(firebaseUser!=null) {
            firebaseUser?.sendEmailVerification()?.addOnCompleteListener {
                Toast.makeText(
                    this,
                    "Verification Email is sent, verify and log in again",
                    Toast.LENGTH_SHORT
                ).show()
                firebaseAuth?.signOut()
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        else
            Toast.makeText(this,"error occured, please try again later",Toast.LENGTH_LONG).show()

    }
}