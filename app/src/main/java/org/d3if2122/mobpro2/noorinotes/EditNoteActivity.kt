package org.d3if2122.mobpro2.noorinotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.SQLiteHelper
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() {
    private lateinit var editNoteBinding: ActivityEditNoteBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var note:Notes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        val rootView = editNoteBinding.root
        setContentView(rootView)
        sqLiteHelper = SQLiteHelper(this)

        val data =intent.getStringExtra("id")
//        note = sqLiteHelper.getNote(data.toString())
        setView(data.toString())
    }

    private fun setView(ids:String) {
        val cursor = sqLiteHelper.getNote(ids)

        var id:Int
        var judul:String
        var isi:String
        var urllink:String
        var gambar:String
        var tanggal:String

        if (cursor!!.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                if(id.equals(ids)){
                    judul = cursor.getString(cursor.getColumnIndex("judul"))
                    isi = cursor.getString(cursor.getColumnIndex("isi"))
                    urllink = cursor.getString(cursor.getColumnIndex("urllink"))
                    gambar = cursor.getString(cursor.getColumnIndex("gambar"))
                    tanggal = cursor.getString(cursor.getColumnIndex("tanggal"))

                    note = Notes(id = id, judul = judul, isi = isi, urlLink =  urllink, gambar = gambar, tanggal = tanggal)
                }
            }while (cursor.moveToNext())
        }
        cursor.close()
        editNoteBinding.eJudul.setText(note.judul)
        editNoteBinding.eIsi.setText(note.isi)
        editNoteBinding.eUrlLink.setText(note.urlLink)
        editNoteBinding.eGambar.setText(note.gambar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_save -> {
                updateNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateNote() {
        if(editNoteBinding.eJudul.text.toString().equals(note.judul)
            || editNoteBinding.eIsi.text.toString().equals(note.isi)
            || editNoteBinding.eUrlLink.text.toString().equals(note.urlLink)
            || editNoteBinding.eGambar.text.toString().equals(note.gambar)){
            Toast.makeText(this, "Record not changed", Toast.LENGTH_SHORT).show()
            return
        }
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm")
        val note = Notes(id = note.id,judul = editNoteBinding.eJudul.text.toString(),isi = editNoteBinding.eIsi.text.toString(),urlLink = editNoteBinding.eUrlLink.text.toString(),gambar = editNoteBinding.eGambar.text.toString(),tanggal = dateInString)
        val status = sqLiteHelper.updateNote(note)
        if(status>-1){
            Toast.makeText(this, "Update Berhasil", Toast.LENGTH_SHORT).show()
            finish()
        }
        else{
            Toast.makeText(this, "Update Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}