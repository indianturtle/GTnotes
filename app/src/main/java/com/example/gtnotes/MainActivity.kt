package com.example.gtnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var mloginemail: EditText ?= null
    private var mloginpassword: EditText ?= null
    private var mlogin: Button ?= null
    private var mforgotpassword: TextView ?=null
    private var mgotosignup: Button ?= null
    private var mprogressbarofmainactivity: ProgressBar ?=null

    private var firebaseAuth: FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mloginemail=findViewById(R.id.loginemail)
        mloginpassword=findViewById(R.id.loginpassword)
        mlogin=findViewById(R.id.login)
        mforgotpassword=findViewById(R.id.forgotpassword)
        mgotosignup=findViewById(R.id.gotosignup)
        mprogressbarofmainactivity = findViewById(R.id.progressbar_of_mainactivity)

        firebaseAuth = FirebaseAuth.getInstance()
        var firebaseUser:FirebaseUser ?= firebaseAuth?.currentUser

        if(firebaseUser!=null){
            finish()
            startActivity(Intent(this,notesActivity::class.java))
        }

        mgotosignup?.setOnClickListener{
            startActivity(Intent(this,signup::class.java))
        }
        mforgotpassword?.setOnClickListener{
            startActivity(Intent(this,forgotpassword::class.java))
        }

        mlogin?.setOnClickListener{
            var mail:String = mloginemail?.text.toString().trim()
            var password : String = mloginpassword?.text.toString().trim()
            
            if(mail.isEmpty() || password.isEmpty())
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            else{
                //login the user
                mprogressbarofmainactivity?.visibility = View.VISIBLE
                firebaseAuth?.signInWithEmailAndPassword(mail,password)?.addOnCompleteListener {

                    if (it.isSuccessful) {
                        checkmailverification()
                    } else {
                        Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show()
                        mprogressbarofmainactivity?.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun checkmailverification() {
        var firebaseUser: FirebaseUser ?= firebaseAuth?.currentUser

        if (firebaseUser!!.isEmailVerified) {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, notesActivity::class.java))
        } else{
            mprogressbarofmainactivity?.visibility = View.INVISIBLE
            Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
            firebaseAuth!!.signOut()

        }
    }

}