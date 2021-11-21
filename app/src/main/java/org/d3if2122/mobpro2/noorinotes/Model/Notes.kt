package org.d3if2122.mobpro2.noorinotes.Model

import java.util.*

data class Notes(
    var id: String,
    var judul: String="",
    var isi: String="",
    var urlLink: String="",
    var gambar: String="",
    var tanggal: String=""
)
