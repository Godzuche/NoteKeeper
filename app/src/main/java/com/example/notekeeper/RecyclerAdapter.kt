package com.example.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class RecyclerAdapter(private val context: Context?, private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.textCourse.text = note.course?.title
        holder.textTitle.text = note.title

        holder.notePosition = position
    }

    override fun getItemCount() = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse: MaterialTextView = itemView.findViewById(R.id.text_course_title)
        val textTitle: MaterialTextView = itemView.findViewById(R.id.text_note_title)

        var notePosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(NOTE_POSITION, notePosition)
                context?.startActivity(intent)
            }
        }
    }

}