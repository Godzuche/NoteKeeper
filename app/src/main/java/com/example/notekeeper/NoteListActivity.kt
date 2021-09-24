package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.databinding.ActivityNoteListBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class NoteListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNoteSelectedListener {
    private val tag = this::class.simpleName
    private lateinit var binding: ActivityNoteListBinding

    private val noteRecyclerAdapter by lazy {  val adapter = RecyclerAdapter(this, DataManager.notes)
        adapter.setOnSelectedListener(this)
        adapter
    }
    private val noteLayoutManager by lazy { LinearLayoutManager(this) }

    private val courseLayoutManager by lazy { GridLayoutManager(this, resources.getInteger(R.integer.course_grid_span)) }
    private val courseRecyclerAdapter by lazy { CourseRecyclerAdapter(this, DataManager.courses.values.toList()) }

    private val recentNotesLayoutManager by lazy { LinearLayoutManager(this) }
    private val recentNotesRecyclerAdapter by lazy { RecyclerAdapter(this,  viewModel.recentlyViewedNotes) }

    private val viewModel by lazy {ViewModelProvider(this)[NoteListActivityViewModel::class.java]}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.appBarNoteList.noteListTopAppBar)

        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarNoteList.noteListTopAppBar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener (this)

        binding.appBarNoteList.fab.setOnClickListener {
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            startActivity(activityIntent)
        }

        Log.d(tag, "onCreate")
    }

    private fun displayNotes() {
        binding.appBarNoteList.contentNoteList.listItems.layoutManager = noteLayoutManager
        binding.appBarNoteList.contentNoteList.listItems.adapter = noteRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        binding.appBarNoteList.contentNoteList.listItems.layoutManager = courseLayoutManager
        binding.appBarNoteList.contentNoteList.listItems.adapter = courseRecyclerAdapter

        binding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_list, menu)
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
        binding.appBarNoteList.contentNoteList.listItems.adapter?.notifyDataSetChanged()
        Log.d(tag, "onResume")
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_notes,
            R.id.nav_courses,
            R.id.nav_recent_notes -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
            R.id.nav_share -> {
                handleSelection(R.string.nav_share_message)
            }
            R.id.nav_send -> {
                handleSelection(R.string.nav_send_message)
            }
            R.id.nav_how_many -> {
                val message = getString(R.string.nav_how_many_message_format, DataManager.notes.size, DataManager.courses.size)
                Snackbar.make(binding.appBarNoteList.contentNoteList.listItems, message, Snackbar.LENGTH_LONG).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplaySelection(itemId: Int) {
        when(itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recent_notes -> {
                displayRecentlyViewedNotes()
            }
        }
    }

    private fun displayRecentlyViewedNotes() {
        binding.appBarNoteList.contentNoteList.listItems.adapter = recentNotesRecyclerAdapter
        binding.appBarNoteList.contentNoteList.listItems.layoutManager = recentNotesLayoutManager

        binding.navView.menu.findItem(R.id.nav_recent_notes).isChecked = true
    }

    private fun handleSelection(stringId: Int) {
        Snackbar.make(binding.appBarNoteList.contentNoteList.listItems, stringId, Snackbar.LENGTH_LONG).show()
    }

    override fun onNoteSelected(note: NoteInfo) {
        viewModel.addToRecentlyViewedNotes(note)
    }


}