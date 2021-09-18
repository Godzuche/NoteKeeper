package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.notekeeper.databinding.ActivityNoteListBinding

class NoteListActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private lateinit var binding: ActivityNoteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.noteListTopAppBar)

        binding.fab.setOnClickListener {
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            startActivity(activityIntent)
        }

        binding.contentNoteList.listItems.adapter = ArrayAdapter<NoteInfo>(this, android.R.layout.simple_list_item_1, DataManager.notes)

        binding.contentNoteList.listItems.setOnItemClickListener {parent, view, position, id->
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, position)
            startActivity(activityIntent)
        }
        Log.d(tag, "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_llist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings0 -> true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        (binding.contentNoteList.listItems.adapter as ArrayAdapter<NoteInfo>).notifyDataSetChanged()
        Log.d(tag, "onResume")
    }
}