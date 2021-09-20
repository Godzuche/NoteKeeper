package com.example.notekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }

    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        val note = NoteInfo(course, noteTitle, noteText)
        notes.add(note)
        return notes.lastIndex
    }

    fun findNote(course: CourseInfo, noteTitle: String,noteText: String): NoteInfo? {
        for (note in notes)
            if (course == note.course && noteTitle == note.title && noteText == note.text)
                return note
        return null
    }
    fun initializeCourses() {
        var course = CourseInfo(title = "Java Fundamentals: The Java Language", courseId = "java_lang")
        courses[course.courseId] = course

        course = CourseInfo("android_intents", "Android Programming with Intents")
        courses[course.courseId] = course

        course = CourseInfo("android_async", "Android Async Programming and Services")
        courses[course.courseId] = course

        course = CourseInfo("java_core", "Java Fundamentals: The Core Platform")
        courses[course.courseId] = course
    }

    fun initializeNotes() {
        var note = courses["android_intents"]?.let {
            NoteInfo(
                it,
                "Dynamic intent resolution",
                "Wow, intents allow components to be resolved at runtime"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["android_intents"]?.let {
            NoteInfo(
                it,
                "Delegating Intents",
                "PendingIntents are powerful; they delegate much more then just a component invocation"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["android_async"]?.let {
            NoteInfo(
                it,
                "Service default threads",
                "Did you know that by default an Android Service will tie up the UI thread?"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["android_async"]?.let {
            NoteInfo(
                it,
                "Long running operations",
                "Foreground Services can be tied to a notification icon"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["java_lang"]?.let {
            NoteInfo(
                it,
                "Parameters",
                "Leverage variable-length parameter lists"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["java_lang"]?.let {
            NoteInfo(
                it,
                "Anonymous classes",
                "Anonymous classes simplify implementing one-use types"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["java_core"]?.let {
            NoteInfo(
                it,
                "Compiler option",
                "The -jar option isn't compatible with the -cp option"
            )
        }

        if (note != null) {
            notes.add(note)
        }
        note = courses["java_core"]?.let {
            NoteInfo(
                it,
                "Serialization",
                "Remember to include SerialVersionUID to assure version compatibility"
            )
        }

        if (note != null) {
            notes.add(note)
        }
    }

}