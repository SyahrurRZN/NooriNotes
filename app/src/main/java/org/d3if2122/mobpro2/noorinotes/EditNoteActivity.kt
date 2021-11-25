package org.d3if2122.mobpro2.noorinotes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() {
    private lateinit var editNoteBinding: ActivityEditNoteBinding
    private val db by lazy { NotesDB(this) }
    private var noteId =0
    private var notegambar=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        val rootView = editNoteBinding.root
        setContentView(rootView)

        setView()
        setListener()
    }

    private fun setListener() {
        editNoteBinding.buttonTambah.setOnClickListener{
            if(editNoteBinding.eJudul.text.toString().trim().isEmpty()
                || editNoteBinding.eIsi.text.toString().trim().isEmpty()
                || editNoteBinding.eUrlLink.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Data harus diisi!", Toast.LENGTH_SHORT).show()
            }
            else{
                CoroutineScope(Dispatchers.IO).launch {
                    db.notesDao().tambahNote(
                        Notes(
                            0,
                            editNoteBinding.eJudul.text.toString(),
                            editNoteBinding.eIsi.text.toString(),
                            editNoteBinding.eUrlLink.text.toString(),
                            "rr",//TODO:belum
                            editNoteBinding.tTanggal.text.toString()
                        )
                    )
                    finish()
                }
            }
        }
        editNoteBinding.buttonUpdate.setOnClickListener{
            if(editNoteBinding.eJudul.text.toString().trim().isEmpty()
                || editNoteBinding.eIsi.text.toString().trim().isEmpty()
                || editNoteBinding.eUrlLink.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else{
                CoroutineScope(Dispatchers.IO).launch {
                    db.notesDao().ubahNote(
                        Notes(
                            noteId,
                            editNoteBinding.eJudul.text.toString(),
                            editNoteBinding.eIsi.text.toString(),
                            editNoteBinding.eUrlLink.text.toString(),
                            "rr",//TODO:belum
                            editNoteBinding.tTanggal.text.toString()
                        )
                    )
                    finish()
                }
            }
        }
    }

    private fun setView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        when(intentType()){
            Constants.TYPE_CREATE ->{
                supportActionBar!!.title = "BUAT NOTE BARU"
                editNoteBinding.buttonTambah.visibility = View.VISIBLE
                editNoteBinding.buttonUpdate.visibility = View.GONE
            }
            Constants.TYPE_READ ->{
                supportActionBar!!.title = "LIHAT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.GONE
                geNote()
            }
            Constants.TYPE_UPDATE ->{
                supportActionBar!!.title = "EDIT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.VISIBLE
                geNote()
            }
        }
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm")
        editNoteBinding.tTanggal.text = dateInString
    }

    private fun geNote() {
        noteId = intent.getIntExtra("note_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val selectedNote = db.notesDao().getNote(noteId).get(0)
            editNoteBinding.eJudul.setText(selectedNote.judul)
            editNoteBinding.eIsi.setText(selectedNote.isi)
            editNoteBinding.eUrlLink.setText(selectedNote.urlLink)
            editNoteBinding.tTanggal.setText(selectedNote.tanggal)
            notegambar = selectedNote.gambar
            loadGambar(selectedNote.gambar)
        }
    }

    private fun loadGambar(gambar: String) {
        Log.d("EditActivity","gambar : ${gambar}")
        runOnUiThread{
            Picasso.get()
                .load(Uri.parse(gambar))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(editNoteBinding.iGambar)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun intentType(): Int {
        return intent.getIntExtra("intent_type",0)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_add,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.action_save -> {
//                updateNote()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

//    private fun updateNote() {
//        if(editNoteBinding.eJudul.text.toString().equals(note.judul)
//            || editNoteBinding.eIsi.text.toString().equals(note.isi)
//            || editNoteBinding.eUrlLink.text.toString().equals(note.urlLink)
//            || editNoteBinding.eGambar.text.toString().equals(note.gambar)){
//            Toast.makeText(this, "Record not changed", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val date = getCurrentDateTime()
//        val dateInString = date.toString("dd/MM/yyyy HH:mm")
//        val note = Notes(id = note.id,judul = editNoteBinding.eJudul.text.toString(),isi = editNoteBinding.eIsi.text.toString(),urlLink = editNoteBinding.eUrlLink.text.toString(),gambar = editNoteBinding.eGambar.text.toString(),tanggal = dateInString)
//        val status = sqLiteHelper.updateNote(note)
//        if(status>-1){
//            Toast.makeText(this, "Update Berhasil", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//        else{
//            Toast.makeText(this, "Update Gagal", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}