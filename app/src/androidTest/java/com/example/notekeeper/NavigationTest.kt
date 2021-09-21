package com.example.notekeeper

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Rule @JvmField
    val noteListActivity = ActivityScenarioRule(NoteListActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange() {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_courses))

        val coursePosition = 0
        onView(withId(R.id.listItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CourseRecyclerAdapter.ViewHolder>(
                coursePosition,
                click()
            )
        )
    onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))

        val notePosition = 0
        onView(withId(R.id.listItems)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerAdapter.ViewHolder>(notePosition, click()))

        val note = DataManager.notes[notePosition]
        onView(withId(R.id.courseSpinner)).check(matches(withSpinnerText(containsString(note.course?.title))))
        onView(withId(R.id.note_title)).check(matches(withText(containsString(note.title))))
        onView(withId(R.id.note_text)).check(matches(withText(containsString(note.text))))
    }
}