package org.d3if2122.mobpro2.noorinotes

import android.Manifest
import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import com.swein.easypermissionmanager.EasyPermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.AlarmReceiver
import org.d3if2122.mobpro2.noorinotes.Support.Constants
import org.d3if2122.mobpro2.noorinotes.Support.NotesDB
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityEditNoteBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() {
    private lateinit var editNoteBinding: ActivityEditNoteBinding
    private val db by lazy { NotesDB(this) }
    private var noteId = 0
    private var tempUri: Uri? = null

    private var uriBaru: Uri? = null
    private var readNote = false
    private var timeAwal = ""
    private var timeAkhir = ""
    private var dateTamppung: Date? = null
    var alarmChecked = false

//    private lateinit var t: Thread
    var cal = Calendar.getInstance()
    var calAlarm = Calendar.getInstance()

    private val easyPermissionManager = EasyPermissionManager(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        val rootView = editNoteBinding.root
        setContentView(rootView)

//        createNotificationChannel()
        noteId = intent.getIntExtra("note_id", 0)
        timeAwal = intent.getStringExtra("awal").toString()
        timeAkhir = intent.getStringExtra("akhir").toString()
        setView()
        setListener()
    }

    private fun setAlarm(judul : String,id : Long,isi:String){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("judul",judul)
        intent.putExtra("isi",isi)
        val pendingIntent = PendingIntent.getBroadcast(this,id.toInt(),intent,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.timeInMillis,pendingIntent)
//        Toast.makeText(this, "Alarm Telah dibuat", Toast.LENGTH_SHORT).show()
    }



//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel("noorinotes","Noori Notes", NotificationManager.IMPORTANCE_HIGH)
//        channel.description = "Channel For Alarm Manager"
//        val notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(channel)
//    }

    private fun setListener() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(year,month,dayOfMonth)
                editNoteBinding.tTanggal.setText(SimpleDateFormat("dd/MM/yyyy").format(cal.time))
                calAlarm.set(year,month,dayOfMonth)
            }
        }
        editNoteBinding.buttonTambah.setOnClickListener {
            if (editNoteBinding.eJudul.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Data harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (tempUri == null) {
                Toast.makeText(this, "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                if (tempUri != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val temp = db.notesDao().tambahNote(
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
                        if(alarmChecked)setAlarm(editNoteBinding.eJudul.text.toString(),temp,editNoteBinding.eIsi.text.toString())
                        finish()
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val temp = db.notesDao().tambahNote(
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
                        if(alarmChecked)setAlarm(editNoteBinding.eJudul.text.toString(),temp,editNoteBinding.eIsi.text.toString())
                        finish()
                    }
                }

            }
        }
        editNoteBinding.buttonUpdate.setOnClickListener {
            if (editNoteBinding.eJudul.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (tempUri != null) {
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
//                        setAlarm(editNoteBinding.eJudul.text.toString(),temp)
                        finish()
                    }
                } else {
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
//                        setAlarm(editNoteBinding.eJudul.text.toString(),temp)
                        finish()
                    }
                }
            }
        }
        editNoteBinding.iGambar.setOnClickListener {
            if (!readNote) {
                imagePickDialog()
            }
        }
        editNoteBinding.switchAlarm.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked){
                alarmChecked =true
                editNoteBinding.tTanggal.visibility=View.VISIBLE
                editNoteBinding.tPukul.visibility=View.VISIBLE
            }else{
                alarmChecked=false
                editNoteBinding.tTanggal.visibility=View.GONE
                editNoteBinding.tPukul.visibility=View.GONE
            }
        }
        editNoteBinding.tTanggal.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val dpd = DatePickerDialog(this@EditNoteActivity,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
                dpd.datePicker.minDate = cal.timeInMillis
                dpd.show()
            }

        })

        editNoteBinding.tPukul.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                timePickerDialog()
            }
        })
    }

    private fun timePickerDialog() {
//        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND,0)
            cal.set(Calendar.MILLISECOND,0)

            editNoteBinding.tPukul.setText(SimpleDateFormat("HH:mm").format(cal.time))
            calAlarm.set(Calendar.HOUR_OF_DAY, hour)
            calAlarm.set(Calendar.MINUTE, minute)

        }
        TimePickerDialog(
            this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true
        ).show()
    }

    private fun imagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which ->
            if (which == 0) {

                easyPermissionManager.requestPermission(
                    "permission",
                    "permission are necessary,",
                    "setting",
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    uriBaru = FileProvider.getUriForFile(
                        this,
                        "org.d3if2122.mobpro2.noorinotes.provider",
                        createImageFile().also {
                        })
                    cameraLauncher.launch(uriBaru)
                }
            } else if (which == 1) {
                easyPermissionManager.requestPermission(
                    "permission",
                    "permission are necessary,",
                    "setting",
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    selectPictureLauncher.launch("image/*")
                }

            }
        }
        builder.create().show()
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(System.currentTimeMillis().toString(), ".jpg", storageDir)
    }

    private fun setView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        when (intentType()) {
            Constants.TYPE_CREATE -> {
                supportActionBar!!.title = "BUAT NOTE BARU"
                editNoteBinding.buttonTambah.visibility = View.VISIBLE
                editNoteBinding.buttonUpdate.visibility = View.GONE
                readNote = false
            }
            Constants.TYPE_READ -> {
                supportActionBar!!.title = "LIHAT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.GONE
//                editNoteBinding.switchAlarm.visibility = View.GONE
                editNoteBinding.lineLayout1.visibility = View.GONE
                editNoteBinding.eIsi.inputType = InputType.TYPE_NULL
                editNoteBinding.eJudul.inputType = InputType.TYPE_NULL
                editNoteBinding.eUrlLink.inputType = InputType.TYPE_NULL
                editNoteBinding.iGambar.setOnClickListener(null)
                readNote = true
                geNote()
            }
            Constants.TYPE_UPDATE -> {
                supportActionBar!!.title = "EDIT NOTE"
                editNoteBinding.buttonTambah.visibility = View.GONE
                editNoteBinding.buttonUpdate.visibility = View.VISIBLE
                readNote = false
                geNote()
            }
        }
        val date = getDateKosongan()
