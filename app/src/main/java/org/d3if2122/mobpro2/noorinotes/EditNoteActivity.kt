package org.d3if2122.mobpro2.noorinotes

import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import com.swein.easypermissionmanager.EasyPermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityEditNoteBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class  EditNoteActivity : AppCompatActivity() {
    private lateinit var editNoteBinding: ActivityEditNoteBinding
    private val db by lazy { NotesDB(this) }
    private var noteId =0
    //    private var pilihUri: Uri? =null
    private var tempUri: Uri? =null
    //    private var imagepathtemp = ""
    private var readNote = false
    private var timeAwal =""
    private var timeAkhir =""
    private var dateTamppung:Date? = null

    private val easyPermissionManager = EasyPermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        val rootView = editNoteBinding.root
        setContentView(rootView)

        setView()
        setListener()
    }

    override fun onStart() {
        super.onStart()
        timeAwal= intent.getStringExtra("awal").toString()
        timeAkhir= intent.getStringExtra("akhir").toString()
    }

    private fun setListener() {
        editNoteBinding.buttonTambah.setOnClickListener{
            if(editNoteBinding.eJudul.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Data harus diisi!", Toast.LENGTH_SHORT).show()
            }
            else if(tempUri==null){
                Toast.makeText(this, "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            else{
                if(tempUri!=null){
                    CoroutineScope(Dispatchers.IO).launch {
                        db.notesDao().tambahNote(
                            Notes(
                                0,
                                editNoteBinding.eJudul.text.toString(),
                                editNoteBinding.eIsi.text.toString(),
                                editNoteBinding.eUrlLink.text.toString(),
                                getBitmap(tempUri),
                                getDateKosongan(),
                                getCurrentDateTime()
                            )
                        )
                        finish()
                    }
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        db.notesDao().tambahNote(
                            Notes(
                                0,
                                editNoteBinding.eJudul.text.toString(),
                                editNoteBinding.eIsi.text.toString(),
                                editNoteBinding.eUrlLink.text.toString(),
                                getBitmapDefault(),
                                getDateKosongan(),
                                getCurrentDateTime()
                            )
                        )
                        finish()
                    }
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
                if(tempUri!=null){
                    CoroutineScope(Dispatchers.IO).launch {
                        db.notesDao().ubahNote(
                            Notes(
                                noteId,
                                editNoteBinding.eJudul.text.toString(),
                                editNoteBinding.eIsi.text.toString(),
                                editNoteBinding.eUrlLink.text.toString(),
                                getBitmap(tempUri),
                                dateTamppung,
                                getCurrentDateTime()
                            )
                        )
                        finish()
                    }
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        db.notesDao().ubahNote(
                            Notes(
                                noteId,
                                editNoteBinding.eJudul.text.toString(),
                                editNoteBinding.eIsi.text.toString(),
                                editNoteBinding.eUrlLink.text.toString(),
                                getBitmapDefault(),
                                dateTamppung,
                                getCurrentDateTime()
                            )
                        )
                        finish()
                    }
                }
            }
        }
        editNoteBinding.iGambar.setOnClickListener{
            if(readNote == false){
                imagePickDialog()
            }
        }
    }

    private fun imagePickDialog() {
        val options = arrayOf("Camera","Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options){dialog,which ->
            if (which==0){

                easyPermissionManager.requestPermission(
                    "permission",
                    "permission are necessary,",
                    "setting",
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ){
                    tempUri = FileProvider.getUriForFile(this,"org.d3if2122.mobpro2.noorinotes.provider", createImageFile().also{
//                        imagepathtemp = it.absolutePath
                    })
                    cameraLauncher.launch(tempUri)
                }
            }
            else if (which==1){
                easyPermissionManager.requestPermission(
                    "permission",
                    "permission are necessary,",
                    "setting",
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ){
                    selectPictureLauncher.launch("image/*")
                }

            }
        }
        builder.create().show()
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(System.currentTimeMillis().toString(),".jpg",storageDir)
    }

    private fun setView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        when(intentType()){
            Constants.TYPE_CREATE ->{
                supportActionBar!!.title = "BUAT NOTE BARU"
                editNoteBinding.buttonTambah.visibility = View.VISIBLE
                editNoteBinding.buttonUpdate.visibility = View.GONE
                readNote = false
            }
            Constants.TYPE_READ ->{
                supportActionBar!!.title = "LIHAT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.GONE
                readNote = true
                geNote()
            }
            Constants.TYPE_UPDATE ->{
                supportActionBar!!.title = "EDIT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.VISIBLE
                readNote = false
                geNote()
            }
        }
        val date = getDateKosongan()
        val dateInString = date.toString(Constants.sdf)
        editNoteBinding.tTanggal.text = dateInString

    }

    private fun geNote() {
        noteId = intent.getIntExtra("note_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val selectedNote = db.notesDao().getNote(noteId).get(0)
            editNoteBinding.eJudul.setText(selectedNote.judul)
            editNoteBinding.eIsi.setText(selectedNote.isi)
            editNoteBinding.eUrlLink.setText(selectedNote.urlLink)
            editNoteBinding.tTanggal.setText(selectedNote.tanggal.toString())
            dateTamppung = selectedNote.tanggal
//            notegambar = selectedNote.gambar
//            imagepathtemp = selectedNote.gambar
//            loadGambar(selectedNote.gambar)
            if(selectedNote!=null){
                editNoteBinding.iGambar.setImageBitmap(selectedNote.gambar)
                val photo = selectedNote.gambar

                val file = File(applicationContext.cacheDir,"CUSTOM NAME") //Get Access to a local file.
                file.delete() // Delete the File, just in Case, that there was still another File
                file.createNewFile()
                val fileOutputStream = file.outputStream()
                val byteArrayOutputStream = ByteArrayOutputStream()
                photo!!.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
                val bytearray = byteArrayOutputStream.toByteArray()
                fileOutputStream.write(bytearray)
                fileOutputStream.flush()
                fileOutputStream.close()
                byteArrayOutputStream.close()

                tempUri = file.toUri()
            }
        }
    }

//    private fun loadGambar(gambar: Bitmap) {
//        runOnUiThread{
//            Picasso.get()
//                .load(gambar)
//                .placeholder(R.drawable.ic_baseline_image_24)
//                .error(R.drawable.ic_baseline_broken_image_24)
//                .into(editNoteBinding.iGambar)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun intentType(): Int {
        return intent.getIntExtra("intent_type",0)
    }

    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            tempUri = it
//            imagepathtemp = it.path.toString()
            loadGambarURI(it)
        }
    }

    private fun loadGambarURI(it: Uri?) {
        runOnUiThread{
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(editNoteBinding.iGambar)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ success ->
        if(success){
            loadGambarURI(tempUri)
        }

    }

    fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getDateKosongan():Date{
        val formater = SimpleDateFormat(Constants.sdf)
        if(!timeAwal.isEmpty()){
            val daw = formater.parse(timeAwal)
            val dak = formater.parse(timeAkhir)
            if(getCurrentDateTime().after(daw) && getCurrentDateTime().before(dak)){
                return getCurrentDateTime()
            }
            else{
                return daw
            }
        }
        else{
            return getCurrentDateTime()
        }
    }
    private fun getBitmap(tempUri: Uri?): Bitmap? {
        return MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(tempUri.toString()))
    }

    private fun getBitmapDefault():Bitmap?{
        return BitmapFactory.decodeResource(resources,R.drawable.ic_baseline_image_24)
    }
}