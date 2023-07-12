package com.example.gtnotes

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query



class notesActivity : AppCompatActivity() {


    var mcreatenotesfab:FloatingActionButton ?= null
    var firebaseAuth :FirebaseAuth ?=null

    var mrecyclerview: RecyclerView ?=null
    var staggeredGridLayoutManager:StaggeredGridLayoutManager?=null

    var firebaseUser:FirebaseUser ?=null
    var firebaseFirestore:FirebaseFirestore ?=null

    private var noteAdapter: FirestoreRecyclerAdapter<firebasemodel,noteViewHolder> ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        supportActionBar?.title = "All Notes"
        firebaseAuth = FirebaseAuth.getInstance()
        mcreatenotesfab = findViewById(R.id.createnotefab)
        firebaseUser = firebaseAuth?.currentUser
        firebaseFirestore = FirebaseFirestore.getInstance()



        mcreatenotesfab!!.setOnClickListener{
            startActivity(Intent(this,createnote::class.java))
        }

        val query:Query = firebaseFirestore!!.collection("notes").document(firebaseUser!!.uid).collection("myNote").orderBy("title")

        val allusernotes = FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel::class.java).build()

        noteAdapter = ProductFirestoreRecyclerAdapter(allusernotes)
        mrecyclerview = findViewById(R.id.recyclerview)
        mrecyclerview?.setHasFixedSize(true)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        mrecyclerview?.layoutManager=staggeredGridLayoutManager
        mrecyclerview?.adapter=noteAdapter


    }

    private inner class ProductFirestoreRecyclerAdapter(allusernotes: FirestoreRecyclerOptions<firebasemodel>) : FirestoreRecyclerAdapter<firebasemodel,noteViewHolder>(allusernotes) {
        //the below function 'onbindviewholder' runs each time for each note

        @RequiresApi(Build.VERSION_CODES.M)

        override fun onBindViewHolder(NoteViewHolder: noteViewHolder, position: Int, Firebasemodel: firebasemodel) {

            var docId = noteAdapter!!.snapshots.getSnapshot(position).id

            NoteViewHolder.setviewholder(Firebasemodel.title,Firebasemodel.content)
            NoteViewHolder.colorit()
            NoteViewHolder.menuclicked(Firebasemodel.title,Firebasemodel.content,docId)
            NoteViewHolder.noteclicked(Firebasemodel.title,Firebasemodel.content,docId)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): noteViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout,parent,false)
            return noteViewHolder(view)
        }
    }
    private inner class noteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notetitle:TextView ?= null
        var notecontent :TextView ?=null
        var mnote:LinearLayout ?=null

        fun setviewholder(notetitle:String,notecontent:String){
             this.notetitle =itemView.findViewById(R.id.notetitle)
             this.notecontent = itemView.findViewById(R.id.notecontent)

            this.notetitle?.text = notetitle
            this.notecontent?.text = notecontent
            mnote = itemView.findViewById(R.id.note)

        }

        @RequiresApi(Build.VERSION_CODES.M) //for getcolor()
        fun colorit(){
            val colorcode:Int = getRandomColor()
            mnote?.setBackgroundColor(itemView.resources.getColor(colorcode,null))
        }

        fun noteclicked(notetitle: String, notecontent: String, docId: String){
            itemView.setOnClickListener {
                intent = Intent(this@notesActivity, notedetails::class.java)

                intent.putExtra("title",notetitle)
                intent.putExtra("content",notecontent)
                intent.putExtra("noteId",docId)
                startActivity(intent)
               // Toast.makeText(this@notesActivity, "clicked", Toast.LENGTH_SHORT).show()
            }
        }


        @RequiresApi(Build.VERSION_CODES.M)
        fun menuclicked(notetitle: String, notecontent: String, docId: String){
            val popupbutton : ImageView=itemView.findViewById(R.id.menupopbutton)
            popupbutton.setOnClickListener{
                val popupMenu:PopupMenu= PopupMenu(it.context,it)
                popupMenu.gravity = Gravity.END
                popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                    intent = Intent(this@notesActivity, editnoteactivity::class.java)

                    intent.putExtra("title",notetitle)
                    intent.putExtra("content",notecontent)
                    intent.putExtra("docid",docId)
                    startActivity(intent)
                    return@setOnMenuItemClickListener false
                }
                popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                    //Toast.makeText(this@notesActivity,"this note is deleted",Toast.LENGTH_SHORT).show()
                    val documentReference : DocumentReference = firebaseFirestore!!.collection("notes").document(firebaseUser!!.uid).collection("myNotes").document(docId)
                    documentReference.delete().addOnCompleteListener {
                        if(it.isSuccessful)
                            Toast.makeText(this@notesActivity,"note deleted", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this@notesActivity,"failed to delete",Toast.LENGTH_SHORT).show()
                    }
                    return@setOnMenuItemClickListener false
                }
                popupMenu.show()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //switch in kotlin
        when(item.itemId){
            R.id.logout ->{
                firebaseAuth?.signOut()
                finish()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        noteAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (noteAdapter!=null)
            noteAdapter?.stopListening()
    }
    private fun getRandomColor(): Int {
        val colorcode = ArrayList<Int>()
        colorcode.add(R.color.color1)
        colorcode.add(R.color.color2)
        colorcode.add(R.color.color3)
        colorcode.add(R.color.color4)
        colorcode.add(R.color.color5)
        colorcode.add(R.color.color6)
        colorcode.add(R.color.color7)
        colorcode.add(R.color.color8)
        colorcode.add(R.color.color9)
        colorcode.add(R.color.color10)

        val number = (0 until colorcode.size).random()
        return colorcode[number]
    }
}