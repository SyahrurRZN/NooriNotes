package org.d3if2122.mobpro2.noorinotes

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.NotesAdapter
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityDayListNotesBinding
import java.text.SimpleDateFormat

class DayListNotesActivity : AppCompatActivity() {
    private lateinit var activityDayListNotesBinding: ActivityDayListNotesBinding

    val db by lazy { NotesDB(this) }
    lateinit var notesAdapter: NotesAdapter
    private var dateAwal = ""
    private var dateAkhir = ""
    private var dateKosongan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDayListNotesBinding = ActivityDayListNotesBinding.inflate(layoutInflater)
        val rootView = activityDayListNotesBinding.root
        setContentView(rootView)


        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(arrayListOf(),object : NotesAdapter.OnAdapterListener{
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
        activityDayListNotesBinding.recycleview.apply {
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
        alertDialog.create().show()
    }

    private fun setupListener() {
        activityDayListNotesBinding.fab.setOnClickListener{
            intentCustom(0,Constants.TYPE_CREATE)
        }
    }

    private fun intentCustom(noteId: Int, intentType: Int) {
        runOnUiThread(Runnable {
            startActivity(
                Intent(applicationContext,EditNoteActivity::class.java)
                    .putExtra("note_id",noteId)
                    .putExtra("intent_type",intentType)
                    .putExtra("awal",dateAwal)
                    .putExtra("akhir",dateAkhir)
            )
        })

    }

    override fun onStart() {
        super.onStart()
        loadData()
        supportActionBar!!.title = dateKosongan
    }

    private fun loadData() {
        dateAwal = intent.getStringExtra("awal").toString()
        dateAkhir = intent.getStringExtra("akhir").toString()
        dateKosongan = intent.getStringExtra("kosongan").toString()
        //supportActionBar!!.title = dateKosongan
        val formatter = SimpleDateFormat(Constants.sdf)
        val awalBanget = formatter.parse(dateAwal)
        val akhirBanget = formatter.parse(dateAkhir)
        CoroutineScope(Dispatchers.IO).launch {
            val allnotes = db.notesDao().getAllNotesbyDay(awalBanget,akhirBanget)
            /*val allnotes2 = db.notesDao().getAllNotes()
            Log.d("DayListNote","Data : $allnotes")
            Log.d("DayListNote","Data2 : $allnotes2")*/
            withContext(Dispatchers.Main){
                notesAdapter.setData(allnotes)
            }
        }
    }
}