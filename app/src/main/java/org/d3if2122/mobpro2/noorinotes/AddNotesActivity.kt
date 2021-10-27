package org.d3if2122.mobpro2.noorinotes

import android.app.Activity
import android.app.Instrumentation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.SQLiteHelper
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityAddNotesBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNotesActivity : AppCompatActivity() {
    private lateinit var addNotesBinding: ActivityAddNotesBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_notes)
        addNotesBinding = ActivityAddNotesBinding.inflate(layoutInflater)
        val rooyView = addNotesBinding.root
        setContentView(rooyView)
        sqLiteHelper = SQLiteHelper(this)

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm")
        addNotesBinding.tTanggal.text = dateInString

    }

    fun Date.toString(format : String,locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date{
        return Calendar.getInstance().time
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_save -> {
                addNewNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun addNewNote() {
        val judul = addNotesBinding.eJudul.text.toString().trim()
        val isi = addNotesBinding.eIsi.text.toString().trim()
        val urlLink = addNotesBinding.eUrlLink.text.toString().trim()
        val gambar = addNotesBinding.eGambar.text.toString().trim()
        val tanggal = addNotesBinding.tTanggal.text.toString()

        if(judul.isEmpty() || isi.isEmpty() || gambar.isEmpty()){
            Toast.makeText(this,"Isi data yang diperlukan",Toast.LENGTH_SHORT).show()
        }else{
            val note = Notes(judul = judul,isi = isi,urlLink = urlLink, gambar = gambar,tanggal = tanggal)
            val status = sqLiteHelper.insertNote(note)
            if(status>-1){
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Note ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Tidak tersimpan", Toast.LENGTH_SHORT).show()
            }
        }
    }

}