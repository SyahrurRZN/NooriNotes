package org.d3if2122.mobpro2.noorinotes.Model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val judul: String,
    val isi: String,
    val urlLink: String,
    val gambar: Bitmap?,
    val tanggal: Date?,
    val tanggalUpdate: Date
)
