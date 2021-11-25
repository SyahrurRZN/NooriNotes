package org.d3if2122.mobpro2.noorinotes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.NotesAdapter
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    val db by lazy { NotesDB(this) }
    lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(arrayListOf(),object :NotesAdapter.OnAdapterListener{
            override fun onRead(note: Notes) {
                intentCustom(note.id,Constants.TYPE_READ)
            }

            override fun onUpdate(note: Notes) {
                intentCustom(note.id,Constants.TYPE_UPDATE)
            }

            override fun onDelete(note: Notes) {
                deleteDialog(note)
            }

        })
        activityMainBinding.recycleview.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = notesAdapter
        }
    }

    private fun deleteDialog(note: Notes) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${note.judul}?")
            setNegativeButton("Batal"){dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.notesDao().hapusNote(note)
                    loadData()
                }
            }
        }
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val allnotes = db.notesDao().getAllNotes()
            Log.d("MainActivity","dbREsponse : $allnotes")
            withContext(Dispatchers.Main){
                notesAdapter.setData(allnotes)
            }
        }
    }

    private fun setupListener() {
        activityMainBinding.fab.setOnClickListener{
            intentCustom(0,Constants.TYPE_CREATE)
        }
    }

    private fun intentCustom(noteId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext,EditNoteActivity::class.java)
                .putExtra("note_id",noteId)
                .putExtra("intent_type",intentType)
        )
    }
}