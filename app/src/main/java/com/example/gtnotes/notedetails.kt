package com.example.gtnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class notedetails : AppCompatActivity() {

    var mtitleofnotedetail: TextView ?= null
    var mcontentofnotedetail : TextView ?= null
    var mgotoeditnote : FloatingActionButton ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notedetails)

        var toolbar : Toolbar = findViewById(R.id.toolbarofnotedetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mtitleofnotedetail = findViewById(R.id.titleofnotedetail)
        mcontentofnotedetail = findViewById(R.id.contentofnotedetail)
        mgotoeditnote = findViewById(R.id.gotoeditnote)

        var data: Intent = intent

        mgotoeditnote!!.setOnClickListener{
            var abc: Intent = Intent(this@notedetails, editnoteactivity::class.java)
            abc.putExtra("title",data.getStringExtra("title"))
            abc.putExtra(data.getStringExtra("content"),"content")
            abc.putExtra("noteId",data.getStringExtra("noteId"))
            startActivity(abc)

        }

        mtitleofnotedetail?.setText(data.getStringExtra("title"))
        mcontentofnotedetail?.setText(data.getStringExtra("content"))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId== android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}