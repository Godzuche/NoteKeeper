package com.example.notekeeper

import android.graphics.Color
import androidx.core.app.Person

data class CourseInfo(val courseId: String, val title: String) {
    override fun toString(): String {
        return title
    }
}

data class NoteInfo(var course: CourseInfo? = null, var title: String? = null, var text: String? = null, var color: Int = Color.TRANSPARENT)

data class Message(val person: Person?, val time: Long, val text: String)