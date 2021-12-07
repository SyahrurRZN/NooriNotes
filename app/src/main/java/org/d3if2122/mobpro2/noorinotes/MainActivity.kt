package org.d3if2122.mobpro2.noorinotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.HistoryAdapter
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var date= null

    val db by lazy {NotesDB(this)}
    lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        setupCalendar()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(arrayListOf(), object : HistoryAdapter.OnHistoryAdapterListener{
            override fun onRead(note: Notes) {
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                var dateAwal = formatter.format(note.tanggal)
                dateAwal = dateAwal+" 00:00:00"
                var dateAkhir = formatter.format(note.tanggal)
                dateAkhir = dateAkhir+" 23:59:59"
                intentCustom(note.id,Constants.TYPE_READ,dateAwal,dateAkhir)
            }

        })

        activityMainBinding.historyrecycle.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = historyAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        loadDataHistory()
    }

    private fun loadDataHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val allnotes = db.notesDao().getAllNotes()
            withContext(Dispatchers.Main){
                historyAdapter.setData(allnotes)
            }
        }
    }

    private fun intentCustom(noteId: Int, intentType: Int, dateAwal: String, dateAkhir: String) {
        startActivity(
            Intent(applicationContext,EditNoteActivity::class.java)
                .putExtra("note_id",noteId)
                .putExtra("intent_type",intentType)
                .putExtra("awal",dateAwal)
                .putExtra("akhir",dateAkhir)
        )
    }

    private fun setupCalendar() {
        activityMainBinding.calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            getDataCalendarView(year,month+1,dayOfMonth)
        }
    }

    private fun getDataCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        var dateSebelum = "$dayOfMonth/${month}/$year 00:00:00"
        var dateSesudah = "$dayOfMonth/${month}/$year 23:59:59"
        val dataKosongan = "$dayOfMonth/${month}/$year"

        startActivity(Intent(
            applicationContext,DayListNotesActivity::class.java)
            .putExtra("awal",dateSebelum)
            .putExtra("akhir",dateSesudah)
            .putExtra("kosongan",dataKosongan)
        )
    }
}