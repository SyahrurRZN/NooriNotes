package org.d3if2122.mobpro2.noorinotes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import org.d3if2122.mobpro2.noorinotes.Model.Notes
//import org.d3if2122.mobpro2.noorinotes.Support.SQLiteHelper
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityDetailNoteBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class DetailNoteActivity : AppCompatActivity() {
    private lateinit var detailNoteBinding: ActivityDetailNoteBinding
    private lateinit var note:Notes
//    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailNoteBinding = ActivityDetailNoteBinding.inflate(layoutInflater)
        val rootView = detailNoteBinding.root
        setContentView(rootView)
//        sqLiteHelper = SQLiteHelper(this)

        note = intent.getSerializableExtra("data") as Notes
//        note = sqLiteHelper.getNote(data.toString())


        setView()
    }

    private fun setView() {
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm")
//        detailNoteBinding.tTanggal.text = dateInString
//        val cursor = sqLiteHelper.getNote(ids)
//
//        var id:Int
//        var judul:String
//        var isi:String
//        var urllink:String
//        var gambar:String
//        var tanggal:String
//
//        if (cursor!!.moveToFirst()){
//            do{
//                id = cursor.getInt(cursor.getColumnIndex("id"))
//                if(id.equals(ids)){
//                    judul = cursor.getString(cursor.getColumnIndex("judul"))
//                    isi = cursor.getString(cursor.getColumnIndex("isi"))
//                    urllink = cursor.getString(cursor.getColumnIndex("urllink"))
//                    gambar = cursor.getString(cursor.getColumnIndex("gambar"))
//                    tanggal = cursor.getString(cursor.getColumnIndex("tanggal"))
//
//                    note = Notes(id = id, judul = judul, isi = isi, urlLink =  urllink, gambar = gambar, tanggal = tanggal)
//                }
//            }while (cursor.moveToNext())
//        }



        Glide.with(this).load(note.gambar).into(detailNoteBinding.tGambar)
        detailNoteBinding.tJudul.text = note.judul
        detailNoteBinding.tIsi.text = note.isi
        detailNoteBinding.tUrlLink.text = note.urlLink

    }

    fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_edit -> {
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("data",note as Serializable)
                startActivity(intent)
                return true
            }
            R.id.action_delete -> {
                deleteNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah benar ingin menghapus note ini?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
//            sqLiteHelper.deleteNote(note.id)
            dialog.dismiss()
            finish()
        }
        builder.setNegativeButton("No"){ dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}