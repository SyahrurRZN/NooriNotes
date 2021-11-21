package org.d3if2122.mobpro2.noorinotes

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Support.SQLiteHelper
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityAddNotesBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddNotesActivity : AppCompatActivity() {
    private lateinit var addNotesBinding: ActivityAddNotesBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    private val CAMERA_REQUEST_CODE=100
    private val STORAGE_REQUEST_CODE=101

    private val IMAGE_PICK_CAMERA_CODE=102
    private val IMAGE_PICK_STORAGE_CODE=101

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_notes)
        addNotesBinding = ActivityAddNotesBinding.inflate(layoutInflater)
        val rooyView = addNotesBinding.root
        setContentView(rooyView)

        var actionBar = supportActionBar
        actionBar!!.title="Add Notes"
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        sqLiteHelper = SQLiteHelper(this)

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm")
        addNotesBinding.tTanggal.text = dateInString

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        storagePermission = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        addNotesBinding.iGambar.setOnClickListener{
            imagePickDialog()
        }
    }

    private fun imagePickDialog() {
        val options = arrayOf("Camera","Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options){dialog, which->
            if (which==0){
                if (!checkCameraPermission()){
                    requestCameraPermission()
                }
                else{
                    pickFromCamera()
                }
            }
            else if (which==1){
                if(!checkStoragePermission()){
                    requestStoragePermission()
                }
                else{
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type="image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_STORAGE_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val resultC = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val resultS = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return resultC && resultS
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if(cameraAccepted && storageAccepted){
                        pickFromCamera()
                    }
                    else{
                        Toast.makeText(
                            this,
                            "Camera and Storage Permission Required!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            STORAGE_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if(storageAccepted){
                        pickFromGallery()
                    }
                    else{
                        Toast.makeText(
                            this,
                            "Storage Permission Required!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IMAGE_PICK_STORAGE_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    imageUri = resultUri
                    addNotesBinding.iGambar.setImageURI(resultUri)
                }
                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error = result.error
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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
//        val gambar = addNotesBinding.eGambar.text.toString().trim()
        val tanggal = addNotesBinding.tTanggal.text.toString()

        if(judul.isEmpty() || isi.isEmpty()){
            Toast.makeText(this,"Isi data yang diperlukan",Toast.LENGTH_SHORT).show()
        }else{
            val note = Notes(judul = judul,isi = isi,urlLink = urlLink, gambar = imageUri.toString(),tanggal = tanggal)
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