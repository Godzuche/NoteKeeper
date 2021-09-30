package com.example.notekeeper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.notekeeper.databinding.ActivityEditNoteBinding

var saveable = true

class EditNoteActivity : AppCompatActivity(){
    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET
    private var noteColor: Int = Color.TRANSPARENT
    private val colorSelector by lazy { findViewById<ColorSelector>(R.id.colorSelector) }

    val noteGetTogetherHelper = NoteGetTogetherHelper(this, lifecycle)

    private lateinit var binding: ActivityEditNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.editNoteTopAppBar)

        val adapterCourses = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.contentEditNote.courseSpinner.adapter = adapterCourses

        notePosition =
            savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?: intent.getIntExtra(
                NOTE_POSITION,
                POSITION_NOT_SET
            )
        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        } else {
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }
        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .show()
        }*/

        colorSelector.addListener { color ->
            this.noteColor = color
        }
        Log.d(tag, "onCreate")
    }

    private fun displayNote() {
        Log.i(tag, "Displaying note for position $notePosition")

        val note = DataManager.notes[notePosition]
        binding.contentEditNote.noteTitle.setText(note.title)
        binding.contentEditNote.noteText.setText(note.text)
        colorSelector.selectedColorValue = (note.color)
        this.noteColor = note.color

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        binding.contentEditNote.courseSpinner.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                moveNext()
                true
            }
            R.id.action_getTogether -> {
                noteGetTogetherHelper.sendMessage(DataManager.loadNote(notePosition))
                true
            }
            R.id.action_discard -> {
                discard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun discard() {
        saveable = false
        val intent = Intent(this, NoteListActivity::class.java)
        startActivity(intent)
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_white_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        if (saveable)
            saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title = binding.contentEditNote.noteTitle.text.toString()
        note.text = binding.contentEditNote.noteText.text.toString()
        note.course = binding.contentEditNote.courseSpinner.selectedItem as CourseInfo
        note.color = this.noteColor
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

}