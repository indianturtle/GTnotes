package com.example.gtnotes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class editnoteactivity : AppCompatActivity() {

    var medittileofnote: EditText ?= null
    var meditcontentofnote:EditText ?=null
    var msaveeditnote : FloatingActionButton ?=null

    var firebaseFirestore : FirebaseFirestore ?= null
    var firebaseAuth : FirebaseAuth ?= null
    var firebaseUser : FirebaseUser ?= null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editnoteactivity)

        meditcontentofnote = findViewById(R.id.editcontentofnote)
        medittileofnote = findViewById(R.id.edittitleofnote)
        msaveeditnote = findViewById(R.id.saveeditnote)

        var data: Intent = intent

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser


        var toolbar : Toolbar = findViewById(R.id.toolbarofeditnote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var notetitle : String ?= data.getStringExtra("title")
        var notecontent : String ?= data.getStringExtra("content")
        meditcontentofnote?.setText(notecontent)
        medittileofnote?.setText(notetitle)

        msaveeditnote!!.setOnClickListener{
           // Toast.makeText(this,"saved clicked",Toast.LENGTH_SHORT).show()
            var newtitle : String = medittileofnote?.text.toString()
            var newcontent: String = meditcontentofnote?.text.toString()

            if(newtitle.isEmpty() || newcontent.isEmpty()){
                Toast.makeText(this@editnoteactivity,"something is left empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                var documentReference : DocumentReference = firebaseFirestore!!.collection("notes").document(firebaseUser!!.uid).collection("myNotes").document(data.getStringExtra("noteId")!!)
                var note: HashMap<String, String> = hashMapOf("title" to newtitle,"content" to newcontent)
                documentReference.set(note).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@editnoteactivity,"note is updated",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@editnoteactivity,notesActivity::class.java))
                    }
                    else{
                        Toast.makeText(this@editnoteactivity,"failed to update",Toast.LENGTH_SHORT).show()

                    }

                }
            }

        }







    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId== android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}