//        val dateInString = date.toTanggalString()
        editNoteBinding.tTanggal.setText(date.toTanggalString())
        editNoteBinding.tPukul.setText(date.toPukulString())

    }

    private fun geNote() {
        CoroutineScope(Dispatchers.IO).launch {
            val selectedNote = db.notesDao().getNote(noteId).get(0)

            runGetUriFavorite(selectedNote)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun intentType(): Int {
        return intent.getIntExtra("intent_type", 0)
    }

    private val selectPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                tempUri = it
                loadGambarURI(it)
            }
        }

    private fun loadGambarURI(it: Uri?) {
        runOnUiThread {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(editNoteBinding.iGambar)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            tempUri = uriBaru
            loadGambarURI(tempUri)
        }
    }

    fun Date.toPukulString(): String {
        val formatter = SimpleDateFormat("HH:mm").format(time)
        return formatter.format(this)
    }

    fun Date.toTanggalString(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy").format(time)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getDateKosongan(): Date {
        val formater = SimpleDateFormat(Constants.sdf)
        if (!timeAwal.isEmpty()) {
            val daw = formater.parse(timeAwal)
            val dak = formater.parse(timeAkhir)
            if (getCurrentDateTime().after(daw) && getCurrentDateTime().before(dak)) {
                return getCurrentDateTime()
            } else {
                return daw
            }
        } else {
            return getCurrentDateTime()
        }
    }

    private fun getBitmap(tempUri: Uri?): Bitmap? {
        return MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(tempUri.toString()))
    }

    private fun getBitmapDefault(): Bitmap? {
        return BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_image_24)
    }

    private fun runGetUriFavorite(selectedNote: Notes) {
        runOnUiThread(Runnable {
            editNoteBinding.eJudul.setText(selectedNote.judul)
            editNoteBinding.eIsi.setText(selectedNote.isi)
            editNoteBinding.eUrlLink.setText(selectedNote.urlLink)
            editNoteBinding.tTanggal.setText(selectedNote.tanggal.toString())
            dateTamppung = selectedNote.tanggal

            val photo = selectedNote.gambar

            val file =
                File(applicationContext.cacheDir, "CUSTOM NAME") //Get Access to a local file.
            file.delete() // Delete the File, just in Case, that there was still another File
            file.createNewFile()
            val fileOutputStream = file.outputStream()
            val byteArrayOutputStream = ByteArrayOutputStream()
            photo!!.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
            val bytearray = byteArrayOutputStream.toByteArray()
            fileOutputStream.write(bytearray)
            fileOutputStream.flush()
            fileOutputStream.close()
            byteArrayOutputStream.close()

            editNoteBinding.iGambar.setImageURI(file.toUri())

            tempUri = file.toUri()

        })
    }
}

class CustomHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
    }
}