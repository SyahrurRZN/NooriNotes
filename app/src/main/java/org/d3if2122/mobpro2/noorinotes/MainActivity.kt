package org.d3if2122.mobpro2.noorinotes

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.AlarmReceiver
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.HistoryAdapter
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    val MENU_LIST = Menu.FIRST
    val MENU_GRID = Menu.FIRST + 1
    var enableList = false
    var enableGrid = true
    private var tampilan = 1
    var layoutManagerBaru = GridLayoutManager(this,tampilan)

    private  var itemGrid: MenuItem? = null
    private  var itemList: MenuItem? = null
    val db by lazy { NotesDB(this) }
    lateinit var historyAdapter: HistoryAdapter
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        setupRecyclerView()
        setupDialog()

    }

    private fun setupDialog() {
        val dateSetListerner = object  : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(year,month,dayOfMonth)
                getDataCalendarView(year,month+1,dayOfMonth)
            }
        }
        activityMainBinding.fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
//                DatePickerDialog(this@MainActivity,dateSetListerner,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
                getDataCalendarView(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH))
//                intentCustom(0,Constants.TYPE_CREATE)
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if(enableList)
            menu?.add(0, MENU_LIST,Menu.NONE,"List")?.setIcon(R.drawable.ic_baseline_view_list_24)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        if(enableGrid)
            menu?.add(0, MENU_GRID,Menu.NONE,"Grid")?.setIcon(R.drawable.ic_baseline_grid_on_24)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun updateMenuItems(b: Boolean) {
        if(b){
            itemList?.isVisible=false
            itemGrid?.isVisible=true
        }
        else{
            itemList?.isVisible=true
            itemGrid?.isVisible=false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_LIST -> {
                showList()
                true
            }
            MENU_GRID -> {
                showGrid()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showList() {
        tampilan = 1
        enableList = false
        enableGrid = true
        layoutManagerBaru.spanCount = tampilan
        invalidateOptionsMenu()
        println("List")
    }

    private fun showGrid() {
        tampilan = 2
        enableList = true
        enableGrid = false
        layoutManagerBaru.spanCount = tampilan
        invalidateOptionsMenu()
        println("Grid")
    }

    private fun setupRecyclerView() {
        historyAdapter =
            HistoryAdapter(arrayListOf(), object : HistoryAdapter.OnHistoryAdapterListener {
                override fun onRead(note: Notes) {
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    var dateAwal = formatter.format(note.tanggal)
                    dateAwal = dateAwal + " 00:00:00"
                    var dateAkhir = formatter.format(note.tanggal)
                    dateAkhir = dateAkhir + " 23:59:59"
                    intentCustom(note.id, Constants.TYPE_READ, dateAwal, dateAkhir)
                }

                override fun onLoong(note: Notes) {
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    var dateAwal = formatter.format(note.tanggal)
                    dateAwal = dateAwal + " 00:00:00"
                    var dateAkhir = formatter.format(note.tanggal)
                    dateAkhir = dateAkhir + " 23:59:59"
                    deleteEditNoteDialog(note, Constants.TYPE_UPDATE, dateAwal, dateAkhir)
                }

            })

        activityMainBinding.historyrecycle.apply {
            layoutManager = layoutManagerBaru
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
            withContext(Dispatchers.Main) {
                historyAdapter.setData(allnotes)
            }
        }
    }

    private fun intentCustom(noteId: Int, intentType: Int, dateAwal: String, dateAkhir: String) {
        startActivity(
            Intent(applicationContext, EditNoteActivity::class.java)
                .putExtra("note_id", noteId)
                .putExtra("intent_type", intentType)
                .putExtra("awal", dateAwal)
                .putExtra("akhir", dateAkhir)
        )
    }

//    private fun setupCalendar() {
//        activityMainBinding.calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
//            getDataCalendarView(year,month+1,dayOfMonth)
//        }
//    }

    private fun getDataCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        var dateSebelum = "$dayOfMonth/${month}/$year 00:00:00"
        var dateSesudah = "$dayOfMonth/${month}/$year 23:59:59"
        val dataKosongan = "$dayOfMonth/${month}/$year"

        startActivity(
            Intent(
                applicationContext, EditNoteActivity::class.java
            )
                .putExtra("note_id",0)
                .putExtra("intent_type",Constants.TYPE_CREATE)
                .putExtra("awal", dateSebelum)
                .putExtra("akhir", dateSesudah)
                .putExtra("kosongan", dataKosongan)
        )
    }

    private fun deleteEditNoteDialog(note:Notes, intentType: Int, dateAwal: String, dateAkhir: String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Edit atau hapus ${note.judul}?")
            setNegativeButton("Hapus"){dialogInterface, i ->
                deleteDialog(note)
            }
            setPositiveButton("Edit"){dialogInterface, i ->
                intentCustom(note.id, intentType, dateAwal, dateAkhir)
            }
        }
        alertDialog.create().show()
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
                    loadDataHistory()
                }
            }
        }
        alertDialog.create().show()
    }
    private fun cancelAlarm(judul : String,id : Long){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("judul",judul)
        val pendingIntent = PendingIntent.getBroadcast(this,id.toInt(),intent,0)
        alarmManager.cancel(pendingIntent)
    }
}