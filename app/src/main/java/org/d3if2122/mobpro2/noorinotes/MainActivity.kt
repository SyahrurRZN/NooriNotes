package org.d3if2122.mobpro2.noorinotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var date= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        setupCalendar()
//        setupListener()
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