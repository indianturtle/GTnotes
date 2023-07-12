package com.example.gtnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class createnote : AppCompatActivity() {
    var mcreatetitleofnote:EditText?=null
    var mcreatecontentofnote:EditText?=null
    var msavenote:FloatingActionButton?=null
    var firebaseAuth:FirebaseAuth?=null
    var firebaseUser:FirebaseUser?=null
    var firebaseFirestore:FirebaseFirestore?=null
    var mprogressbarofcreatenote: ProgressBar ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createnote)

        mcreatetitleofnote= findViewById(R.id.createtitleofnote)
        mcreatecontentofnote=findViewById(R.id.createcontentofnote)
        msavenote= findViewById(R.id.savenote)
        mprogressbarofcreatenote = findViewById(R.id.progressbar_of_createnote)

        var toolbar:Toolbar=findViewById(R.id.toolbarofcreatenode)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth= FirebaseAuth.getInstance()
        firebaseUser=firebaseAuth?.currentUser
        firebaseFirestore= FirebaseFirestore.getInstance()


        msavenote!!.setOnClickListener{


            var title:String = mcreatetitleofnote?.text.toString()
            var content:String= mcreatecontentofnote?.text.toString()

            if(title.isEmpty())
                Toast.makeText(this,"Title is required",Toast.LENGTH_SHORT).show()
            else {
                mprogressbarofcreatenote?.visibility = View.VISIBLE

                //here think of notes as a book..firebaseUser.uid a page of that book(each user)..myNote each paragraph in that page(each note)
                var documentReference: DocumentReference = firebaseFirestore?.collection("notes")?.document(firebaseUser!!.uid)?.collection("myNote")!!.document()

                var note: HashMap<String, String> = hashMapOf("title" to title,"content" to content)


                documentReference.set(note).addOnSuccessListener{
                    mprogressbarofcreatenote?.visibility = View.INVISIBLE

                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, notesActivity::class.java))

                }.addOnFailureListener {
                    mprogressbarofcreatenote?.visibility = View.INVISIBLE

                    Toast.makeText(this, "unable to save,check your internet", Toast.LENGTH_SHORT).show()

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