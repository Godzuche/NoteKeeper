package com.example.notekeeper

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.notekeeper.databinding.ActivityNoteQuickViewBinding

class NoteQuickViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteQuickViewBinding
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteQuickViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        val adapterCourses = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList())

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.courseSpinner.adapter = adapterCourses

        if (notePosition != POSITION_NOT_SET)
            displayNote()

        binding.btnDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun deleteNote() {
        DataManager.notes.removeAt(notePosition)
    }

    private fun displayNote() {
        val note = DataManager.notes[notePosition]
        binding.noteTitle.text = note.title
        binding.noteText.text = note.text

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        binding.courseSpinner.setSelection(coursePosition)
    }
}