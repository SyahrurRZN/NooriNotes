package org.d3if2122.mobpro2.noorinotes.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val judul: String,
    val isi: String,
    val urlLink: String,
    val gambar: String,
    val tanggal: String,
//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    val imageString : ByteArray
)
